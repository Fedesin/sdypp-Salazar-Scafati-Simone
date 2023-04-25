#!/bin/bash
docker exec -it pg  psql -U postgres -f create-database.sql