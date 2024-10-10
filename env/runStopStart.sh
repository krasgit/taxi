#!/bin/bash

#chmod +x run.sh
#./run.sh

sudo zerotier-cli join a0cbf4b62ac3c44d

echo "stop "


docker stop unruffled_haslett
docker start unruffled_haslett

docker stop compassionate_dubinsky
docker start compassionate_dubinsky

docker stop nominatim
docker start  nominatim

docker ps

echo "start "
