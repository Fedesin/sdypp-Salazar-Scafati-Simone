## Pasos de ejecucion
docker network create p2p_network

docker build -t extremo .
 
docker run --name extremo -p 8080:8080 -v /"cambiar_path_local"/sdypp-Salazar-Scafati-Simone/TP2/ej1/extremo/archivos:/app/archivos --network p2p_network extremo

docker build -t maestro .

docker run --name maestro -p 8081:8080  --network p2p_network  maestro