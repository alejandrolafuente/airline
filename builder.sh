#!/bin/bash

chmod +x builder.sh

docker network create sagas-network

docker network create auth-network

docker network create client-network

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
