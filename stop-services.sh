#!/bin/bash

# ToolRent Microservices Stop Script
# This script stops all running service processes

echo "Stopping all ToolRent microservices..."

# Kill all java processes running spring-boot:run
pkill -f "spring-boot:run" || true

echo "All services stopped!"
