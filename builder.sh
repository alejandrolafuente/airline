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

./mvnw clean package

docker-compose up -d

cd ..

cd authentication-service

./mvnw clean package -DskipTests

docker-compose up -d

cd ..

cd client-service

./mvnw clean package -DskipTests

docker-compose up -d

cd ..

cd flight-service

./mvnw clean package -DskipTests

docker-compose up -d

cd ..

cd employee-service

./mvnw clean package -DskipTests

docker-compose up -d

cd ..

cd booking-command-service

./mvnw clean package -DskipTests

docker-compose up -d

cd ..

cd booking-query-service

./mvnw clean package -DskipTests

docker-compose up -d

cd ..

cd pgadmin

docker-compose up -d
