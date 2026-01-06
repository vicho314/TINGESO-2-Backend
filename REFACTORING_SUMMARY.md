# ToolRent Microservices - Refactoring Summary

## Overview
The ToolRent backend application has been successfully refactored from a monolithic architecture into a cloud-native microservices architecture using Spring Cloud.

## What Was Done

### 1. **Created Infrastructure Services**
   - **Eureka Server** (Port 8761): Netflix Eureka for service discovery and registration
   - **Config Server** (Port 8888): Spring Cloud Config for centralized configuration management
   - **API Gateway** (Port 8080): Spring Cloud Gateway for request routing and load balancing

### 2. **Extracted Microservices**
   Each entity has been extracted into its own independent microservice:
   
   - **Client Service** (Port 8081)
     - Manages client information and validation
     - Database table: `client`
   
   - **Employee Service** (Port 8082)
     - Manages employee data and authentication
     - Database table: `employee`
   
   - **Fee Service** (Port 8083)
     - Manages rental and delay fees
     - Database table: `fee`
   
   - **Kardex Service** (Port 8084)
     - Tracks tool movements and history
     - Database table: `kardex`
   
   - **Lend Service** (Port 8085)
     - Manages tool lending transactions
     - Database table: `lend`
   
   - **Tool Service** (Port 8086)
     - Manages tool inventory and status
     - Database table: `tool`

### 3. **Architecture Improvements**

#### Service Discovery
- Services automatically register with Eureka
- Dynamic service discovery - no hardcoded IP addresses
- Load balancing across multiple instances
- Health monitoring via Eureka dashboard

#### Configuration Management
- Centralized configuration for all services
- Environment-specific configurations
- No need to rebuild images for configuration changes
- Dynamic property management

#### API Gateway
- Single entry point for all client requests
- Route-based request forwarding
- Load balancing with Ribbon
- Circuit breaker patterns ready for implementation

### 4. **Technology Stack**
- **Spring Boot**: 3.4.4
- **Spring Cloud**: 2024.0.0
- **Java**: 17
- **Maven**: 3.6+
- **PostgreSQL**: 12+ (or H2 for development)
- **Docker & Docker Compose**: For containerized deployment

### 5. **File Structure**
```
TINGESO-2-Backend/
├── parent-pom.xml                 # Parent Maven POM
├── eureka-server/                 # Eureka Service Discovery
├── config-server/                 # Config Server
│   └── src/main/resources/config-files/
│       ├── client-service.yaml
│       ├── employee-service.yaml
│       ├── fee-service.yaml
│       ├── kardex-service.yaml
│       ├── lend-service.yaml
│       └── tool-service.yaml
├── api-gateway/                   # API Gateway
├── client-service/                # Client Microservice
├── employee-service/              # Employee Microservice
├── fee-service/                   # Fee Microservice
├── kardex-service/                # Kardex Microservice
├── lend-service/                  # Lend Microservice
├── tool-service/                  # Tool Microservice
├── docker-compose.yml             # Docker orchestration
├── start-services.sh              # Startup script
├── stop-services.sh               # Shutdown script
└── MICROSERVICES_README.md        # Detailed documentation
```

## Service Communication

### Through API Gateway
```
Client Application → API Gateway (8080)
                        ↓
                  Routes to appropriate service
                        ↓
           Client/Employee/Fee/Kardex/Lend/Tool Service
```

### Service-to-Service (Future)
Services can communicate with each other using:
- RestTemplate with Eureka client
- Feign Client (add spring-cloud-starter-openfeign)
- Service discovery client

## Configuration

### Environment Variables
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/tingeso
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=postgres
export SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

### Eureka Configuration
All services automatically register with Eureka. Check registration at:
```
http://localhost:8761/eureka/apps
```

## Running the Services

### Option 1: Local Development
```bash
# Terminal 1 - Eureka
cd eureka-server && mvn spring-boot:run

# Terminal 2 - Config Server
cd config-server && mvn spring-boot:run

# Terminal 3-8 - Microservices
cd client-service && mvn spring-boot:run
cd employee-service && mvn spring-boot:run
cd fee-service && mvn spring-boot:run
cd kardex-service && mvn spring-boot:run
cd lend-service && mvn spring-boot:run
cd tool-service && mvn spring-boot:run

# Terminal 9 - API Gateway
cd api-gateway && mvn spring-boot:run
```

### Option 2: Using Startup Script
```bash
./start-services.sh
```

### Option 3: Docker Compose
```bash
mvn clean install -f parent-pom.xml
docker-compose up -d
```

## API Endpoints

All services are accessible through the API Gateway:

### Client Service
- GET  `/api/v1/client/` - List all clients
- GET  `/api/v1/client/{id}` - Get client by ID
- POST `/api/v1/client/` - Create client
- PUT  `/api/v1/client/` - Update client
- DELETE `/api/v1/client/{id}` - Delete client

Similar endpoints for: Employee, Fee, Kardex, Lend, Tool services

## Database Schema

- Each service can have its own database (polyglot persistence pattern)
- Currently, all services use the same database for simplicity
- Future: Implement separate databases per service
- Hibernate auto-creates/updates schemas based on entities

## Benefits of This Architecture

1. **Scalability**: Each service can be scaled independently
2. **Deployment**: Services can be deployed independently
3. **Technology**: Different services can use different technologies
4. **Resilience**: Failure in one service doesn't bring down others
5. **Development**: Teams can work on services independently
6. **Monitoring**: Each service can be monitored separately

## Future Enhancements

1. **API Documentation**: Swagger/OpenAPI on each service (ready to use)
2. **Load Balancing**: Multiple instances of each service
3. **Circuit Breaker**: Hystrix/Resilience4j for fault tolerance
4. **Service Mesh**: Istio or Linkerd for advanced traffic management
5. **Event-Driven**: Message queues for async communication (RabbitMQ/Kafka)
6. **Authentication**: OAuth2/JWT for inter-service authentication
7. **Observability**: Distributed tracing (Sleuth/Zipkin), logging (ELK stack)
8. **Caching**: Redis for distributed caching

## Backward Compatibility

All original API endpoints are preserved through the API Gateway routing:
- `/api/v1/client/**` → client-service
- `/api/v1/employee/**` → employee-service
- `/api/v1/fee/**` → fee-service
- `/api/v1/kardex/**` → kardex-service
- `/api/v1/lend/**` → lend-service
- `/api/v1/tool/**` → tool-service

Existing clients can continue using the same endpoints; they're now routed through the gateway.

## Troubleshooting

### Services not starting
- Ensure PostgreSQL is running
- Check environment variables are set
- Check port availability
- Review logs for detailed errors

### Eureka showing no services
- Wait 30 seconds for services to register
- Check service logs for connection errors
- Verify Eureka server is running on port 8761

### API Gateway returning 503
- Ensure all microservices are registered with Eureka
- Check that services are running on their assigned ports
- Verify network connectivity between services

## Monitoring

### Eureka Dashboard
```
http://localhost:8761
```
Shows all registered services and their health status

### Individual Service Health
Each service exposes health endpoints:
```
http://localhost:8081/actuator/health
http://localhost:8082/actuator/health
... (for each service port)
```

## Next Steps

1. Test all services are running correctly
2. Verify API Gateway is routing requests properly
3. Monitor Eureka dashboard for service registration
4. Scale services as needed
5. Implement inter-service communication patterns
6. Add advanced monitoring and observability
7. Implement API security
8. Set up CI/CD pipeline for microservices

## Support

For issues or questions, refer to:
- MICROSERVICES_README.md - Detailed operational guide
- Individual service logs
- Spring Cloud documentation: https://spring.io/cloud
- Eureka documentation: https://github.com/Netflix/eureka
