#!/bin/bash

#chmod +x run.sh
#./run.sh




echo "Run osrm/osrm-backend"
docker run -t -v "${PWD}:/data" osrm/osrm-backend osrm-extract -p /opt/car.lua /data/bulgaria-latest.osm.pbf
docker run -t -v "${PWD}:/data" osrm/osrm-backend osrm-partition /data/bulgaria-latest.osm.pbf
docker run -t -v "${PWD}:/data" osrm/osrm-backend osrm-customize /data/bulgaria-latest.osm.pbf
docker run -t -i -p 5000:5000 -v "${PWD}:/data" osrm/osrm-backend osrm-routed --algorithm mld /data/bulgaria-latest.osm.pbf
    	
#Test Make requests against the HTTP server
#curl "http://127.0.0.1:5000/route/v1/driving/13.388860,52.517037;13.385983,52.496891?steps=true"



