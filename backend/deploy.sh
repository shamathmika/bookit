#!/bin/bash

# Exit on error
set -e

# Disable BuildKit
export DOCKER_BUILDKIT=0

# Load environment variables
if [ -f .env ]; then
    export $(cat .env | grep -v '^#' | xargs)
else
    echo "Error: .env file not found"
    exit 1
fi

# Build and start containers
echo "Building and starting containers..."
docker-compose up -d --build

# Wait for application to be ready
echo "Waiting for application to be ready..."
sleep 30

# Check application health
echo "Checking application health..."
curl -f http://localhost:8080/actuator/health || {
    echo "Application health check failed"
    docker-compose logs app
    exit 1
}

echo "Deployment completed successfully!" 