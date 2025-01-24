#!/bin/bash

#chmod +x run.sh
#./run.sh

sudo zerotier-cli join a0cbf4b62ac3c44d

echo "stop "

docker stop tile-server
docker start tile-server
docker stop osrm
docker start osrm

docker stop nominatim
docker start  nominatim

docker  stop postgres_taxi
docker  start postgres_taxi

docker ps

echo "start "
