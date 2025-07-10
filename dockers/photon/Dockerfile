#java -jar photon-0.6.0.jar -nominatim-import -host localhost -port 5433 -database nominatim -user nominatim -password very_secure_password -languages bg,en,es,fr

FROM debian
#ARG JAR_FILE=photon/target/*.jar
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} photon-0.6.0.jar

RUN    apt update &&   apt -y install openjdk-17-jdk maven 
RUN    apt install -y wget && apt install iproute2 -y && apt-get install -y iputils-ping && apt install -y curl


EXPOSE 2322

#ENTRYPOINT ["java","-jar","/photon-0.6.0.jar"]
CMD ["java","-jar","/photon-0.6.0.jar"]
#172.17.0.6

#curl 'http://172.17.0.1:8181/search?q=varna&limit=5&format=json&addressdetails=1'
#docker container run -it --rm  --add-host=docker-host:`ip addr show docker0 | grep -Po 'inet \K[\d.]+'` name  



#get ip docker host 
#   ip addr show docker0 | grep -Po 'inet \K[\d.]+'

#create image
#   docker build -t photon:v1 . 

# with create volume 
#   docker volume create docker volume create photon_data_v1
#     docker container run -it --rm -v photon_data_v1:/photon_data  -p 8484:2322 photon:v1


#with fs
#mkdir photon_data
#   docker container run -it --rm  -v $(pwd)/photon_data:/photon_data  -p 8484:2322 name
#

#init +
#docker container run -it --rm -v photon_data_v1:/photon_data  -p 2322:2322 photon:v1 java -jar photon-0.6.0.jar -nominatim-import -host 172.17.0.1 -port 5433 -database nominatim -user nominatim -password very_secure_password -languages bg,en,es,fr

#docker container run -it --rm -v photon_data_v1:/photon_data  -p 2322:2322 photon:v1
#or (EXPOSE)
#docker container run -it  -v photon_data_v1:/photon_data   photon:v1


#docker container run -d --name photon  -v photon_data_v1:/photon_data   photon:v1 
#stop  docker stop photon


#use  java -jar photon-0.6.0.jar 
# intern curl http://localhost:2322/api?q=berlin&lon=10&lat=52
# ext  curl http://localhost:8484/api?q=berlin&lon=10&lat=52
