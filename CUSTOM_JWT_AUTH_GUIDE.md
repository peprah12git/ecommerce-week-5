# Custom JWT Authentication (Without Spring Security)

## Implementation Overview

This solution provides JWT authentication for Spring Boot 3 GraphQL API **without using Spring Security**.

## Backend Components

### 1. JwtAuthFilter.java
**Location:** `filter/JwtAuthFilter.java`

Custom servlet filter that:
- Intercepts all `/api/*` and `/graphql` requests
- Allows public paths: `/api/users/register`, `/api/users/login`, Swagger, GraphiQL
- Allows public GraphQL queries: `categories`, `category`
- Validates JWT token from `Authorization: Bearer <token>` header
- Returns 401 for missing/invalid tokens
- Logs all authentication attempts

### 2. FilterConfig.java
**Location:** `config/FilterConfig.java`

Registers the JWT filter with Spring Boot:
- Applies filter to `/api/*` and `/graphql` endpoints
- Sets filter order to 1 (runs first)

### 3. GraphQLAuthInterceptor.java
**Location:** `interceptor/GraphQLAuthInterceptor.java`

GraphQL-specific interceptor for logging:
- Logs public vs protected query execution
- Integrates with Spring GraphQL

## Frontend Components

### 1. api.js (Updated)
**Location:** `frontend/src/services/api.js`

Enhanced Axios instance with:
- **Request Interceptor:** Automatically attaches JWT from localStorage
- **Response Interceptor:** Handles 401 errors, redirects to login
- **graphqlQuery helper:** Simplifies GraphQL requests
- **Console logging:** Visual feedback for debugging

### 2. graphqlExamples.js
**Location:** `frontend/src/services/graphqlExamples.js`

Example queries:
- `getCategories()` - Public query
- `getProducts()` - Protected query
- `createProduct()` - Protected mutation

## Setup Instructions

### 1. Build Backend
```bash
cd demo
mvnw clean install
mvnw spring-boot:run
```

### 2. Test Authentication

#### Login and Store Token
```javascript
import api from './services/api';

const login = async (email, password) => {
  const response = await api.post('/api/users/login', { email, password });
  localStorage.setItem('token', response.data.token);
  console.log('Token stored');
};
```

#### Make Public GraphQL Request (No Token)
```javascript
import { getCategories } from './services/graphqlExamples';

const categories = await getCategories();
console.log(categories);
```

#### Make Protected GraphQL Request (With Token)
```javascript
import { getProducts } from './services/graphqlExamples';

// Token automatically attached by interceptor
const products = await getProducts(0, 10);
console.log(products);
```

## Configuration

### Add More Public Queries
Edit `JwtAuthFilter.java`:
```java
private static final Set<String> PUBLIC_QUERIES = Set.of(
    "categories", 
    "category",
    "products"  // Add new public query
);
```

### Add More Public Paths
Edit `JwtAuthFilter.java`:
```java
private static final Set<String> PUBLIC_PATHS = Set.of(
    "/api/users/register",
    "/api/users/login",
    "/api/public",  // Add new public path
    "/swagger-ui",
    "/v3/api-docs",
    "/graphiql"
);
```

## Testing

### Test Public Endpoint
```bash
curl -X POST http://localhost:8080/graphql \
  -H "Content-Type: application/json" \
  -d '{"query":"{ categories { categoryId name } }"}'
```

### Test Protected Endpoint
```bash
# First login to get token
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@test.com","password":"password123"}'

# Use token in GraphQL request
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

# Expected: {"error":"Invalid or expired token"}
```

## Console Logging

### Backend Logs
- `✓ Authenticated user: {userId} role: {role}` - Successful auth
- `✗ JWT validation failed: {error}` - Failed auth
- `⚠ Missing Authorization header: {path}` - No token provided
- `✓ Public GraphQL query allowed` - Public query executed

### Frontend Logs
- `✓ Token attached to request` - Token found and attached
- `⚠ No token in localStorage` - No token available
- `✓ Request successful` - Request completed
- `✓ GraphQL query successful` - GraphQL query completed
- `✗ Unauthorized - redirecting to login` - 401 error
- `✗ GraphQL errors: {errors}` - GraphQL errors

## React Component Example

```javascript
import React, { useEffect, useState } from 'react';
import { getCategories, getProducts } from './services/graphqlExamples';

function ProductsPage() {
  const [categories, setCategories] = useState([]);
  const [products, setProducts] = useState([]);

  useEffect(() => {
    // Public query - works without token
    getCategories()
      .then(data => setCategories(data.categories))
      .catch(err => console.error(err));

    // Protected query - requires token
    getProducts(0, 10)
      .then(data => setProducts(data.products.content))
      .catch(err => console.error(err));
  }, []);

  return (
    <div>
      <h2>Categories (Public)</h2>
      {categories.map(cat => <div key={cat.categoryId}>{cat.name}</div>)}
      
      <h2>Products (Protected)</h2>
      {products.map(prod => <div key={prod.productId}>{prod.name}</div>)}
    </div>
  );
}
```

## Troubleshooting

### 401 Errors
1. Check token exists: `localStorage.getItem('token')`
2. Verify token format in Network tab: `Authorization: Bearer <token>`
3. Check token expiration (24 hours default)
4. Ensure endpoint is not in public list

### Token Not Attached
1. Check console for "⚠ No token in localStorage"
2. Verify login stores token: `localStorage.setItem('token', token)`
3. Check api.js is imported correctly

### GraphQL Query Blocked
1. Add query to PUBLIC_QUERIES in JwtAuthFilter.java
2. Verify query name matches exactly
3. Check filter logs for authentication status

## Key Differences from Spring Security

✅ **Advantages:**
- Simpler setup, no Spring Security configuration
- Direct control over authentication logic
- Easier to understand and debug
- No dependency on Spring Security

⚠️ **Limitations:**
- No built-in CSRF protection
- No session management features
- Manual role-based access control
- Less enterprise-grade security features

## Security Best Practices

✅ **Implemented:**
- JWT token validation
- BCrypt password hashing
- Token expiration (24 hours)
- Automatic logout on 401
- Request/response logging

⚠️ **Recommended:**
- Use HTTPS in production
- Implement refresh tokens
- Add rate limiting
- Token blacklist for logout
- Input validation and sanitization
