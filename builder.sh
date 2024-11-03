#!/bin/bash

# chmod +x builder.sh

for container in al-broker al-client-db al-command-db al-flight-db al-query-db pgadmin-container; do
    echo "Iniciando container: $container"
    docker start "$container"
done

for container in al-client-db al-command-db al-flight-db al-query-db; do
    echo -n "$container: "
    docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' "$container"
done
