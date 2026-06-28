#!/bin/bash

chmod a+x ./tests/.github/workflows/wait-for-it.sh

docker compose -f docker-compose.yml up -d
echo "Docker is up"

./tests/.github/workflows/wait-for-it.sh -t 120 localhost:9090
server_result=$?

if [ $server_result -ne 0 ]; then
  docker compose -f docker-compose.yml logs
  exit $server_result
fi

echo "Server is up"

./tests/.github/workflows/wait-for-it.sh -t 120 localhost:8080
gateway_result=$?

docker compose -f docker-compose.yml logs

if [ $gateway_result -ne 0 ]; then
  exit $gateway_result
fi

echo "Gateway is up"
exit 0
