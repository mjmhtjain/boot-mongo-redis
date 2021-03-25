#!/bin/bash
set -e

AZ_RESOURCE_GROUP=simpledbresourcegroup
AZ_LOCATION=eastus

AZ_POSTGRES_SERVER=simpledbpostgres
AZ_POSTGRES_USERNAME=puffyFerret0
AZ_POSTGRES_PASSWORD=ip69BiBpQ2XPdyNGKofeSQ
AZ_POSTGRES_FIREWALL_RULE_ALLOW_AZURE=AllowAllAzureServices
AZ_POSTGRES_FIREWALL_RULE_ALLOW_LOCAL=AllowAllLocal
AZ_POSTGRES_FIREWALL_ALLOW_AZURE_START=0.0.0.0
AZ_POSTGRES_FIREWALL_ALLOW_AZURE_END=0.0.0.0
IP_ADDR=$(curl "http://whatismyip.akamai.com/")

AZ_ACR=simpledbregistry
ACR_LOGIN_SERVER=simpledbregistryserver
AZ_ACR_PASSWORD=password

DOCKER_IMAGE_TAG=simpledb

AZ_AKS=simpledbakscluster
AZ_DNS_PREFIX=simpledbkubernetes
AKS_POD=simpledbpod

# Create a resource group.
az group create \
    --name $AZ_RESOURCE_GROUP \
    --location $AZ_LOCATION \
    | jq

# Create postgres server
az postgres server create \
  -l $AZ_LOCATION \
  -g $AZ_RESOURCE_GROUP \
  -n $AZ_POSTGRES_SERVER \
  -u $AZ_POSTGRES_USERNAME \
  -p $AZ_POSTGRES_PASSWORD

az postgres server firewall-rule create \
  --name $AZ_POSTGRES_FIREWALL_RULE_ALLOW_AZURE \
  --resource-group $AZ_RESOURCE_GROUP \
  --server-name $AZ_POSTGRES_SERVER \
  --start-ip-address $AZ_POSTGRES_FIREWALL_ALLOW_AZURE_START \
  --end-ip-address $AZ_POSTGRES_FIREWALL_ALLOW_AZURE_END

az postgres server firewall-rule create \
  --name $AZ_POSTGRES_FIREWALL_RULE_ALLOW_LOCAL \
  --resource-group $AZ_RESOURCE_GROUP \
  --server-name $AZ_POSTGRES_SERVER \
  --start-ip-address $IP_ADDR \
  --end-ip-address $IP_ADDR

# Create a registry
az acr create --resource-group $AZ_RESOURCE_GROUP \
  --location $AZ_LOCATION \
  --name $AZ_ACR \
  --sku Basic \
  --admin-enabled true

ACR_LOGIN_SERVER=$(az acr list \
  --resource-group $AZ_RESOURCE_GROUP \
  | jq -r '.[0].loginServer')

# push image to ACR
az acr import  \
  -n $AZ_ACR \
  --source docker.io/mjmhtjain/$DOCKER_IMAGE_TAG:latest \
  --image $DOCKER_IMAGE_TAG:latest

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

kubectl get services --watch