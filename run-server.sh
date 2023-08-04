
echo "Docker Compose Up..."
docker-compose -f ./docker/docker-compose.yml -p preonboarding up -d

# MySQL이 완전히 준비될 때까지 대기
echo "Waiting for MySQL to be ready..."
while [ -z "$(docker ps --filter "name=preonboarding-database" --format "{{.Names}}")" ]; do
    sleep 1
done

echo "Waiting for Redis to be ready..."
while [ -z "$(docker ps --filter "name=preonboarding-cache" --format "{{.Names}}")" ]; do
    sleep 1
done

./gradlew bootRun

