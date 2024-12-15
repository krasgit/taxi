#!/bin/bash

#chmod +x run.sh
#./run.sh

sudo zerotier-cli join a0cbf4b62ac3c44d

echo "stop "

docker stop musing_ardinghelli
docker start musing_ardinghelli
docker stop compassionate_meninsky
docker start compassionate_meninsky

docker stop nominatim
docker start  nominatim

docker  stop postgres_taxi
docker  start postgres_taxi

docker ps

echo "start "
