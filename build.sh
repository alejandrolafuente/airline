#!/bin/bash

chmod +x builder.sh

docker network create sagas-network

docker network create auth-network

docker network create client-network

docker network create flight-network

docker network create employee-network

docker network create command-network

docker network create query-network

cd back-end/broker-rabbitMQ

docker-compose up -d

cd ..

cd sagas

docker compose up -d --build # --build força a reconstrução da imagem

cd ..

cd authentication-service

docker-compose up -d --build

cd ..

cd client-service

docker-compose up -d --build

cd ..

cd flight-service

docker-compose up -d --build

cd ..

cd employee-service

docker-compose up -d --build

cd ..

cd booking-command-service

docker-compose up -d --build

cd ..

cd booking-query-service

docker-compose up -d --build

cd ..

cd pgadmin

docker-compose up -d
