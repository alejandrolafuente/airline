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

docker-compose up -d

cd ..

cd authentication-service

docker-compose up -d

cd ..

cd client-service

docker-compose up -d

cd ..

cd flight-service

docker-compose up -d

cd ..

cd employee-service

docker-compose up -d

cd ..

cd booking-command-service

docker-compose up -d

cd ..

cd booking-query-service

docker-compose up -d

cd ..

cd pgadmin

docker-compose up -d
