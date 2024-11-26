
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

test  for Тодор Хаджистанчев 1
	curl http://localhost:2322/api?q=%D0%A2%D0%BE%D0%B4%D0%BE%D1%80%20%D0%A5%D0%B0%D0%B4%D0%B6%D0%B8%D1%81%D1%82%D0%B0%D0%BD%D1%87%D0%B5%D0%B2%201

	

