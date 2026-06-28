chmod a+x ./tests/.github/workflows/wait-for-it.sh
docker compose -f docker-compose.yml up --detach  &&
echo "Docker is up" &&
./tests/.github/workflows/wait-for-it.sh -t 120 server:9090 &&
echo "Server is up" &&
./tests/.github/workflows/wait-for-it.sh -t 120 gateway:8080 &&
echo "Gateway is up"
result=$?
docker compose -f docker-compose.yml logs
exit $result
