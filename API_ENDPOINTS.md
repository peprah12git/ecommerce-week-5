# SmartCommerce API Endpoints

## Public Endpoints (No JWT Required)

### Authentication
- `POST /api/users/register` - Register new user
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - Register via auth endpoint
- `POST /api/admin/login` - Admin login

### Documentation
- `GET /swagger-ui/**` - Swagger UI
- `GET /v3/api-docs/**` - OpenAPI docs
- `GET /graphiql` - GraphQL playground

### GraphQL
- `POST /graphql` - GraphQL endpoint (public queries: categories, category)

## Protected Endpoints (JWT Required)

### Users
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Products
- `GET /api/products` - Get all products (with pagination/filters)
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create product (Admin only)
- `PUT /api/products/{id}` - Update product (Admin only)
- `DELETE /api/products/{id}` - Delete product (Admin only)

### Categories
- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create category (Admin only)
- `PUT /api/categories/{id}` - Update category (Admin only)
- `DELETE /api/categories/{id}` - Delete category (Admin only)

### Cart
- `GET /api/cart/{userId}` - Get user's cart
- `POST /api/cart` - Add item to cart
- `PUT /api/cart/{cartItemId}` - Update cart item quantity
- `DELETE /api/cart/{cartItemId}` - Remove item from cart
- `DELETE /api/cart/user/{userId}` - Clear cart

### Orders
- `POST /api/orders` - Create order
- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `GET /api/orders/user/{userId}` - Get user's orders
- `PATCH /api/orders/{id}/status` - Update order status
- `POST /api/orders/{id}/cancel` - Cancel order

### Inventory
- `GET /api/inventory` - Get all inventory
- `GET /api/inventory/{productId}` - Get inventory for product
- `PUT /api/inventory/{productId}` - Update inventory

### Reviews
- `GET /api/reviews/product/{productId}` - Get product reviews
- `POST /api/reviews` - Create review
- `PUT /api/reviews/{id}` - Update review
- `DELETE /api/reviews/{id}` - Delete review

## GraphQL Queries

### Public Queries
```graphql
query {
  categories {
    categoryId
    name
    description
  }
}

query {
  category(id: 1) {
    categoryId
    name
    description
  }
}
```

### Protected Queries (Requires JWT)
```graphql
query {
  products(page: 0, size: 10) {
    content {
      productId
      name
      price
      description
    }
    totalElements
    totalPages
  }
}

query {
  product(id: 1) {
    productId
    name
    price
    description
  }
}

query {
  searchProducts(searchTerm: "laptop") {
    productId
    name
    price
  }
}
```

### Protected Mutations (Requires JWT)
```graphql
mutation {
  createProduct(
    productName: "New Product"
    description: "Description"
    price: 99.99
    categoryId: 1
    quantityAvailable: 100
  ) {
    productId
    name
    price
  }
}

mutation {
  updateProduct(
    id: 1
    productName: "Updated Name"
    price: 89.99
  ) {
    productId
    name
    price
  }
}

mutation {
  deleteProduct(id: 1)
}
```
