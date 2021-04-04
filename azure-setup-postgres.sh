#!/bin/bash
set -e

COSMOS_RESOURCE_GROUP=cosmosdbrg
COSMOS_LOCATION=eastus

COSMOS_ACCOUNT_NAME="cosmos-13767"
DATABASE_NAME='database1'
COLLECTION_NAME='ShoppingCart'

KUBE_RESOURCE_GROUP=simpledbrg
KUBE_LOCATION=eastus

KUBE_ACR=simpledbregistry
ACR_LOGIN_SERVER=simpledbregistryserver
AZ_ACR_PASSWORD=password

DOCKER_IMAGE_TAG=simpledb:v1.2

AZ_AKS=simpledbakscluster
AZ_DNS_PREFIX=simpledbkubernetes
AKS_POD=simpledbpod

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


# Setup K8 cluster and registry

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
  --name $AZ_AKS \
  --vm-set-type VirtualMachineScaleSets \
  --enable-cluster-autoscaler \
  --min-count 3 \
  --max-count 5 \
  --attach-acr $KUBE_ACR \
  --dns-name-prefix $AZ_DNS_PREFIX \
  --generate-ssh-keys \
  --load-balancer-sku standard

az aks get-credentials \
  --resource-group=$KUBE_RESOURCE_GROUP \
  --name=$AZ_AKS \
  --overwrite-existing

# deploy image to AKS instance
kubectl apply -f deployment.yaml

kubectl get pods --watch