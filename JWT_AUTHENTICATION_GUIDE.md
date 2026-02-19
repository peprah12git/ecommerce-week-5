# JWT Authentication Implementation Guide

## Overview
Complete JWT-based authentication for Spring Boot 3 GraphQL API with automatic token attachment in Axios.

## Backend Components

### 1. JwtAuthenticationFilter
**Location:** `security/JwtAuthenticationFilter.java`

Validates JWT tokens and sets Spring Security context:
- Extracts token from `Authorization: Bearer <token>` header
- Validates token using JwtUtil
- Sets authentication in SecurityContext with user role
- Logs successful/failed authentication attempts
- Returns 401 for invalid tokens

### 2. SecurityConfig
**Location:** `config/SecurityConfig.java`

Configures Spring Security:
- **Public endpoints:** `/api/users/register`, `/api/users/login`, `/api/auth/**`, Swagger, GraphiQL
- **Protected endpoints:** `/graphql` and all other API endpoints
- Stateless session management (no cookies)
- JWT filter runs before Spring Security's default authentication

### 3. GraphQLAuthInterceptor
**Location:** `security/GraphQLAuthInterceptor.java`

Intercepts GraphQL requests:
- Allows public queries: `categories`, `category`
- Logs all GraphQL requests with authentication status
- Can be extended to add more public queries

## Frontend Components

### 1. apiService.js
**Location:** `frontend/src/services/apiService.js`

Enhanced Axios setup:
- **Two instances:** REST API and GraphQL API
- **Request interceptor:** Automatically attaches JWT from localStorage
- **Response interceptor:** Handles 401 errors, redirects to login
- **graphqlQuery helper:** Simplifies GraphQL requests with error handling

### 2. graphqlExamples.js
**Location:** `frontend/src/services/graphqlExamples.js`

Example GraphQL queries:
- `getCategories()` - Public query (no auth)
- `getProducts()` - Protected query (requires token)
- `createProduct()` - Protected mutation (requires ADMIN role)

## Setup Instructions

### 1. Add Spring Security Dependency
Already added to `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### 2. Rebuild Backend
```bash
cd demo
mvnw clean install
mvnw spring-boot:run
```

### 3. Update Frontend API Calls
Replace old `api.js` imports with new `apiService.js`:

```javascript
// Old
import api from './services/api';

// New
import api, { graphqlQuery } from './services/apiService';
```

### 4. Test Authentication

#### Login and Store Token
```javascript
const response = await api.post('/users/login', { email, password });
localStorage.setItem('token', response.data.token);
```

#### Make Protected GraphQL Request
```javascript
import { getProducts } from './services/graphqlExamples';

const data = await getProducts(0, 10);
```

## Configuration Options

### Add More Public Queries
Edit `GraphQLAuthInterceptor.java`:
```java
private static final Set<String> PUBLIC_QUERIES = Set.of(
    "categories", 
    "category",
    "products"  // Add this
);
```

### Change Token Expiration
Edit `JwtUtil.java`:
```java
private final long expiration = 86400000; // 24 hours (in milliseconds)
```

### Customize Unauthorized Response
Edit `JwtAuthenticationFilter.java`:
```java
response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
response.setContentType("application/json");
response.getWriter().write("{\"error\":\"Custom error message\"}");
```

## Testing

### Test Public Endpoint (No Token)
```bash
curl -X POST http://localhost:8080/graphql \
  -H "Content-Type: application/json" \
  -d '{"query":"{ categories { categoryId name } }"}'
```

### Test Protected Endpoint (With Token)
```bash
curl -X POST http://localhost:8080/graphql \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"query":"{ products { content { productId name } } }"}'
```

### Test Invalid Token
```bash
curl -X POST http://localhost:8080/graphql \
  -H "Authorization: Bearer invalid_token" \
  -d '{"query":"{ products { content { name } } }"}'
# Expected: 401 Unauthorized
```

## Logging

All authentication events are logged:
- **Success:** `JWT authenticated user: {userId} with role: {role}`
- **Failure:** `JWT validation failed: {error}`
- **GraphQL:** `GraphQL request authorized: {user}` or `Unauthorized GraphQL request`

View logs in console or configure logging in `application.yml`:
```yaml
logging:
  level:
    com.smartcommerce.security: DEBUG
```

## Troubleshooting

### 401 Errors
1. Check token exists: `localStorage.getItem('token')`
2. Verify token format: Should start with `Bearer `
3. Check token expiration (24 hours default)
4. Ensure endpoint is not in public list

### Token Not Attached
1. Verify `apiService.js` is imported correctly
2. Check browser console for "Token attached" log
3. Ensure token is stored: `localStorage.setItem('token', token)`

### CORS Issues
Update `CorsConfig.java` to allow Authorization header:
```java
.allowedHeaders("*")
.exposedHeaders("Authorization")
```

## Security Best Practices

✅ **Implemented:**
- Stateless JWT authentication
- BCrypt password hashing
- Role-based access control
- Token expiration (24 hours)
- Automatic logout on 401

⚠️ **Recommended Additions:**
- Refresh token mechanism
- Token blacklist for logout
- HTTPS in production
- Rate limiting
- CSRF protection for state-changing operations
