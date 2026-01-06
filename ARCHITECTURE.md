# ToolRent Microservices Architecture Diagram

## System Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          CLIENT APPLICATIONS                                  │
│                     (Web, Mobile, Desktop)                                    │
└────────────────────────────────────┬────────────────────────────────────────┘
                                     │
                                     ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                           API GATEWAY                                          │
│                         (Port 8080)                                            │
│  ┌─────────────────────────────────────────────────────────────────────┐    │
│  │  Routes:                                                              │    │
│  │  /api/v1/client/** → client-service:8081                           │    │
│  │  /api/v1/employee/** → employee-service:8082                       │    │
│  │  /api/v1/fee/** → fee-service:8083                                 │    │
│  │  /api/v1/kardex/** → kardex-service:8084                           │    │
│  │  /api/v1/lend/** → lend-service:8085                               │    │
│  │  /api/v1/tool/** → tool-service:8086                               │    │
│  └─────────────────────────────────────────────────────────────────────┘    │
└─┬──────────────────┬──────────────────┬───────────────┬─────────────────────┘
  │                  │                  │               │
  ▼                  ▼                  ▼               ▼
┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│   Client     │ │   Employee   │ │    Fee       │ │   Kardex     │
│   Service    │ │   Service    │ │   Service    │ │   Service    │
│   (8081)     │ │   (8082)     │ │   (8083)     │ │   (8084)     │
│              │ │              │ │              │ │              │
│ ┌──────────┐ │ │ ┌──────────┐ │ │ ┌──────────┐ │ │ ┌──────────┐ │
│ │ Entities │ │ │ │ Entities │ │ │ │ Entities │ │ │ │ Entities │ │
│ │ Services │ │ │ │ Services │ │ │ │ Services │ │ │ │ Services │ │
│ │Repository│ │ │ │Repository│ │ │ │Repository│ │ │ │Repository│ │
│ │Controller│ │ │ │Controller│ │ │ │Controller│ │ │ │Controller│ │
│ └──────────┘ │ │ └──────────┘ │ │ └──────────┘ │ │ └──────────┘ │
└──────────────┘ └──────────────┘ └──────────────┘ └──────────────┘
  │                  │                  │               │
  └──────────────────┴──────────────────┴───────────────┘
                     │
              ┌──────▼──────┐
              │  PostgreSQL  │
              │  Database    │
              │              │
              │  Tables:     │
              │  - client    │
              │  - employee  │
              │  - fee       │
              │  - kardex    │
              │  - lend      │
              │  - tool      │
              └──────────────┘
                     │
    ┌────────────────┴────────────────┐
    ▼                                  ▼
┌──────────────┐              ┌──────────────┐
│   Lend       │              │   Tool       │
│   Service    │              │   Service    │
│   (8085)     │              │   (8086)     │
│              │              │              │
│ ┌──────────┐ │              │ ┌──────────┐ │
│ │ Entities │ │              │ │ Entities │ │
│ │ Services │ │              │ │ Services │ │
│ │Repository│ │              │ │Repository│ │
│ │Controller│ │              │ │Controller│ │
│ └──────────┘ │              │ └──────────┘ │
└──────────────┘              └──────────────┘
```

## Service Discovery & Configuration

```
┌─────────────────────────────────────────────────────────────────────────┐
│                     SERVICE DISCOVERY LAYER                               │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                            │
│  ┌──────────────────────┐         ┌──────────────────────┐              │
│  │  Eureka Server       │         │  Config Server       │              │
│  │  (Port 8761)         │         │  (Port 8888)         │              │
│  │                      │         │                      │              │
│  │ Service Registry:    │         │ Centralized Config:  │              │
│  │ - client-service     │         │ - client-service.y   │              │
│  │ - employee-service   │         │ - employee-service.y │              │
│  │ - fee-service        │         │ - fee-service.yaml   │              │
│  │ - kardex-service     │         │ - kardex-service.yaml│              │
│  │ - lend-service       │         │ - lend-service.yaml  │              │
│  │ - tool-service       │         │ - tool-service.yaml  │              │
│  │ - api-gateway        │         │                      │              │
│  │                      │         │ Load: On startup     │              │
│  │ Dashboard:           │         │ Update: Dynamic      │              │
│  │ localhost:8761       │         │                      │              │
│  └──────────────────────┘         └──────────────────────┘              │
│          ▲                                   ▲                            │
│          └───────────────┬───────────────────┘                            │
│                          │                                                │
│        All services register and poll config                             │
│                          │                                                │
└──────────────────────────┼────────────────────────────────────────────────┘
                           │
              ┌────────────┴─────────────┐
              │                          │
         Service Auto-Registration   Config Auto-Load
         on Startup                  on Startup
```

## Data Flow Example: Client Service Request

```
1. Client Application
        │
        │ HTTP Request: GET /api/v1/client/1
        ▼
2. API Gateway (8080)
        │
        │ Checks routes
        │ Route: /api/v1/client/** → client-service:8081
        ▼
3. Service Registry (Eureka)
        │
        │ Lookup: Get client-service instance
        │ Return: 127.0.0.1:8081
        ▼
4. Client Service (8081)
        │
        ├─→ Request received by ClientController
        │
        ├─→ ClientService.getById(1)
        │
        ├─→ ClientRepository.findById(1)
        │
        ├─→ Query PostgreSQL Database
        │
        ├─→ Return ClientEntity
        │
        └─→ Convert to JSON response
        
5. Response back to Client Application
        │
        ▼
   {"id": 1, "name": "John", "email": "john@example.com", ...}
```

## Service Dependencies

```
API Gateway
    ├── depends on → Eureka (discovery)
    ├── depends on → Config Server (configuration)
    ├── routes to → Client Service
    ├── routes to → Employee Service
    ├── routes to → Fee Service
    ├── routes to → Kardex Service
    ├── routes to → Lend Service
    └── routes to → Tool Service

All Microservices
    ├── depend on → Eureka (self-registration)
    ├── depend on → Config Server (dynamic config)
    └── depend on → PostgreSQL (data persistence)

PostgreSQL Database
    └── shared by all microservices (for now)
```

## Deployment Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Docker Compose Network                    │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │          Development/Testing Environment              │  │
│  │                                                        │  │
│  │  ┌─────────────────────────────────────────────────┐ │  │
│  │  │        Service Container Network                 │ │  │
│  │  │                                                   │ │  │
│  │  │  [Eureka] [Config] [Gateway]                    │ │  │
│  │  │  [Client] [Employee] [Fee]                      │ │  │
│  │  │  [Kardex] [Lend] [Tool]                         │ │  │
│  │  │                                                   │ │  │
│  │  │  All connected via: toolrent-network            │ │  │
│  │  └─────────────────────────────────────────────────┘ │  │
│  │                        ▲                              │  │
│  │                        │                              │  │
│  │                    Shared Volume                       │  │
│  │              (postgres_data)                           │  │
│  │                        │                              │  │
│  │  ┌─────────────────────▼─────────────────────────┐  │  │
│  │  │         PostgreSQL Database                    │  │  │
│  │  │         (Port 5432 exposed)                   │  │  │
│  │  └───────────────────────────────────────────────┘  │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                              │
│  Port Mappings:                                             │
│  8080 → API Gateway                                        │
│  8761 → Eureka                                             │
│  8888 → Config Server                                      │
│  8081 → Client Service                                     │
│  8082 → Employee Service                                   │
│  8083 → Fee Service                                        │
│  8084 → Kardex Service                                     │
│  8085 → Lend Service                                       │
│  8086 → Tool Service                                       │
│  5432 → PostgreSQL                                         │
└─────────────────────────────────────────────────────────────┘
```

## Development Workflow

```
Developer
    │
    ├─→ Clone Repository
    │
    ├─→ Set Environment Variables
    │   - SPRING_DATASOURCE_URL
    │   - SPRING_DATASOURCE_USERNAME
    │   - SPRING_DATASOURCE_PASSWORD
    │
    ├─→ mvn clean install -f parent-pom.xml
    │
    ├─→ Start Services (in order):
    │   1. eureka-server
    │   2. config-server
    │   3. All microservices (parallel)
    │   4. api-gateway
    │
    ├─→ Verify Services
    │   - Check Eureka: http://localhost:8761
    │   - Test API: curl http://localhost:8080/api/v1/client/
    │
    └─→ Develop and Test
        - Edit service code
        - Restart affected service
        - Test via API Gateway
        - Verify service registration
```

## Scaling Architecture

```
Current: Single Instance Per Service
┌──────────────────────────────────────┐
│  API Gateway (8080)                   │
│  routes to                            │
├──────────────────────────────────────┤
│  Client Service Instance 1 (8081)    │
│  Employee Service Instance 1 (8082)  │
│  Fee Service Instance 1 (8083)       │
│  ... etc                              │
└──────────────────────────────────────┘

Scaled: Multiple Instances Per Service (load balanced)
┌──────────────────────────────────────────────┐
│  API Gateway (8080) - Load Balancer            │
│  routes to (Round Robin)                      │
├──────────────────────────────────────────────┤
│  Client Service:                              │
│    ├─ Instance 1 (8081)                      │
│    ├─ Instance 2 (8081-1)                    │
│    └─ Instance 3 (8081-2)                    │
│                                               │
│  Employee Service:                            │
│    ├─ Instance 1 (8082)                      │
│    └─ Instance 2 (8082-1)                    │
│  ... (same for other services)                │
└──────────────────────────────────────────────┘

All instances register with Eureka
Eureka maintains service registry
API Gateway uses service name for routing
```

---

## Key Points

✅ **Independent Services**: Each microservice runs independently
✅ **Service Discovery**: Eureka automatically discovers and registers services
✅ **Centralized Config**: Config Server provides dynamic configuration
✅ **Single Entry Point**: API Gateway provides unified API access
✅ **Load Balancing**: Built-in load balancing via Eureka
✅ **Scalability**: Easy to scale individual services
✅ **Database Flexibility**: Ready for polyglot persistence
✅ **Docker Ready**: Includes Docker Compose for easy deployment
✅ **Backward Compatible**: Original API endpoints preserved

---

For detailed operations, see: **MICROSERVICES_README.md**
For quick start, see: **QUICK_START.md**
