# SmartCommerce E-Commerce Platform

A full-stack e-commerce application built with Spring Boot and React, featuring REST APIs, GraphQL support, and a modern responsive frontend.

## 🚀 Features

### Backend (Spring Boot)
- **RESTful APIs** for all e-commerce operations
- **GraphQL** support for flexible data querying
- **User Management** with role-based access (Admin/User)
- **Product Catalog** with categories and inventory tracking
- **Shopping Cart** functionality
- **Order Management** with status tracking
- **Review System** for products
- **Security** with BCrypt password hashing
- **API Documentation** with Swagger/OpenAPI
- **AOP Logging** for performance monitoring and exception tracking
- **Custom Validation** for business rules

### Frontend (React)
- **Modern UI** with responsive design
- **User Authentication** with protected routes
- **Product Browsing** with pagination and filtering
- **Shopping Cart** management
- **Order Placement** and tracking
- **Admin Dashboard** for product and inventory management
- **Context API** for state management

## 🛠️ Tech Stack

### Backend
- **Java 21**
- **Spring Boot 4.0.2**
- **Spring MVC** - REST APIs
- **Spring GraphQL** - GraphQL support
- **Spring JDBC** - Database operations
- **MySQL** - Database
- **BCrypt** - Password encryption
- **Lombok** - Reduce boilerplate code
- **SpringDoc OpenAPI** - API documentation
- **Maven** - Build tool

### Frontend
- **React 18.2**
- **React Router 6** - Navigation
- **Axios** - HTTP client
- **Lucide React** - Icons
- **Context API** - State management

## 📋 Prerequisites

- **Java 21** or higher
- **Maven 3.6+**
- **Node.js 16+** and npm
- **MySQL 8.0+**

## 🔧 Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd week-5-Ecommerce
```

### 2. Database Setup
```bash
# Login to MySQL
mysql -u root -p

# Run the schema script
source demo/src/main/resources/sql/schema.sql
```

This will create:
- Database: `ecommerce_db`
- Tables: Users, Products, Categories, Orders, OrderItems, Inventory, Reviews, CartItems
- Sample data for testing

### 3. Backend Setup

#### Configure Database Connection
Edit `demo/src/main/resources/application-dev.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce_db
    username: <your-mysql-username>
    password: <your-mysql-password>
```

#### Run the Backend
```bash
cd demo
mvnw spring-boot:run
```

The backend will start on `http://localhost:8080`

### 4. Frontend Setup
```bash
cd frontend
npm install
npm start
```

The frontend will start on `http://localhost:3000`

## 📚 API Documentation

### Swagger UI
Access interactive API documentation at:
```
http://localhost:8080/swagger-ui.html
```

### GraphQL Playground
Access GraphQL interface at:
```
http://localhost:8080/graphiql
```

### Main API Endpoints

#### Users
- `POST /api/users/register` - Register new user
- `POST /api/users/login` - User login
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

#### Products
- `GET /api/products` - Get all products (with pagination & filtering)
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create product (Admin)
- `PUT /api/products/{id}` - Update product (Admin)
- `DELETE /api/products/{id}` - Delete product (Admin)

#### Categories
- `GET /api/categories` - Get all categories
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create category (Admin)
- `PUT /api/categories/{id}` - Update category (Admin)
- `DELETE /api/categories/{id}` - Delete category (Admin)

#### Cart
- `GET /api/cart/{userId}` - Get user's cart
- `POST /api/cart` - Add item to cart
- `PUT /api/cart/{cartItemId}` - Update cart item quantity
- `DELETE /api/cart/{cartItemId}` - Remove item from cart
- `DELETE /api/cart/user/{userId}` - Clear cart

#### Orders
- `POST /api/orders` - Create order
- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `GET /api/orders/user/{userId}` - Get user's orders
- `PATCH /api/orders/{id}/status` - Update order status
- `POST /api/orders/{id}/cancel` - Cancel order

## 🗄️ Database Schema

### Main Tables
- **Users** - User accounts with authentication
- **Products** - Product catalog
- **Categories** - Product categories
- **Inventory** - Stock management
- **Orders** - Order records
- **OrderItems** - Order line items
- **CartItems** - Shopping cart items
- **Reviews** - Product reviews and ratings

## 🔐 Default Test Accounts

```
Admin Account:
Email: admin@test.com
Password: password123

User Account:
Email: john.doe@email.com
Password: password123
```

## 🏗️ Project Structure

```
week-5-Ecommerce/
├── demo/                          # Backend (Spring Boot)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/smartcommerce/
│   │   │   │   ├── aspect/       # AOP logging aspects
│   │   │   │   ├── config/       # Configuration classes
│   │   │   │   ├── controller/   # REST & GraphQL controllers
│   │   │   │   ├── dao/          # Data access layer
│   │   │   │   ├── dtos/         # Request/Response DTOs
│   │   │   │   ├── exception/    # Exception handling
│   │   │   │   ├── model/        # Entity models
│   │   │   │   ├── service/      # Business logic
│   │   │   │   ├── utils/        # Utility classes
│   │   │   │   └── validation/   # Custom validators
│   │   │   └── resources/
│   │   │       ├── sql/          # Database scripts
│   │   │       └── graphql/      # GraphQL schemas
│   │   └── test/                 # Unit tests
│   └── pom.xml                   # Maven dependencies
│
└── frontend/                      # Frontend (React)
    ├── src/
    │   ├── components/           # Reusable components
    │   ├── context/              # Context providers
    │   ├── pages/                # Page components
    │   │   ├── admin/           # Admin pages
    │   │   └── client/          # Client pages
    │   └── services/            # API service layer
    └── package.json             # npm dependencies
```

## 🎯 Key Features Implementation

### Sorting Algorithm
- Custom merge sort implementation for product sorting
- Strategy pattern for flexible sorting options

### AOP (Aspect-Oriented Programming)
- **Logging Aspect** - Logs all method calls
- **Performance Monitoring** - Tracks execution time
- **Exception Logging** - Centralized exception handling

### Validation
- Custom validators for phone numbers, price ranges, and sort directions
- Bean validation with Jakarta Validation API

### Security
- BCrypt password hashing
- Role-based access control (Admin/User)
- CORS configuration for frontend integration

## 🧪 Testing

### Run Backend Tests
```bash
cd demo
mvnw test
```

### Run Frontend Tests
```bash
cd frontend
npm test
```

## 📦 Building for Production

### Backend
```bash
cd demo
mvnw clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Frontend
```bash
cd frontend
npm run build
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License.

## 👥 Authors

- Emmanuel Peprah Mensah

## 🙏 Acknowledgments

- Spring Boot Documentation
- React Documentation
- MySQL Documentation
