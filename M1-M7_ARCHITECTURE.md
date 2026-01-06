# M1-M7 Microservices Architecture

## Overview
The ToolRent backend has been refactored from the initial 6-service architecture into a new **7-microservice (M1-M7)** structure, with some services combined and using **DTOs (Data Transfer Objects)** for data retrieval and reporting.

## New Microservices Structure

### **M1: Tool Inventory Service** (Unchanged - Port 8086)
- **Issue**: #1
- **Responsibilities**:
  - Register new tools with basic data (name, category, initial state, replacement value)
  - Take down damaged or unused tools (Admin only)
  - Manage tool states: Available, Borrowed, Under Repair, Down
- **Database**: PostgreSQL (`tingeso` database)
- **Technology**: Spring Boot 3.4.4, Spring Data JPA, PostgreSQL Driver
- **Key Features**: Tool entity validation, state management
- **Endpoints**:
  - `POST /api/v1/tool/` - Create new tool
  - `GET /api/v1/tool/{id}` - Get tool by ID
  - `GET /api/v1/tool/all` - Get all tools
  - `PUT /api/v1/tool/{id}` - Update tool
  - `DELETE /api/v1/tool/{id}` - Delete tool
  - `PUT /api/v1/tool/{id}/takedown` - Take down tool (Admin)

---

### **M2: Lending Management Service** (Unchanged - Port 8085)
- **Issue**: #4 (with sub-issues #5-#9)
- **Responsibilities**:
  - Register lending with client and tool (with delivery and return dates)
  - Validate tool availability before authorizing lending
  - Register tool return, updating state and stock
  - Automatically calculate late fees (daily fee)
  - Block new lendings for clients with fee backlogs
  - Update Kardex for each lending/return
- **Database**: PostgreSQL (`tingeso` database)
- **Technology**: Spring Boot 3.4.4, Spring Data JPA, Feign Clients (inter-service calls)
- **Key Features**:
  - Lending state management (ACTIVE, LATE, RETURNED)
  - Fee calculation logic
  - Client validation
  - Kardex integration
- **Business Rules**:
  - Maximum 5 active lendings per client
  - Client must be in "Active" state
  - Tool must be available with stock >= 1
  - Return date cannot be before delivery date
  - One unit per tool per client at a time

---

### **M3: Client Management Service** (Unchanged - Port 8081)
- **Issue**: #10 (with sub-issues #11-#12)
- **Responsibilities**:
  - Register client info (name, contact, DNI, state)
  - Manage client states: Active, Restricted
  - Change client state to "Restricted" for delays
- **Database**: PostgreSQL (`tingeso` database)
- **Technology**: Spring Boot 3.4.4, Spring Data JPA
- **Key Features**:
  - Client validation (mandatory: name, DNI, phone, state)
  - State transition management
- **Endpoints**:
  - `POST /api/v1/client/` - Register new client
  - `GET /api/v1/client/{id}` - Get client by ID
  - `PUT /api/v1/client/{id}/state` - Update client state

---

### **M4: Fee Management Service** (Unchanged - Port 8083)
- **Issue**: #13 (with sub-issues #14-#16)
- **Responsibilities**:
  - Set up daily lending fee (Admin only)
  - Set up daily late fee (Admin only)
  - Register replacement value for each tool (Admin only)
  - Manage fee configuration globally
- **Database**: PostgreSQL (`tingeso` database)
- **Technology**: Spring Boot 3.4.4, Spring Data JPA
- **Key Features**:
  - Role-based access control (Admin only)
  - Daily fee configuration
  - Replacement value tracking per tool
- **Business Rules**:
  - All fees are daily values
  - Only Admins can modify values
  - Replacement fee charged only for "Down" tools

---

### **M5: Kardex Service** (Unchanged - Port 8084)
- **Issue**: #17 (with sub-issues #18-#20)
- **Responsibilities**:
  - Register automatically each movement in kardex (registrations, tools)
  - Check movement history of each tool
  - Generate list of movements by date range
  - Track inventory changes (Ingress, Lend, Return, Down, Repair)
- **Database**: PostgreSQL (`tingeso` database)
- **Technology**: Spring Boot 3.4.4, Spring Data JPA
- **Key Features**:
  - Auto movement registration via events
  - Tool history tracking
  - Date range queries
  - Movement types: INGRESS, LEND, RETURN, DOWN, REPAIR
- **Endpoints**:
  - `GET /api/v1/kardex/tool/{toolId}` - Get history for tool
  - `GET /api/v1/kardex/dateRange` - Get movements by date range
  - `GET /api/v1/kardex/` - Get all movements

---

### **M6: Reports Service** (NEW - Port 8087)
- **Issue**: #21 (with sub-issues #22-#24)
- **Responsibilities**:
  - List active lendings and their state (current, late)
  - List clients with delays
  - Generate report of most lended tools (Ranking)
  - All reports can be filtered by date range
- **Architecture**: **Aggregation Service** (NO database)
- **Technology**: Spring Boot 3.4.4, Spring Cloud OpenFeign, DTOs
- **Key Features**:
  - **Uses DTOs for data retrieval** (LendingDTO, ClientDelayDTO, ToolRankingDTO)
  - Calls other microservices via Feign clients
  - Real-time report generation
  - Date range filtering
- **Feign Clients**:
  - `LendServiceClient` - Get lending data
  - `ClientServiceClient` - Get client information
  - `ToolServiceClient` - Get tool information
- **Endpoints**:
  - `GET /api/v1/reports/active-lendings?startDate=X&endDate=Y` - Active lendings (#22)
  - `GET /api/v1/reports/clients-with-delays?startDate=X&endDate=Y` - Clients with delays (#23)
  - `GET /api/v1/reports/most-lended-tools?startDate=X&endDate=Y` - Tool ranking (#24)
- **DTOs**:
  - `LendingDTO`: id, clientId, toolId, clientName, toolName, deliveryDay, returnDay, state, daysLate
  - `ClientDelayDTO`: clientId, clientName, clientDNI, delayedLendings, totalDaysLate, clientState
  - `ToolRankingDTO`: toolId, toolName, toolCategory, totalLendings, rank

---

### **M7: User Management Service** (NEW - Port 8088)
- **Issue**: #25 (with sub-issues #26-#29)
- **Responsibilities**:
  - Register users into the system with access credentials (#26)
  - Assign user roles (Admin, Employee) (#27)
  - Validate permissions according to role (#28)
  - Authentication by login and session control (#29)
  - Merge employee functionality with user/auth management
- **Database**: PostgreSQL (`tingeso` database, table: `users`)
- **Technology**: Spring Boot 3.4.4, Spring Data JPA, Spring Security ready
- **Key Features**:
  - **Combines** original EmployeeEntity with new UserEntity
  - User registration and validation
  - Role-based access control (ADMIN, EMPLOYEE)
  - Session tracking (lastLogin timestamp)
  - User activation/deactivation
- **User Roles**:
  - **ADMIN**: Total system access, can manage everything
  - **EMPLOYEE**: Limited access, can only perform lendings/returns and view reports
- **Endpoints**:
  - `POST /api/v1/user/register` - Register new user (#26)
  - `POST /api/v1/user/login` - Authenticate user (#29)
  - `PUT /api/v1/user/{userId}/role` - Assign role (#27)
  - `GET /api/v1/user/{userId}/validate-permission` - Check permissions (#28)
  - `GET /api/v1/user/{id}` - Get user details
  - `GET /api/v1/user/active` - Get active users
  - `GET /api/v1/user/by-role` - Get users by role
  - `PUT /api/v1/user/{id}` - Update user
  - `DELETE /api/v1/user/{id}/deactivate` - Deactivate user
- **Entity Fields**:
  - Employee: `name`, `mail`, `rut`
  - Auth: `username`, `password` (hash in production)
  - Role: `role` (ADMIN | EMPLOYEE)
  - Session: `lastLogin`, `isActive`

---

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                     API Gateway (8080)                           │
├─────────────────────────────────────────────────────────────────┤
│  Routes to: M1(8086), M2(8085), M3(8081), M4(8083), M5(8084)   │
│             M6(8087), M7(8088)                                   │
└────────────┬─────────────────────────────────────────┬──────────┘
             │                                         │
    ┌────────▼─────────┐                       ┌──────▼──────────┐
    │  Infrastructure  │                       │  Microservices  │
    ├──────────────────┤                       ├─────────────────┤
    │ Eureka (8761)    │                       │ M1: Tool (8086) │
    │ Config (8888)    │                       │ M2: Lend (8085) │
    │ PostgreSQL       │                       │ M3: Client(8081)│
    │                  │                       │ M4: Fee (8083)  │
    └──────────────────┘                       │ M5: Kardex(8084)│
                                              │ M6: Reports(8087)◄──DTOs
                                              │ M7: User (8088) │
                                              └─────────────────┘
```

---

## Key Changes from Previous Architecture

| Aspect | Old | New |
|--------|-----|-----|
| **Total Services** | 9 (6 business + 3 infra) | 11 (7 business + 3 infra + 1 removed) |
| **New Services** | - | M6 (Reports), M7 (User/Auth) |
| **Removed Services** | - | employee-service (merged into M7) |
| **Database** | Shared (all in `tingeso`) | Still shared, but better separated by tables |
| **Reporting** | None | M6 with DTOs for data aggregation |
| **Authentication** | None | M7 with login, roles, permissions |
| **Employee Service** | Separate | Merged into M7 as UserEntity |

---

## Data Transfer Objects (DTOs)

### Why DTOs in M6 (Reports)?
- **Aggregation Service**: M6 doesn't own data, it retrieves and combines data from other services
- **Efficient Data Transfer**: Only sends required fields via DTOs
- **Loose Coupling**: Reports service doesn't depend on actual entity classes
- **API Contract**: DTOs define clear interfaces for inter-service communication

### DTO Usage Pattern
```java
// M6 Reports Service calls other services via Feign
@FeignClient(name = "lend-service")
public interface LendServiceClient {
    @GetMapping("/api/v1/lend/active")
    List<LendingDTO> getActiveLendings();
}

// Service then transforms and enriches the data
public List<LendingDTO> getActiveLendingsReport(...) {
    List<LendingDTO> lendings = lendServiceClient.getActiveLendings();
    // Enrich with additional data from other services
    // Filter, aggregate, calculate
    return enrichedLendings;
}
```

---

## Deployment Ports

| Service | Port | Type |
|---------|------|------|
| API Gateway | 8080 | Infrastructure |
| Eureka Server | 8761 | Infrastructure |
| Config Server | 8888 | Infrastructure |
| Client Service (M3) | 8081 | Business |
| Fee Service (M4) | 8083 | Business |
| Kardex Service (M5) | 8084 | Business |
| Lend Service (M2) | 8085 | Business |
| Tool Service (M1) | 8086 | Business |
| Reports Service (M6) | 8087 | Business |
| User Service (M7) | 8088 | Business |
| PostgreSQL | 5432 | Database |

---

## Future Enhancements

1. **Add Spring Security** to M7 for JWT tokens
2. **Implement message queues** for Kardex auto-registration events
3. **Add caching** in M6 reports for performance
4. **Polyglot persistence**: Each service could have its own database
5. **API versioning**: Add v2 endpoints with extended DTOs
6. **Distributed tracing**: Implement Sleuth + Zipkin
7. **Unit tests**: Add JaCoCo coverage for all services
8. **Kubernetes deployment**: Create Helm charts for K8s

---

## GitHub Issues Mapping

| Issue | Service | Functionality |
|-------|---------|---------------|
| #1-#3 | M1 | Tool inventory, take down |
| #4-#9 | M2 | Lending, returns, late fees |
| #10-#12 | M3 | Client registration, state management |
| #13-#16 | M4 | Fee configuration |
| #17-#20 | M5 | Kardex, movement tracking |
| #21-#24 | M6 | Reports, queries |
| #25-#29 | M7 | User management, authentication |

---

## Configuration Files

All microservices are configured with:
- **Eureka Discovery**: Auto-registration and service lookup
- **Config Server**: Centralized configuration
- **Database**: PostgreSQL with JPA/Hibernate auto DDL-update
- **Feign Clients**: For inter-service communication (especially M6)

Configuration can be overridden via Config Server YAML files located in:
`config-server/src/main/resources/config-files/`

---

## Next Steps

1. ✅ Create M6 (Reports Service) with DTOs
2. ✅ Create M7 (User Service) with auth/roles
3. ⏳ Remove or deprecate employee-service
4. ⏳ Update all service clients to use new M7 for user validation
5. ⏳ Implement Feign clients in M2 for M3 (Client) validation
6. ⏳ Add message queue integration for Kardex events
7. ⏳ Implement comprehensive unit tests

---

Generated: January 6, 2026
Status: M6 and M7 created and integrated with Eureka, Config Server, and API Gateway
