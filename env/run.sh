#!/bin/bash

#chmod +x run.sh
#./run.sh

echo "Full purne"

docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)


docker volume rm $(docker volume ls  -q)

docker system prune -a

echo "Run	mediagis/nominatim:4.2"

docker run -it -e PBF_PATH=/nominatim/data/bulgaria-latest.osm.pbf -p 5433:5432 -p 8181:8080 -v "${PWD}:/nominatim/data"  -e NOMINATIM_PASSWORD=very_secure_password --name nominatim mediagis/nominatim:4.2
	
#	Note!!! where the /osm-maps/data/ directory contains monaco-latest.osm.pbf file that is mounted and available in container: /nominatim/data/monaco-latest.osm.pbf
	
	 
	#Test
	curl 'http://localhost:8181/http://127.0.0.1:8181/search?q=varna&limit=5&format=json&addressdetails=1'


docker stop nominatim

echo "Run overv/openstreetmap-tile-server"

docker volume create osm-data
docker run  -v "${PWD}/bulgaria-latest.osm.pbf:/data/region.osm.pbf"  -v osm-data:/data/database/  overv/openstreetmap-tile-server  import
docker run -p 8080:80   -p 5432:5432 -v osm-data:/data/database/ -e ALLOW_CORS=enabled -d overv/openstreetmap-tile-server run

echo "Test tile server"
	curl -k -L --output tile-3005.png "http://localhost:8080/tile/13/4731/3005.png"



echo "Run osrm/osrm-backend"
docker run -t -v "${PWD}:/data" osrm/osrm-backend osrm-extract -p /opt/car.lua /data/bulgaria-latest.osm.pbf
docker run -t -v "${PWD}:/data" osrm/osrm-backend osrm-partition /data/bulgaria-latest.osm.pbf
docker run -t -v "${PWD}:/data" osrm/osrm-backend osrm-customize /data/bulgaria-latest.osm.pbf
docker run -t -i -p 5000:5000 -v "${PWD}:/data" osrm/osrm-backend osrm-routed --algorithm mld /data/bulgaria-latest.osm.pbf
    	
#		Test Make requests against the HTTP server
			curl "http://127.0.0.1:5000/route/v1/driving/13.388860,52.517037;13.385983,52.496891?steps=true"

