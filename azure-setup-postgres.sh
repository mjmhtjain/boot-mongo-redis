#!/bin/bash
set -e

uniqueId=$RANDOM
AZ_RESOURCE_GROUP=cosmosdbrg
AZ_LOCATION=eastus

accountName="cosmos-$uniqueId"
databaseName='database1'
collectionName='ShoppingCart'

AZ_ACR=simpledbregistry
ACR_LOGIN_SERVER=simpledbregistryserver
AZ_ACR_PASSWORD=password

DOCKER_IMAGE_TAG=simpledb:v1.2

AZ_AKS=simpledbakscluster
AZ_DNS_PREFIX=simpledbkubernetes
AKS_POD=simpledbpod

# Create a resource group.
az group create \
    --name $AZ_RESOURCE_GROUP \
    --location $AZ_LOCATION \
    | jq

# Create MongoDB server
az cosmosdb create \
    -n $accountName \
    -g $AZ_RESOURCE_GROUP \
    --kind MongoDB \
    --locations regionName='East US' failoverPriority=0 isZoneRedundant=True

az cosmosdb mongodb database create \
    -a $accountName \
    -g $AZ_RESOURCE_GROUP \
    -n $databaseName

az cosmosdb mongodb collection create \
    -a $accountName \
    -g $AZ_RESOURCE_GROUP \
    -d $databaseName \
    -n $collectionName \
    --throughput "10000"

# disable georedundancy and enable auto-scale in collection

# Create a registry
az acr create --resource-group $AZ_RESOURCE_GROUP \
  --location $AZ_LOCATION \
  --name $AZ_ACR \
  --sku Basic \
  --admin-enabled true

# push image to ACR
az acr import  \
  -n $AZ_ACR \
  --source docker.io/mjmhtjain/$DOCKER_IMAGE_TAG \
  --image $DOCKER_IMAGE_TAG

# create AKS cluster
az aks create \
  --resource-group $AZ_RESOURCE_GROUP \
  --name $AZ_AKS \
  --vm-set-type VirtualMachineScaleSets \
  --enable-cluster-autoscaler \
  --min-count 3 \
  --max-count 5 \
  --attach-acr $AZ_ACR \
  --dns-name-prefix $AZ_DNS_PREFIX \
  --generate-ssh-keys \
  --load-balancer-sku standard

az aks get-credentials \
  --resource-group=$AZ_RESOURCE_GROUP \
  --name=$AZ_AKS \
  --overwrite-existing

# deploy image to AKS instance
kubectl apply -f deployment.yaml

kubectl get pods --watch