#!/bin/bash
set -e

AZ_RESOURCE_GROUP=simpledbresourcegroup
AZ_LOCATION=eastus

AKS_POSTGRES=simpledbpostgres
AKS_POSTGRES_USERNAME=postgresadmin
AKS_POSTGRES_PASSWORD=postgresadmin
AKS_POSTGRES_FIREWALL_RULE=AllowAllAzureServicesAndResourcesWithinAzureIps
AKS_POSTGRES_FIREWALL_START=0.0.0.0
AKS_POSTGRES_FIREWALL_END=0.0.0.0
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

# Create postgres flex server
az postgres flexible-server create \
  --location $AZ_LOCATION \
  --resource-group $AZ_RESOURCE_GROUP \
  --name $AKS_POSTGRES \
  --admin-user $AKS_POSTGRES_USERNAME \
  --admin-password $AKS_POSTGRES_PASSWORD \
  --public-access $IP_ADDR

az postgres flexible-server firewall-rule create \
  --resource-group $AZ_RESOURCE_GROUP \
  --name $AKS_POSTGRES \
  --rule-name $AKS_POSTGRES_FIREWALL_RULE \
  --start-ip-address $AKS_POSTGRES_FIREWALL_START \
  --end-ip-address $AKS_POSTGRES_FIREWALL_END

# Create a registry
az acr create --resource-group $AZ_RESOURCE_GROUP \
  --location $AZ_LOCATION \
  --name $AZ_ACR \
  --sku Basic \
  --admin-enabled true

ACR_LOGIN_SERVER=$(az acr list \
  --resource-group $AZ_RESOURCE_GROUP \
  | jq -r '.[0].loginServer')


# login to registry
AZ_ACR_PASSWORD=$(az acr credential show \
  --resource-group $AZ_RESOURCE_GROUP \
  --name $AZ_ACR | jq -r '.passwords[0].value')

# build docker image
docker build --tag $DOCKER_IMAGE_TAG .
docker tag $DOCKER_IMAGE_TAG $ACR_LOGIN_SERVER/$DOCKER_IMAGE_TAG:latest

# push image to ACR
echo $AZ_ACR_PASSWORD | docker login $ACR_LOGIN_SERVER \
  --username $AZ_ACR \
  --password-stdin

docker push $ACR_LOGIN_SERVER/$DOCKER_IMAGE_TAG:latest


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