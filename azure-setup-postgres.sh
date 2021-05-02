#!/bin/bash
set -e

COSMOS_RESOURCE_GROUP=cosmosdbrg
COSMOS_LOCATION=eastus

COSMOS_ACCOUNT_NAME="cosmos-13767"
DATABASE_NAME='database1'
COLLECTION_NAME='ShoppingCart'

# Setting up Mongo Resources
az group create \
    --name $COSMOS_RESOURCE_GROUP \
    --location $COSMOS_LOCATION \
    | jq

az cosmosdb create \
    -n $COSMOS_ACCOUNT_NAME \
    -g $COSMOS_RESOURCE_GROUP \
    --kind MongoDB \
    --locations regionName='East US' failoverPriority=0 isZoneRedundant=True

az cosmosdb mongodb database create \
    -a $COSMOS_ACCOUNT_NAME \
    -g $COSMOS_RESOURCE_GROUP \
    -n $DATABASE_NAME

az cosmosdb mongodb collection create \
    -a $COSMOS_ACCOUNT_NAME \
    -g $COSMOS_RESOURCE_GROUP \
    -d $DATABASE_NAME \
    -n $COLLECTION_NAME \
    --throughput "10000"


# Redis Cluster setup
REDIS_RESOURCE_GROUP=redisrg
REDIS_LOCATION=eastus
REDIS_NAME=redis-13767

az group create \
    --name $REDIS_RESOURCE_GROUP \
    --location $REDIS_LOCATION \
    | jq

az redis create \
  --location $REDIS_LOCATION \
  --name $REDIS_NAME \
  --resource-group $REDIS_RESOURCE_GROUP \
  --sku Basic \
  --minimum-tls-version 1.2 \
  --vm-size c0 ## {c0, c1, c2, c3, c4, c5, c6, p1, p2, p3, p4, p5}

# Setup K8 cluster and registry
KUBE_RESOURCE_GROUP=simpledbrg
KUBE_LOCATION=eastus

KUBE_ACR=simpledbregistry
DOCKER_IMAGE_TAG=simpledb-mongo:latest

KUBE_AKS=simpledbakscluster
KUBE_DNS_PREFIX=simpledbkubernetes
AKS_POD=simpledbpod

az group create \
    --name $KUBE_RESOURCE_GROUP \
    --location $KUBE_LOCATION \
    | jq

# Create a registry
az acr create --resource-group $KUBE_RESOURCE_GROUP \
  --location $KUBE_LOCATION \
  --name $KUBE_ACR \
  --sku Basic \
  --admin-enabled true

# push image to ACR
az acr import  \
  -n $KUBE_ACR \
  --source docker.io/mjmhtjain/$DOCKER_IMAGE_TAG \
  --image $DOCKER_IMAGE_TAG

# create AKS cluster
az aks create \
  --resource-group $KUBE_RESOURCE_GROUP \
  --name $KUBE_AKS \
  --vm-set-type VirtualMachineScaleSets \
  --enable-cluster-autoscaler \
  --min-count 3 \
  --max-count 5 \
  --attach-acr $KUBE_ACR \
  --dns-name-prefix $KUBE_DNS_PREFIX \
  --generate-ssh-keys \
  --load-balancer-sku standard

az aks get-credentials \
  --resource-group=$KUBE_RESOURCE_GROUP \
  --name=$KUBE_AKS \
  --overwrite-existing

# deploy image to AKS instance
kubectl apply -f deployment.yaml

kubectl get pods --watch