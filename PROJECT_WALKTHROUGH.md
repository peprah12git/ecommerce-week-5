# SmartCommerce Project Walkthrough: Beginner to Advanced

## Table of Contents

1. [Objective 1: Spring Boot Configuration, IoC & Dependency Injection](#objective-1)
2. [Objective 2: RESTful APIs with Layered Architecture](#objective-2)
3. [Objective 3: Validation, Exception Handling & API Documentation](#objective-3)
4. [Objective 4: GraphQL Integration](#objective-4)
5. [Objective 5: AOP & Algorithmic Techniques](#objective-5)

---

## Objective 1: Spring Boot Configuration, IoC & Dependency Injection

### 🎯 Goal

Apply Spring Boot configuration principles, IoC, and Dependency Injection to build modular and maintainable enterprise
web applications.

### 📚 Beginner Level: Understanding the Basics

#### 1.1 Application Entry Point

**File:** `SmartcommerceApplication.java`

```java

@SpringBootApplication
@EnableAspectJAutoProxy
public class SmartcommerceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartcommerceApplication.class, args);
    }
}
```

**Key Concepts:**

- `@SpringBootApplication`: Combines `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan`
- `@EnableAspectJAutoProxy`: Enables AOP support (we'll cover this in Objective 5)
- Spring Boot auto-configures beans based on classpath dependencies

#### 1.2 Configuration Files

**File:** `application-dev.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecommerce_db
    username: dev_user
    password: noah_1@23.Djanor
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      maximum-pool-size: 10
      connection-timeout: 30000
```

**Key Concepts:**

- Environment-based configuration (dev, prod, test)
- Externalized configuration for database credentials
- HikariCP connection pooling configuration

### 🔧 Intermediate Level: Dependency Injection Patterns

#### 1.3 Configuration Class with @Bean

**File:** `DataSourceConfig.java`

```java

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://localhost:3306/ecommerce_db")
                .username("root")
                .password("noah_1@23.Djanor")
                .build();
    }
}
```

**Key Concepts:**

- `@Configuration`: Marks class as source of bean definitions
- `@Bean`: Registers method return value as Spring bean
- Manual bean creation for fine-grained control

#### 1.4 Constructor-Based Dependency Injection

**File:** `ProductDAO.java`

```java

@Repository
public class ProductDAO implements ProductDaoInterface {
    private DataSource dataSource;

    public ProductDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
```

**Why Constructor Injection?**

- ✅ Immutability (final fields)
- ✅ Required dependencies are explicit
- ✅ Easier to test (no reflection needed)
- ✅ Prevents circular dependencies

### 🚀 Advanced Level: Layered Architecture with DI

#### 1.5 Complete Dependency Chain

**Controller → Service → DAO**

```java
// Controller Layer
@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
}

// Service Layer
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductDaoInterface productDao;
    private final CategoryDaoInterface categoryDao;
    private final SortStrategy<Product> sortStrategy;

    public ProductServiceImpl(ProductDaoInterface productDao,
                              CategoryDaoInterface categoryDao,
                              SortStrategy<Product> sortStrategy) {
        this.productDao = productDao;
        this.categoryDao = categoryDao;
        this.sortStrategy = sortStrategy;
    }
}

// DAO Layer
@Repository
public class ProductDAO implements ProductDaoInterface {
    private DataSource dataSource;

    public ProductDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
```

**Key Principles:**

- **Inversion of Control (IoC)**: Spring manages object lifecycle
- **Dependency Injection**: Dependencies provided by Spring container
- **Interface-Based Design**: Program to interfaces, not implementations
- **Single Responsibility**: Each layer has distinct responsibility

---

## Objective 2: RESTful APIs with Layered Architecture

### 🎯 Goal

Develop RESTful APIs using layered architecture (Controller → Service → Repository) with environment-based
configurations and clean response handling.

### 📚 Beginner Level: Basic REST Endpoints

#### 2.1 Simple GET Endpoint

**File:** `ProductController.java`

```java

@GetMapping("/{id}")
public ResponseEntity<ProductResponse> getProductById(@PathVariable int id) {
    Product product = productService.getProductById(id);
    ProductResponse response = ProductMapper.toProductResponse(product);
    return ResponseEntity.ok(response);
}
```

**Key Concepts:**

- `@GetMapping`: Maps HTTP GET requests
- `@PathVariable`: Extracts value from URL path
- `ResponseEntity`: Wraps response with HTTP status
- DTO Pattern: Separate internal models from API responses

#### 2.2 POST Endpoint with Request Body

**File:** `ProductController.java`

```java

@PostMapping
public ResponseEntity<ProductResponse> createProduct(
        @Valid @RequestBody CreateProductDTO createProductDTO) {

    Product product = new Product(
            createProductDTO.productName(),
            createProductDTO.description(),
            createProductDTO.price(),
            createProductDTO.categoryId()
    );

    Product createdProduct = productService.createProduct(product);
    ProductResponse response = ProductMapper.toProductResponse(createdProduct);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}
```

**Key Concepts:**

- `@PostMapping`: Maps HTTP POST requests
- `@RequestBody`: Deserializes JSON to Java object
- `@Valid`: Triggers validation (covered in Objective 3)
- HTTP 201 Created status for resource creation

### 🔧 Intermediate Level: Layered Architecture

#### 2.3 Controller Layer (Presentation)

**Responsibilities:**

- Handle HTTP requests/responses
- Validate input (basic)
- Map DTOs to domain models
- Return appropriate HTTP status codes

```java

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<PagedResponse<ProductResponse>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // Delegate to service layer
        List<Product> products = productService.getProductsWithPagination(page, size);
        // Map to response DTOs
        List<ProductResponse> responses = ProductMapper.toProductResponseList(products);
        return ResponseEntity.ok(new PagedResponse<>(responses, page, size));
    }
}
```

#### 2.4 Service Layer (Business Logic)

**File:** `ProductServiceImpl.java`

**Responsibilities:**

- Business logic and validation
- Transaction management
- Coordinate multiple DAOs
- Apply business rules

```java

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Override
    public Product createProduct(Product product) {
        // Business validation
        validateProduct(product);

        // Check if category exists
        Category category = categoryDao.getCategoryById(product.getCategoryId());
        if (category == null) {
            throw new ResourceNotFoundException("Category", "id", product.getCategoryId());
        }

        // Persist product
        boolean success = productDao.addProduct(product);
        if (!success) {
            throw new BusinessException("Failed to create product");
        }

        // Invalidate cache
        productDao.invalidateCache();

        return getProductById(product.getProductId());
    }
}
```

#### 2.5 DAO Layer (Data Access)

**File:** `ProductDAO.java`

**Responsibilities:**

- Database operations (CRUD)
- SQL query execution
- Result set mapping
- Caching (optional)

```java

@Repository
public class ProductDAO implements ProductDaoInterface {
    private DataSource dataSource;

    @Override
    public Product getProductById(int id) {
        String sql = "SELECT p.*, c.category_name, COALESCE(i.quantity_available, 0) as quantity " +
                "FROM Products p " +
                "LEFT JOIN Categories c ON p.category_id = c.category_id " +
                "LEFT JOIN Inventory i ON p.product_id = i.product_id " +
                "WHERE p.product_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractProduct(rs);
            }
        } catch (SQLException e) {
            // Handle exception
        }
        return null;
    }
}
```

### 🚀 Advanced Level: Complex Operations

#### 2.6 Pagination, Filtering & Sorting

**File:** `ProductController.java`

```java

@GetMapping
public ResponseEntity<PagedResponse<ProductResponse>> getProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "productId") String sortBy,
        @RequestParam(defaultValue = "ASC") @ValidSortDirection String sortDirection,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) BigDecimal minPrice,
        @RequestParam(required = false) BigDecimal maxPrice,
        @RequestParam(required = false) String searchTerm,
        @RequestParam(required = false) Boolean inStock) {

    ProductFilterDTO filters = new ProductFilterDTO(
            category, minPrice, maxPrice, searchTerm, inStock
    );

    List<Product> products = productService.getProductsWithPaginationAndFilters(
            page, size, sortBy, sortDirection, filters
    );

    long totalElements = productService.countProductsWithFilters(filters);

    List<ProductResponse> productResponses = ProductMapper.toProductResponseList(products);

    PagedResponse<ProductResponse> response = new PagedResponse<>(
            productResponses, page, size, totalElements, sortBy, sortDirection
    );

    return ResponseEntity.ok(response);
}
```

**Service Layer Implementation:**

```java

@Override
public List<Product> getProductsWithPaginationAndFilters(
        int pageNumber, int pageSize, String sortBy,
        String sortDirection, ProductFilterDTO filters) {

    // 1. Get all products
    List<Product> products = productDao.getAllProducts();

    // 2. Apply filters
    if (filters != null && filters.hasFilters()) {
        products = applyFilters(products, filters);
    }

    // 3. Apply sorting (using Strategy Pattern)
    products = applySorting(products, sortBy, sortDirection);

    // 4. Apply pagination
    return applyPagination(products, pageNumber, pageSize);
}

private List<Product> applyFilters(List<Product> products, ProductFilterDTO filters) {
    return products.stream()
            .filter(p -> matchesCategory(p, filters.category()))
            .filter(p -> matchesPriceRange(p, filters.minPrice(), filters.maxPrice()))
            .filter(p -> matchesSearchTerm(p, filters.searchTerm()))
            .filter(p -> matchesStockStatus(p, filters.inStock()))
            .collect(Collectors.toList());
}
```

---

## Objective 3: Validation, Exception Handling & API Documentation

### 🎯 Goal

Implement validation, exception handling, and API documentation using Bean Validation, @ControllerAdvice, and Springdoc
OpenAPI.

### 📚 Beginner Level: Basic Validation

#### 3.1 Bean Validation with Jakarta Validation

**File:** `CreateProductDTO.java`

```java
public record CreateProductDTO(
        @NotBlank(message = "Product name is required")
        @Size(min = 2, max = 255, message = "Product name must be between 2 and 255 characters")
        String productName,

        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be greater than zero")
        BigDecimal price,

        @NotNull(message = "Category ID is required")
        @Positive(message = "Category ID must be a positive number")
        Integer categoryId,

        @Min(value = 0, message = "Quantity available cannot be negative")
        Integer quantityAvailable
) {
}
```

**Common Validation Annotations:**

- `@NotNull`: Field cannot be null
- `@NotBlank`: String cannot be null, empty, or whitespace
- `@Size`: String/Collection size constraints
- `@Min/@Max`: Numeric minimum/maximum
- `@DecimalMin/@DecimalMax`: Decimal constraints
- `@Positive/@Negative`: Number sign validation
- `@Email`: Email format validation
- `@Pattern`: Regex pattern matching

#### 3.2 Triggering Validation in Controller

```java

@PostMapping
public ResponseEntity<ProductResponse> createProduct(
        @Valid @RequestBody CreateProductDTO createProductDTO) {
    // If validation fails, MethodArgumentNotValidException is thrown
    // GlobalExceptionHandler catches it and returns 400 Bad Request
}
```

### 🔧 Intermediate Level: Custom Validators

#### 3.3 Custom Validation Annotation

**File:** `ValidSortDirection.java`

```java

@Documented
@Constraint(validatedBy = SortDirectionValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSortDirection {
    String message() default "Sort direction must be 'ASC' or 'DESC'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
```

**File:** `SortDirectionValidator.java`

```java
public class SortDirectionValidator implements ConstraintValidator<ValidSortDirection, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; // Let @NotNull handle null checks
        }
        return "ASC".equalsIgnoreCase(value) || "DESC".equalsIgnoreCase(value);
    }
}
```

**Usage:**

```java

@GetMapping
public ResponseEntity<PagedResponse<ProductResponse>> getProducts(
        @RequestParam(defaultValue = "ASC") @ValidSortDirection String sortDirection) {
    // sortDirection is validated before method execution
}
```

### 🚀 Advanced Level: Global Exception Handling

#### 3.4 Centralized Exception Handler

**File:** `GlobalExceptionHandler.java`

```java

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle validation errors from @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        ValidationErrorResponse response = ValidationErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("One or more fields have validation errors")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    // Handle custom business exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {

        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Handle business logic exceptions
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {

        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    // Catch-all for unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);

        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
```

**Benefits:**

- ✅ Consistent error response format across all endpoints
- ✅ Centralized error handling logic
- ✅ Proper HTTP status codes
- ✅ Detailed error messages for debugging
- ✅ Clean controller code (no try-catch blocks)

#### 3.5 Custom Exception Classes

```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s not found with %s: %s", resource, field, value));
    }
}

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
```

### 📖 API Documentation with OpenAPI

#### 3.6 OpenAPI Configuration

**File:** `OpenApiConfig.java`

```java

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SmartCommerce API")
                        .version("1.0")
                        .description("E-commerce platform REST API"));
    }
}
```

#### 3.7 Documenting Endpoints

**File:** `ProductController.java`

```java

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Product management API")
public class ProductController {

    @Operation(summary = "Create a new product",
            description = "Creates a new product with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody CreateProductDTO createProductDTO) {
        // Implementation
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @Parameter(description = "Product ID", required = true, example = "1")
            @PathVariable int id) {
        // Implementation
    }
}
```

**Access Swagger UI:** `http://localhost:8080/swagger-ui.html`

---

## Objective 4: GraphQL Integration

### 🎯 Goal

Integrate GraphQL schemas, queries, and mutations to enable flexible and optimized data retrieval alongside REST
endpoints.

### 📚 Beginner Level: GraphQL Basics

#### 4.1 GraphQL Schema Definition

**File:** `schema.graphqls` (in resources/graphql/)

```graphql
type Product {
    productId: Int!
    productName: String!
    description: String
    price: Float!
    categoryId: Int!
    categoryName: String
    quantityAvailable: Int!
    createdAt: String
}

type Query {
    # Get single product by ID
    product(id: Int!): Product

    # Get all products with optional filters
    products(
        pageNumber: Int
        pageSize: Int
        category: String
        minPrice: Float
        maxPrice: Float
        searchTerm: String
    ): [Product!]!

    # Search products
    searchProducts(searchTerm: String!): [Product!]!
}

type Mutation {
    # Create new product
    createProduct(
        productName: String!
        description: String
        price: Float!
        categoryId: Int!
        quantityAvailable: Int
    ): Product!

    # Update existing product
    updateProduct(
        id: Int!
        productName: String
        description: String
        price: Float
        categoryId: Int
    ): Product!

    # Update product quantity
    updateProductQuantity(id: Int!, quantity: Int!): Product!

    # Delete product
    deleteProduct(id: Int!): Boolean!
}
```

### 🔧 Intermediate Level: GraphQL Controllers

#### 4.2 Query Mapping

**File:** `ProductGraphQLController.java`

```java

@Controller
public class ProductGraphQLController {

    private final ProductService productService;

    public ProductGraphQLController(ProductService productService) {
        this.productService = productService;
    }

    // Query: product(id: Int!): Product
    @QueryMapping
    public Product product(@Argument int id) {
        return productService.getProductById(id);
    }

    // Query: products(...): [Product!]!
    @QueryMapping
    public List<Product> products(
            @Argument Integer pageNumber,
            @Argument Integer pageSize,
            @Argument String category,
            @Argument Double minPrice,
            @Argument Double maxPrice,
            @Argument String searchTerm) {

        if (pageNumber != null || pageSize != null || category != null) {
            int page = pageNumber != null ? pageNumber : 0;
            int size = pageSize != null ? pageSize : 10;

            ProductFilterDTO filters = new ProductFilterDTO(
                    category,
                    minPrice != null ? BigDecimal.valueOf(minPrice) : null,
                    maxPrice != null ? BigDecimal.valueOf(maxPrice) : null,
                    searchTerm,
                    null
            );

            return productService.getProductsWithPaginationAndFilters(
                    page, size, "productId", "ASC", filters
            );
        }

        return productService.getAllProducts();
    }
}
```

#### 4.3 Mutation Mapping

```java

@Controller
public class ProductGraphQLController {

    // Mutation: createProduct(...): Product!
    @MutationMapping
    public Product createProduct(
            @Argument String productName,
            @Argument String description,
            @Argument Double price,
            @Argument int categoryId,
            @Argument Integer quantityAvailable) {

        Product product = new Product(
                productName,
                description,
                BigDecimal.valueOf(price),
                categoryId
        );

        if (quantityAvailable != null) {
            product.setQuantityAvailable(quantityAvailable);
        }

        return productService.createProduct(product);
    }

    // Mutation: updateProductQuantity(id: Int!, quantity: Int!): Product!
    @MutationMapping
    public Product updateProductQuantity(
            @Argument int id,
            @Argument int quantity) {
        return productService.updateProductQuantity(id, quantity);
    }

    // Mutation: deleteProduct(id: Int!): Boolean!
    @MutationMapping
    public boolean deleteProduct(@Argument int id) {
        productService.deleteProduct(id);
        return true;
    }
}
```

### 🚀 Advanced Level: GraphQL vs REST

#### 4.4 When to Use GraphQL vs REST

**Use GraphQL when:**

- ✅ Clients need flexible data fetching
- ✅ Multiple resources in single request
- ✅ Avoiding over-fetching/under-fetching
- ✅ Mobile apps with bandwidth constraints
- ✅ Complex nested data structures

**Use REST when:**

- ✅ Simple CRUD operations
- ✅ Caching is critical (HTTP caching)
- ✅ File uploads/downloads
- ✅ Standard HTTP status codes needed
- ✅ Team unfamiliar with GraphQL

#### 4.5 GraphQL Query Examples

**Query 1: Get single product**

```graphql
query {
    product(id: 1) {
        productId
        productName
        price
        categoryName
    }
}
```

**Query 2: Get filtered products**

```graphql
query {
    products(
        pageNumber: 0
        pageSize: 10
        category: "Electronics"
        minPrice: 100
        maxPrice: 1000
    ) {
        productId
        productName
        price
        quantityAvailable
    }
}
```

**Mutation: Create product**

```graphql
mutation {
    createProduct(
        productName: "Wireless Mouse"
        description: "Ergonomic wireless mouse"
        price: 29.99
        categoryId: 1
        quantityAvailable: 50
    ) {
        productId
        productName
        price
    }
}
```

**Access GraphiQL:** `http://localhost:8080/graphiql`

---

## Objective 5: AOP & Algorithmic Techniques

### 🎯 Goal

Apply Aspect-Oriented Programming (AOP) and algorithmic techniques for logging, monitoring, and efficient sorting,
searching, and pagination within APIs.

### 📚 Beginner Level: Understanding AOP

#### 5.1 What is AOP?

Aspect-Oriented Programming separates cross-cutting concerns (logging, security, transactions) from business logic.

**Key Concepts:**

- **Aspect**: Module that encapsulates cross-cutting concern
- **Join Point**: Point in program execution (method call)
- **Pointcut**: Expression that matches join points
- **Advice**: Action taken at join point (@Before, @After, @Around)

#### 5.2 Enabling AOP

```java

@SpringBootApplication
@EnableAspectJAutoProxy  // Enable AOP support
public class SmartcommerceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartcommerceApplication.class, args);
    }
}
```

### 🔧 Intermediate Level: Logging Aspect

#### 5.3 Logging Aspect Implementation

**File:** `LoggingAspect.java`

```java

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Pointcut for all service layer methods
    @Pointcut("execution(* com.smartcommerce.service..*.*(..))")
    public void serviceLayerPointcut() {
    }

    // Pointcut for all REST controller methods
    @Pointcut("execution(* com.smartcommerce.controller..*.*(..))")
    public void controllerLayerPointcut() {
    }

    // Log service method entry
    @Before("serviceLayerPointcut()")
    public void logServiceMethodEntry(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.debug("⏩ [SERVICE] Entering: {}.{}() with arguments: {}",
                getSimpleClassName(className),
                methodName,
                formatArguments(args));
    }

    // Log service method completion
    @AfterReturning(pointcut = "serviceLayerPointcut()", returning = "result")
    public void logServiceMethodExit(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        logger.debug("✅ [SERVICE] Completed: {}.{}()",
                getSimpleClassName(className),
                methodName);
    }

    // Log REST API controller invocations
    @Before("controllerLayerPointcut()")
    public void logControllerMethodEntry(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info("⏩ [REST API] Request: {}.{}() with parameters: {}",
                getSimpleClassName(className),
                methodName,
                formatArguments(args));
    }
}
```

**Benefits:**

- ✅ Centralized logging logic
- ✅ No logging code in business logic
- ✅ Easy to enable/disable
- ✅ Consistent logging format

#### 5.4 Performance Monitoring Aspect

**File:** `PerformanceMonitoringAspect.java`

```java

@Aspect
@Component
public class PerformanceMonitoringAspect {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceMonitoringAspect.class);

    @Around("execution(* com.smartcommerce.service..*.*(..))")
    public Object monitorPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        try {
            // Execute the method
            Object result = joinPoint.proceed();

            long executionTime = System.currentTimeMillis() - startTime;

            if (executionTime > 1000) {
                logger.warn("⚠️ [PERFORMANCE] Slow method: {}.{}() took {}ms",
                        getSimpleClassName(className),
                        methodName,
                        executionTime);
            } else {
                logger.debug("⏱️ [PERFORMANCE] {}.{}() took {}ms",
                        getSimpleClassName(className),
                        methodName,
                        executionTime);
            }

            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("❌ [PERFORMANCE] {}.{}() failed after {}ms",
                    getSimpleClassName(className),
                    methodName,
                    executionTime);
            throw e;
        }
    }
}
```

### 🚀 Advanced Level: Algorithmic Techniques

#### 5.5 Strategy Pattern for Sorting

**File:** `SortStrategy.java`

```java
public interface SortStrategy<T> {
    List<T> sort(List<T> items, Comparator<T> comparator);

    String getAlgorithmName();
}
```

**File:** `MergeSortStrategy.java`

```java

@Component
public class MergeSortStrategy<T> implements SortStrategy<T> {

    @Override
    public List<T> sort(List<T> items, Comparator<T> comparator) {
        if (items == null || items.size() <= 1) {
            return items == null ? new ArrayList<>() : new ArrayList<>(items);
        }

        List<T> result = new ArrayList<>(items);
        mergeSort(result, 0, result.size() - 1, comparator);
        return result;
    }

    private void mergeSort(List<T> list, int left, int right, Comparator<T> comparator) {
        if (left < right) {
            int middle = left + (right - left) / 2;

            // Sort first and second halves
            mergeSort(list, left, middle, comparator);
            mergeSort(list, middle + 1, right, comparator);

            // Merge the sorted halves
            merge(list, left, middle, right, comparator);
        }
    }

    private void merge(List<T> list, int left, int middle, int right,
                       Comparator<T> comparator) {
        int leftSize = middle - left + 1;
        int rightSize = right - middle;

        List<T> leftArray = new ArrayList<>(leftSize);
        List<T> rightArray = new ArrayList<>(rightSize);

        for (int i = 0; i < leftSize; i++) {
            leftArray.add(list.get(left + i));
        }
        for (int j = 0; j < rightSize; j++) {
            rightArray.add(list.get(middle + 1 + j));
        }

        int i = 0, j = 0, k = left;

        while (i < leftSize && j < rightSize) {
            if (comparator.compare(leftArray.get(i), rightArray.get(j)) <= 0) {
                list.set(k, leftArray.get(i));
                i++;
            } else {
                list.set(k, rightArray.get(j));
                j++;
            }
            k++;
        }

        while (i < leftSize) {
            list.set(k, leftArray.get(i));
            i++;
            k++;
        }

        while (j < rightSize) {
            list.set(k, rightArray.get(j));
            j++;
            k++;
        }
    }

    @Override
    public String getAlgorithmName() {
        return "Merge Sort";
    }
}
```

**Time Complexity:** O(n log n)
**Space Complexity:** O(n)

#### 5.6 Using Sort Strategy in Service

**File:** `ProductServiceImpl.java`

```java

@Service
public class ProductServiceImpl implements ProductService {

    private final SortStrategy<Product> sortStrategy;

    public ProductServiceImpl(SortStrategy<Product> sortStrategy) {
        this.sortStrategy = sortStrategy;
    }

    private List<Product> applySorting(List<Product> products,
                                       String sortBy,
                                       String sortDirection) {
        Comparator<Product> comparator = getComparator(sortBy);

        if ("DESC".equalsIgnoreCase(sortDirection)) {
            comparator = comparator.reversed();
        }

        // Use injected sort strategy (Merge Sort)
        return sortStrategy.sort(products, comparator);
    }

    private Comparator<Product> getComparator(String sortBy) {
        return switch (sortBy.toLowerCase()) {
            case "productname", "name" -> Comparator.comparing(
                    p -> p.getProductName() != null ? p.getProductName().toLowerCase() : "",
                    Comparator.nullsLast(Comparator.naturalOrder())
            );
            case "price" -> Comparator.comparing(
                    Product::getPrice,
                    Comparator.nullsLast(Comparator.naturalOrder())
            );
            case "quantity" -> Comparator.comparingInt(Product::getQuantityAvailable);
            case "productid", "id" -> Comparator.comparingInt(Product::getProductId);
            default -> throw new BusinessException("Invalid sort field: " + sortBy);
        };
    }
}
```

#### 5.7 Pagination Algorithm

```java
private List<Product> applyPagination(List<Product> products,
                                      int pageNumber,
                                      int pageSize) {
    int startIndex = pageNumber * pageSize;

    if (startIndex >= products.size()) {
        return List.of(); // Empty list if page out of bounds
    }

    int endIndex = Math.min(startIndex + pageSize, products.size());
    return products.subList(startIndex, endIndex);
}
```

**Example:**

- Total products: 100
- Page size: 10
- Page 0: products[0-9]
- Page 1: products[10-19]
- Page 5: products[50-59]

#### 5.8 Caching Strategy

**File:** `ProductDAO.java`

```java

@Repository
public class ProductDAO implements ProductDaoInterface {

    private static List<Product> productCache = null;
    private static long cacheTimestamp = 0;
    private static final long CACHE_TTL_MS = 300000; // 5 minutes

    private boolean isCacheValid() {
        return productCache != null &&
                (System.currentTimeMillis() - cacheTimestamp) < CACHE_TTL_MS;
    }

    @Override
    public List<Product> getAllProducts() {
        if (isCacheValid()) {
            return new ArrayList<>(productCache);
        }

        // Fetch from database
        List<Product> products = fetchFromDatabase();

        // Update cache
        productCache = new ArrayList<>(products);
        cacheTimestamp = System.currentTimeMillis();

        return products;
    }

    public void invalidateCache() {
        productCache = null;
        cacheTimestamp = 0;
    }
}
```

---

## 🎓 Learning Path Summary

### Beginner → Intermediate → Advanced

1. **Start with Objective 1**: Understand Spring Boot basics, IoC, and DI
2. **Move to Objective 2**: Build REST APIs with layered architecture
3. **Add Objective 3**: Implement validation and exception handling
4. **Explore Objective 4**: Add GraphQL alongside REST
5. **Master Objective 5**: Apply AOP and algorithmic techniques

### Key Takeaways

✅ **Separation of Concerns**: Controller → Service → DAO
✅ **Dependency Injection**: Constructor-based for testability
✅ **Validation**: Bean Validation + Custom validators
✅ **Exception Handling**: Centralized with @ControllerAdvice
✅ **API Documentation**: OpenAPI/Swagger for REST
✅ **GraphQL**: Flexible data fetching alongside REST
✅ **AOP**: Cross-cutting concerns (logging, monitoring)
✅ **Algorithms**: Merge sort, pagination, caching

### Next Steps

1. Run the application: `mvnw spring-boot:run`
2. Test REST APIs: `http://localhost:8080/swagger-ui.html`
3. Test GraphQL: `http://localhost:8080/graphiql`
4. Review logs to see AOP in action
5. Experiment with filtering, sorting, and pagination
6. Add your own features following the same patterns

---

## 📚 Additional Resources

- Spring Boot Documentation: https://spring.io/projects/spring-boot
- Spring GraphQL: https://spring.io/projects/spring-graphql
- Bean Validation: https://beanvalidation.org/
- AspectJ: https://www.eclipse.org/aspectj/
- OpenAPI Specification: https://swagger.io/specification/

**Happy Learning! 🚀**
