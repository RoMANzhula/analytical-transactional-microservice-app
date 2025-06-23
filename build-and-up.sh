# Function to stop the script in case of error
function error_exit {
    echo "$1" 1>&2
    exit 1
}

echo "Checking for Maven..."
command -v mvn >/dev/null 2>&1 || error_exit "!!! Maven not found. Install Maven and try again."

echo "Checking for Docker..."
command -v docker >/dev/null 2>&1 || error_exit "!!! Docker not found. Install Docker and try again."

echo "Checking for Docker Compose..."
command -v docker-compose >/dev/null 2>&1 || error_exit "!!! Docker Compose not found. Install Docker Compose and try again."

# List of micoservices
services=(
    "eureka-server"
    "api-gateway"
    "user-service"
    "transaction-service"
    "notification-service"
    "analytics-service"
)

echo "Building all microservices with Maven..."

for service in "${services[@]}"
do
    echo "Building $service..."
    cd $service || error_exit "Failed to change directory to $service"
    mvn clean package -DskipTests || error_exit "Maven build failed for $service"
    cd ..
done

echo "All services build successfully!"

echo "Start Docker Compose with --build option..."
docker-compose up --build

