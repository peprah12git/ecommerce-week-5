package com.smartcommerce.controller;

import com.smartcommerce.dtos.request.CreateProductDTO;
import com.smartcommerce.dtos.request.UpdateProductDTO;
import com.smartcommerce.dtos.request.UpdateProductQuantityDTO;
import com.smartcommerce.model.Product;
import com.smartcommerce.service.imp.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Product management
 * Handles HTTP requests for product CRUD operations
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
    public ResponseEntity<Product> createProduct(
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

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdProduct);
    }

    /**
     * Get all products
     * GET /api/products
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Get product by ID
     * GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Get products by category
     * GET /api/products/category/{categoryName}
     */
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<Product>> getProductsByCategory(
            @PathVariable String categoryName) {
        List<Product> products = productService.getProductsByCategory(categoryName);
        return ResponseEntity.ok(products);
    }

    /**
     * Search products by name or description
     * GET /api/products/search?term={searchTerm}
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam String term) {
        List<Product> products = productService.searchProducts(term);
        return ResponseEntity.ok(products);
    }

    /**
     * Update product
     * PUT /api/products/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
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

        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Update product quantity only
     * PATCH /api/products/{id}/quantity
     */
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<Product> updateProductQuantity(
            @PathVariable int id,
            @Valid @RequestBody UpdateProductQuantityDTO updateQuantityDTO) {

        Product updatedProduct = productService.updateProductQuantity(
                id,
                updateQuantityDTO.quantity()
        );

        return ResponseEntity.ok(updatedProduct);
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