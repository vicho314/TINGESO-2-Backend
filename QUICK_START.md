# Quick Start Guide - ToolRent Microservices

## Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- PostgreSQL 12+ (or H2 for quick testing)
- Docker & Docker Compose (optional)

## Option 1: Quick Start with Docker Compose (Easiest)

```bash
# 1. Navigate to project root
cd /home/huachimingo/Documents/usach/TINGESO-2-Backend

# 2. Build all services
mvn clean install -f parent-pom.xml

# 3. Build Docker images (or skip if you have them)
docker-compose build

# 4. Start all services
docker-compose up -d

# 5. Check service status
docker-compose ps

# 6. View logs
docker-compose logs -f
```

**Access points:**
- API Gateway: http://localhost:8080
- Eureka Dashboard: http://localhost:8761
- Config Server: http://localhost:8888

## Option 2: Local Development

### Step 1: Set Environment Variables
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/tingeso
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=postgres
export SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

### Step 2: Build All Services
```bash
mvn clean install -f parent-pom.xml
```

### Step 3: Start Services (in separate terminals)

**Terminal 1 - Eureka Server:**
```bash
cd eureka-server
mvn spring-boot:run
```

**Terminal 2 - Config Server:**
```bash
cd config-server
mvn spring-boot:run
```

**Wait 5 seconds, then Terminal 3-8 - All Microservices:**
```bash
cd client-service && mvn spring-boot:run &
cd employee-service && mvn spring-boot:run &
cd fee-service && mvn spring-boot:run &
cd kardex-service && mvn spring-boot:run &
cd lend-service && mvn spring-boot:run &
cd tool-service && mvn spring-boot:run &
```

**Terminal 9 - API Gateway (start last):**
```bash
cd api-gateway
mvn spring-boot:run
```

### Step 4: Verify Everything is Running
1. Check Eureka Dashboard: http://localhost:8761
   - You should see all 6 microservices registered
   
2. Test the API Gateway: http://localhost:8080/api/v1/client/
   - Should return an empty list (or existing data)

## Option 3: Automated Startup Script

```bash
./start-services.sh
```

This will build and start all services in the correct order. Watch the output for any errors.

To stop all services:
```bash
./stop-services.sh
```

## Testing the API

Once services are running, test with curl:

```bash
# Get all clients
curl http://localhost:8080/api/v1/client/

# Create a new client
curl -X POST http://localhost:8080/api/v1/client/ \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "telephone": "555-1234",
    "mail": "john@example.com",
    "clientState": "A",
    "rut": "12345678-9"
  }'

# Get client by ID
curl http://localhost:8080/api/v1/client/1

# Update client
curl -X PUT http://localhost:8080/api/v1/client/ \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "name": "Jane Doe",
    "telephone": "555-5678",
    "mail": "jane@example.com",
    "clientState": "A",
    "rut": "12345678-9"
  }'

# Delete client
curl -X DELETE http://localhost:8080/api/v1/client/1
```

Similar endpoints exist for other services by replacing `/api/v1/client/` with:
- `/api/v1/employee/`
- `/api/v1/fee/`
- `/api/v1/kardex/`
- `/api/v1/lend/`
- `/api/v1/tool/`

## Service Ports Reference

| Service | Port | Purpose |
|---------|------|---------|
| Eureka Server | 8761 | Service Discovery Dashboard |
| Config Server | 8888 | Configuration Management |
| API Gateway | 8080 | Client Entry Point |
| Client Service | 8081 | Client Management |
| Employee Service | 8082 | Employee Management |
| Fee Service | 8083 | Fee Management |
| Kardex Service | 8084 | Tool Movement History |
| Lend Service | 8085 | Tool Lending |
| Tool Service | 8086 | Tool Inventory |

## Monitoring

### View Eureka Dashboard
```
http://localhost:8761
```

### Check Service Health
```bash
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
# ... for each service
```

### View Service Logs
```bash
# Individual service logs
tail -f <service-name>/logs/*.log

# Or use docker compose logs
docker-compose logs -f client-service
```

## Stopping Services

**If using Docker Compose:**
```bash
docker-compose down
```

**If running locally:**
```bash
./stop-services.sh
```

**Or manually kill Java processes:**
```bash
pkill -f spring-boot
```

## Common Issues

### Port Already in Use
If you get "Address already in use" error:
```bash
# Find and kill process using the port
lsof -i :8080
kill -9 <PID>

# Or change service ports in application.yaml files
```

### Services Not Registering with Eureka
- Wait 30 seconds for registration
- Check service logs for connection errors
- Ensure Eureka server started first
- Verify all services have `@EnableDiscoveryClient`

### Database Connection Errors
- Ensure PostgreSQL is running
- Check database credentials in environment variables
- For quick testing, use H2 (in-memory database)

### API Gateway 503 Service Unavailable
- Check Eureka dashboard to see if services are registered
- Wait for all services to become "UP"
- Verify services are accessible on their individual ports

## Next Steps

1. **Read Full Documentation**: See `MICROSERVICES_README.md` for detailed information
2. **Read Refactoring Summary**: See `REFACTORING_SUMMARY.md` for architecture details
3. **Explore APIs**: Use Swagger UI or curl to test endpoints
4. **Monitor Services**: Watch Eureka dashboard for service health
5. **Scale Services**: Add more instances in docker-compose.yml
6. **Implement Service Communication**: Add Feign clients for inter-service calls

## Support

For detailed information, see:
- `MICROSERVICES_README.md` - Complete operational guide
- `REFACTORING_SUMMARY.md` - Architecture and design decisions
- Individual service logs in each service directory
- Spring Cloud Documentation: https://spring.io/cloud

## Quick Commands Reference

```bash
# Build all services
mvn clean install -f parent-pom.xml

# Start with Docker Compose
docker-compose up -d

# Stop with Docker Compose
docker-compose down

# View logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f <service-name>

# Test API Gateway
curl http://localhost:8080/api/v1/client/

# Check service health
curl http://localhost:8081/actuator/health

# View Eureka Dashboard
# Open browser to: http://localhost:8761
```

---

Happy coding! 🚀
