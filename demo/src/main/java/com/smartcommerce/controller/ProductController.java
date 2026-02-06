package com.smartcommerce.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartcommerce.dtos.request.CreateProductDTO;
import com.smartcommerce.dtos.request.ProductFilterDTO;
import com.smartcommerce.dtos.request.UpdateProductDTO;
import com.smartcommerce.dtos.request.UpdateProductQuantityDTO;
import com.smartcommerce.dtos.response.PagedResponse;
import com.smartcommerce.dtos.response.ProductResponse;
import com.smartcommerce.model.Product;
import com.smartcommerce.service.serviceInterface.ProductService;
import com.smartcommerce.utils.ProductMapper;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 * REST Controller for Product management
 * Handles HTTP requests for product CRUD operations with pagination, sorting, and filtering
 * Base URL: /api/products
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    /**
     * Create a new product
     * POST /api/products
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody CreateProductDTO createProductDTO) {

        Product product = new Product(
                createProductDTO.productName(),
                createProductDTO.description(),
                createProductDTO.price(),
                createProductDTO.categoryId()
        );

        if (createProductDTO.quantityAvailable() != null) {
            product.setQuantityAvailable(createProductDTO.quantityAvailable());
        }

        Product createdProduct = productService.createProduct(product);
        ProductResponse response = ProductMapper.toProductResponse(createdProduct);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Get all products (without pagination)
     * GET /api/products/all
     */
    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductResponse> response = ProductMapper.toProductResponseList(products);
        return ResponseEntity.ok(response);
    }

    /**
     * Get products with pagination, sorting, and filtering
     * GET /api/products?page=0&size=10&sortBy=price&sortDirection=ASC&category=Electronics&minPrice=100&maxPrice=1000&searchTerm=phone&inStock=true
     *
     * @param page          Page number (default: 0)
     * @param size          Page size (default: 10, max: 100)
     * @param sortBy        Sort field (default: productId)
     *                      Options: productName, price, categoryName, quantity, createdAt, productId
     * @param sortDirection Sort direction (default: ASC)
     *                      Options: ASC, DESC
     * @param category      Filter by category name
     * @param minPrice      Filter by minimum price
     * @param maxPrice      Filter by maximum price
     * @param searchTerm    Search in product name and description
     * @param inStock       Filter by stock status (true=in stock, false=out of stock)
     */
    @GetMapping
    public ResponseEntity<PagedResponse<ProductResponse>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Boolean inStock) {

        // Create filter DTO
        ProductFilterDTO filters = new ProductFilterDTO(
                category,
                minPrice,
                maxPrice,
                searchTerm,
                inStock
        );

        // Get paginated and filtered products
        List<Product> products = productService.getProductsWithPaginationAndFilters(
                page, size, sortBy, sortDirection, filters
        );

        // Get total count for pagination
        long totalElements = productService.countProductsWithFilters(filters);

        // Convert to response DTOs
        List<ProductResponse> productResponses = ProductMapper.toProductResponseList(products);

        // Create paged response
        PagedResponse<ProductResponse> response = new PagedResponse<>(
                productResponses,
                page,
                size,
                totalElements,
                sortBy,
                sortDirection
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Get product by ID
     * GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable int id) {
        Product product = productService.getProductById(id);
        ProductResponse response = ProductMapper.toProductResponse(product);
        return ResponseEntity.ok(response);
    }

    /**
     * Get products by category (without pagination)
     * GET /api/products/category/{categoryName}
     */
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(
            @PathVariable String categoryName) {
        List<Product> products = productService.getProductsByCategory(categoryName);
        List<ProductResponse> response = ProductMapper.toProductResponseList(products);
        return ResponseEntity.ok(response);
    }

    /**
     * Search products by name or description (without pagination)
     * GET /api/products/search?term={searchTerm}
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam String term) {
        List<Product> products = productService.searchProducts(term);
        List<ProductResponse> response = ProductMapper.toProductResponseList(products);
        return ResponseEntity.ok(response);
    }

    /**
     * Update product
     * PUT /api/products/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable int id,
            @Valid @RequestBody UpdateProductDTO updateProductDTO) {

        Product product = new Product();
        product.setProductName(updateProductDTO.productName());
        product.setDescription(updateProductDTO.description());
        product.setPrice(updateProductDTO.price());
        product.setCategoryId(updateProductDTO.categoryId());

        if (updateProductDTO.quantityAvailable() != null) {
            product.setQuantityAvailable(updateProductDTO.quantityAvailable());
        }

        Product updatedProduct = productService.updateProduct(id, product);
        ProductResponse response = ProductMapper.toProductResponse(updatedProduct);

        return ResponseEntity.ok(response);
    }

    /**
     * Update product quantity only
     * PATCH /api/products/{id}/quantity
     */
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<ProductResponse> updateProductQuantity(
            @PathVariable int id,
            @Valid @RequestBody UpdateProductQuantityDTO updateQuantityDTO) {

        Product updatedProduct = productService.updateProductQuantity(
                id,
                updateQuantityDTO.quantity()
        );
        ProductResponse response = ProductMapper.toProductResponse(updatedProduct);

        return ResponseEntity.ok(response);
    }

    /**
     * Delete product
     * DELETE /api/products/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Invalidate product cache
     * POST /api/products/cache/invalidate
     */
    @PostMapping("/cache/invalidate")
    public ResponseEntity<Void> invalidateCache() {
        productService.invalidateProductCache();
        return ResponseEntity.ok().build();
    }
}