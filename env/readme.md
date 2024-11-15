
docker exec -it c304d90860ea /bin/bash


ALTER DATABASE nominatim
SET log_statement = 'all';


1) chmod 777 *.sh
2) download.sh  (wget https://download.geofabrik.de/europe/bulgaria-latest.osm.pbf)