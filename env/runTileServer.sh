#!/bin/bash

#chmod +x run.sh
#./run.sh




docker volume create osm-data
docker run  -v "${PWD}/bulgaria-latest.osm.pbf:/data/region.osm.pbf"  -v osm-data:/data/database/  overv/openstreetmap-tile-server import
docker run -p 8080:80   -p 5434:5432 -v osm-data:/data/database/ -e ALLOW_CORS=enabled -d overv/openstreetmap-tile-server run 

#echo "Test tile server"
#curl -k -L --output tile-3005.png "http://localhost:8080/tile/13/4731/3005.png"



