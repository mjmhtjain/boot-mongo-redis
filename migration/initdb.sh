#!/bin/bash
set -e

scriptdir="$(dirname "$0")"
cd "$scriptdir"

HOST=simpledbpostgres.postgres.database.azure.com
PORT=5432
USERNAME=postgresadmin
DBNAME=postgres
FILE1_PATH=./V1__ddl.sql

chmod 600 ./.pgpass

PGPASSFILE=./.pgpass psql \
  --file=$FILE1_PATH \
  --host=$HOST \
  --port=$PORT \
  --username=$USERNAME \
  --dbname=$DBNAME