# ToolRent Microservices Architecture

This is a refactored version of the ToolRent backend application, now using a microservices architecture with Spring Cloud.

## Architecture Overview

The application is now divided into the following components:

### Core Services
- **Client Service** (Port 8081): Manages client information
- **Employee Service** (Port 8082): Manages employee data
- **Fee Service** (Port 8083): Manages rental fees
- **Kardex Service** (Port 8084): Manages tool movement history
- **Lend Service** (Port 8085): Manages tool lending transactions
- **Tool Service** (Port 8086): Manages tool inventory

### Infrastructure Services
- **Eureka Server** (Port 8761): Service discovery and registration
- **Config Server** (Port 8888): Centralized configuration management
- **API Gateway** (Port 8080): Single entry point for all client requests

## Prerequisites

- Java 17+
- Maven 3.6+
- PostgreSQL 12+ (or H2 for development)
- Docker & Docker Compose (optional, for containerized deployment)

## Building the Project

### Build all modules
```bash
mvn clean install -f parent-pom.xml
```

### Build individual services
```bash
cd eureka-server && mvn clean install
cd ../config-server && mvn clean install
cd ../api-gateway && mvn clean install
cd ../client-service && mvn clean install
# ... repeat for other services
```

## Running the Services

### Start in order:

1. **Eureka Server**
```bash
cd eureka-server
mvn spring-boot:run
```
Access at: http://localhost:8761

2. **Config Server**
```bash
cd config-server
mvn spring-boot:run
```
Access at: http://localhost:8888

3. **Start all microservices** (in separate terminals)
```bash
cd client-service && mvn spring-boot:run
cd employee-service && mvn spring-boot:run
cd fee-service && mvn spring-boot:run
cd kardex-service && mvn spring-boot:run
cd lend-service && mvn spring-boot:run
cd tool-service && mvn spring-boot:run
```

4. **API Gateway** (last)
```bash
cd api-gateway
mvn spring-boot:run
```
Access at: http://localhost:8080

## Using the API Gateway

Once all services are running, you can access them through the API Gateway:

```
http://localhost:8080/api/v1/client/
http://localhost:8080/api/v1/employee/
http://localhost:8080/api/v1/fee/
http://localhost:8080/api/v1/kardex/
http://localhost:8080/api/v1/lend/
http://localhost:8080/api/v1/tool/
```

## Configuration

### Environment Variables

Set these before running the services:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/tingeso
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=postgres
export SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

### Config Server

Configuration files are stored in `config-server/src/main/resources/config-files/`. Each microservice has its own configuration file:
- `client-service.yaml`
- `employee-service.yaml`
- `fee-service.yaml`
- `kardex-service.yaml`
- `lend-service.yaml`
- `tool-service.yaml`

## Docker Deployment

### Build Docker Images

```bash
# Build parent pom to ensure all modules are available
mvn clean install -f parent-pom.xml

# Build individual service images
cd eureka-server && docker build -t toolrent/eureka-server . && cd ..
cd config-server && docker build -t toolrent/config-server . && cd ..
cd api-gateway && docker build -t toolrent/api-gateway . && cd ..
cd client-service && docker build -t toolrent/client-service . && cd ..
cd employee-service && docker build -t toolrent/employee-service . && cd ..
cd fee-service && docker build -t toolrent/fee-service . && cd ..
cd kardex-service && docker build -t toolrent/kardex-service . && cd ..
cd lend-service && docker build -t toolrent/lend-service . && cd ..
cd tool-service && docker build -t toolrent/tool-service . && cd ..
```

### Run with Docker Compose

```bash
docker-compose -f docker-compose.yml up -d
```

## Service Discovery

All microservices automatically register with the Eureka server. You can view the registry at:

```
http://localhost:8761/eureka/apps
```

## API Documentation

Each service includes Swagger/OpenAPI documentation:

- Client Service: http://localhost:8081/swagger-ui.html
- Employee Service: http://localhost:8082/swagger-ui.html
- Fee Service: http://localhost:8083/swagger-ui.html
- Kardex Service: http://localhost:8084/swagger-ui.html
- Lend Service: http://localhost:8085/swagger-ui.html
- Tool Service: http://localhost:8086/swagger-ui.html

## Endpoints

### Client Service
- `GET /api/v1/client/` - List all clients
- `GET /api/v1/client/{id}` - Get client by ID
- `POST /api/v1/client/` - Create new client
- `PUT /api/v1/client/` - Update client
- `DELETE /api/v1/client/{id}` - Delete client

Similar endpoints exist for Employee, Fee, Kardex, Lend, and Tool services.

## Database Schema

Each microservice maintains its own database schema. The initial schema is created by Hibernate based on the entity definitions.

## Scaling

To scale a microservice:

1. Build the Docker image
2. Update docker-compose.yml to add additional instances
3. Change ports for each instance (8081, 8081-1, 8081-2, etc.)
4. Eureka will automatically load balance requests

## Troubleshooting

### Services not registering with Eureka
- Ensure Eureka Server is running first (port 8761)
- Check that services have `@EnableDiscoveryClient` annotation

### Config Server not loading configurations
- Ensure Config Server is running first (port 8888)
- Check that service configuration files exist in `config-server/src/main/resources/config-files/`

### API Gateway not routing requests
- Verify all microservices are registered with Eureka
- Check gateway route configuration in `api-gateway/src/main/resources/application.yaml`

## Project Structure

```
├── eureka-server/           # Service Discovery
├── config-server/           # Centralized Configuration
├── api-gateway/             # API Gateway
├── client-service/          # Client Microservice
├── employee-service/        # Employee Microservice
├── fee-service/             # Fee Microservice
├── kardex-service/          # Kardex Microservice
├── lend-service/            # Lend Microservice
├── tool-service/            # Tool Microservice
├── parent-pom.xml           # Parent POM for all modules
└── docker-compose.yml       # Docker Compose configuration
```

## Migration from Monolith

The refactoring maintains backward compatibility with the original API endpoints. All routes are preserved through the API Gateway routing configuration.

Original endpoints like `/api/v1/client/` continue to work but are now routed to the appropriate microservice through the gateway.

## Development

### Adding a new microservice

1. Create a new module folder: `mkdir new-service`
2. Create a basic pom.xml with parent reference to `parent-pom.xml`
3. Add the new module to `parent-pom.xml` modules section
4. Implement the service following the pattern of existing services
5. Add routing rule to API Gateway
6. Create configuration file in Config Server

## Testing

Run tests for all modules:
```bash
mvn test -f parent-pom.xml
```

## License

See LICENSE file in the root directory

## Authors

- Original project: USACH TINGESO-2 Team
- Microservices refactoring: (Your Name/Team)
