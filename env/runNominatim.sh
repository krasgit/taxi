#!/bin/bash

#chmod +x run.sh
#./run.sh

echo "Run	mediagis/nominatim:4.2"

docker run -it -e PBF_PATH=/nominatim/data/bulgaria-latest.osm.pbf -p 5433:5432 -p 8181:8080 -v "${PWD}:/nominatim/data"  -e NOMINATIM_PASSWORD=very_secure_password --name nominatim mediagis/nominatim:4.2

#docker stop nominatim	
#Note!!! where the /osm-maps/data/ directory contains monaco-latest.osm.pbf file that is mounted and available in container: /nominatim/data/monaco-latest.osm.pbf
#DB  psql localhost:5433  DBname:nominatim DBuser:nominatim DBpassword:very_secure_password 
#Test	curl 'http://127.0.0.1:8181/search?q=varna&limit=5&format=json&addressdetails=1'





