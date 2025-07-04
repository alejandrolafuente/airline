#!/bin/bash
chmod +x builder.sh

docker network create sagas-network

docker network create auth-network

docker network create client-network

docker network create flight-network

docker network create employee-network

docker network create command-network

docker network create query-network

# Função para encontrar porta livre
find_free_port() {
    local base_port=$1
    while (netstat -tuln | grep -q ":${base_port} ") || (ss -tuln | grep -q ":${base_port} "); do
        ((base_port++))
    done
    echo $base_port
}

# Define portas padrão ou alternativas
export RABBIT_PORT=$(find_free_port 5672)
export RABBIT_MGMT_PORT=$(find_free_port 15672)

echo "Usando portas: RabbitMQ AMQP=${RABBIT_PORT}, Management=${RABBIT_MGMT_PORT}"

cd back-end/broker-rabbitMQ

docker-compose up -d

cd ..

export SAGAS_PORT=$(find_free_port 8080)

cd sagas

docker compose up -d --build # --build força a reconstrução da imagem

echo "Sagas service rodando em: ${SAGAS_PORT}"

# cd ..

# cd authentication-service

# docker-compose up -d --build

# cd ..

# cd client-service

# docker-compose up -d --build

# cd ..

# cd flight-service

# docker-compose up -d --build

# cd ..

# cd employee-service

# docker-compose up -d --build

# cd ..

# cd booking-command-service

# docker-compose up -d --build

# cd ..

# cd booking-query-service

# docker-compose up -d --build

# cd ..

# cd pgadmin

# docker-compose up -d
