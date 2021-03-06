#!/bin/bash
set -e

AZ_RESOURCE_GROUP=webfluxapiservice
AZ_LOCATION=eastus
AKS_POSTGRES=webfluxpostgres
AKS_POSTGRES_USERNAME=webfluxadmin
AKS_POSTGRES_PASSWORD=webfluxadmin
IP_ADDR=$(curl "http://whatismyip.akamai.com/")

# Create postgres flex server
az postgres flexible-server create \
  --location $AZ_LOCATION \
  --resource-group $AZ_RESOURCE_GROUP \
  --name $AKS_POSTGRES \
  --admin-user $AKS_POSTGRES_USERNAME \
  --admin-password $AKS_POSTGRES_PASSWORD \
  --public-access $IP_ADDR