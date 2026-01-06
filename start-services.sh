#!/bin/bash

# ToolRent Microservices Startup Script
# This script starts all services in the correct order

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "================================"
echo "ToolRent Microservices Startup"
echo "================================"

# Check if required ports are available
check_port() {
    if lsof -Pi :$1 -sTCP:LISTEN -t >/dev/null 2>&1 ; then
        return 0
    else
        return 1
    fi
}

echo ""
echo "Checking available ports..."
PORTS=(8761 8888 8080 8081 8082 8083 8084 8085 8086)
for port in "${PORTS[@]}"; do
    if check_port $port; then
        echo "Warning: Port $port is already in use"
    fi
done

echo ""
echo "Building all services..."
mvn clean install -f "$SCRIPT_DIR/parent-pom.xml" -DskipTests

echo ""
echo "Starting Eureka Server (Port 8761)..."
cd "$SCRIPT_DIR/eureka-server"
mvn spring-boot:run &
EUREKA_PID=$!
echo "Eureka Server PID: $EUREKA_PID"

sleep 5

echo ""
echo "Starting Config Server (Port 8888)..."
cd "$SCRIPT_DIR/config-server"
mvn spring-boot:run &
CONFIG_PID=$!
echo "Config Server PID: $CONFIG_PID"

sleep 5

echo ""
echo "Starting Microservices..."

cd "$SCRIPT_DIR/client-service"
mvn spring-boot:run &
CLIENT_PID=$!
echo "Client Service PID: $CLIENT_PID"

cd "$SCRIPT_DIR/employee-service"
mvn spring-boot:run &
EMPLOYEE_PID=$!
echo "Employee Service PID: $EMPLOYEE_PID"

cd "$SCRIPT_DIR/fee-service"
mvn spring-boot:run &
FEE_PID=$!
echo "Fee Service PID: $FEE_PID"

cd "$SCRIPT_DIR/kardex-service"
mvn spring-boot:run &
KARDEX_PID=$!
echo "Kardex Service PID: $KARDEX_PID"

cd "$SCRIPT_DIR/lend-service"
mvn spring-boot:run &
LEND_PID=$!
echo "Lend Service PID: $LEND_PID"

cd "$SCRIPT_DIR/tool-service"
mvn spring-boot:run &
TOOL_PID=$!
echo "Tool Service PID: $TOOL_PID"

sleep 5

echo ""
echo "Starting API Gateway (Port 8080)..."
cd "$SCRIPT_DIR/api-gateway"
mvn spring-boot:run &
GATEWAY_PID=$!
echo "API Gateway PID: $GATEWAY_PID"

echo ""
echo "================================"
echo "All services started!"
echo "================================"
echo ""
echo "Service URLs:"
echo "  Eureka Dashboard: http://localhost:8761"
echo "  Config Server: http://localhost:8888"
echo "  API Gateway: http://localhost:8080"
echo "  Client Service: http://localhost:8081"
echo "  Employee Service: http://localhost:8082"
echo "  Fee Service: http://localhost:8083"
echo "  Kardex Service: http://localhost:8084"
echo "  Lend Service: http://localhost:8085"
echo "  Tool Service: http://localhost:8086"
echo ""
echo "To stop all services, run: ./stop-services.sh"
echo ""
echo "Service PIDs:"
echo "  Eureka: $EUREKA_PID"
echo "  Config: $CONFIG_PID"
echo "  Client: $CLIENT_PID"
echo "  Employee: $EMPLOYEE_PID"
echo "  Fee: $FEE_PID"
echo "  Kardex: $KARDEX_PID"
echo "  Lend: $LEND_PID"
echo "  Tool: $TOOL_PID"
echo "  Gateway: $GATEWAY_PID"

# Keep the script running
wait
