
docker exec -it c304d90860ea /bin/bash


ALTER DATABASE nominatim
SET log_statement = 'all';


1) chmod 777 *.sh
2) download.sh  (wget https://download.geofabrik.de/europe/bulgaria-latest.osm.pbf)


setup https://github.com/komoot/photon
build 
	gradlew app:es_embedded:build
init 
	java -jar photon-0.6.0.jar -nominatim-import -host localhost -port 5433 -database nominatim -user nominatim -password very_secure_password
run java -jar photon-0.6.0.jar


