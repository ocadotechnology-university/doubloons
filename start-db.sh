#!/bin/bash
CONTAINER_NAME=doubloons-db
DB_NAME=doubloons
PASSWORD=admin
docker rm -f $CONTAINER_NAME
docker run -d -p 5432:5432 --name $CONTAINER_NAME -e POSTGRES_PASSWORD=$PASSWORD postgres
sleep 5
docker exec -i $CONTAINER_NAME createdb -U postgres $DB_NAME
docker exec -i $CONTAINER_NAME psql -U postgres -d $DB_NAME < db-scheme.sql