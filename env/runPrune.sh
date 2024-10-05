#!/bin/bash

#chmod +x run.sh
#./run.sh


echo "Full purne"

docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)


docker volume rm $(docker volume ls  -q)

docker system prune -a



