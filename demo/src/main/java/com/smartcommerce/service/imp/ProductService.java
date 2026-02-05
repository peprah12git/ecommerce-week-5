package com.smartcommerce.service.imp;

import com.smartcommerce.dao.interfaces.CategoryDaoInterface;
import com.smartcommerce.dao.interfaces.ProductDaoInterface;
import com.smartcommerce.exception.BusinessException;
import com.smartcommerce.exception.ResourceNotFoundException;
import com.smartcommerce.model.Category;
import com.smartcommerce.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

/**
 * Service layer for Product entity
 * Handles business logic, validation, and orchestration of product operations
 */
@Service
@Transactional
public class ProductService {

    private final ProductDaoInterface productDao;
    private final CategoryDaoInterface categoryDao;

    @Autowired
    public ProductService(ProductDaoInterface productDao, CategoryDaoInterface categoryDao) {
        this.productDao = productDao;
        this.categoryDao = categoryDao;
    }

    /**
     * Creates a new product
     * @param product Product object to create
     * @return Created product
     * @throws ResourceNotFoundException if category not found
     * @throws BusinessException if product creation fails
     */
    public Product createProduct(Product product) {
        // Validate input
        validateProduct(product);

        // Verify category exists
        Category category = categoryDao.getCategoryById(product.getCategoryId());
        if (category == null) {
            throw new ResourceNotFoundException("Category", "id", product.getCategoryId());
        }

        // Add product
        boolean success = productDao.addProduct(product);
        if (!success) {
            throw new BusinessException("Failed to create product");
        }

        // Invalidate cache after creating
        productDao.invalidateCache();

        // Retrieve and return the created product
        List<Product> products = productDao.getAllProducts();
        Product createdProduct = products.stream()
                .filter(p -> p.getProductName().equals(product.getProductName())
                        && p.getCategoryId() == product.getCategoryId())
                .findFirst()
                .orElse(null);

        if (createdProduct == null) {
            throw new BusinessException("Product created but could not be retrieved");
        }

        return createdProduct;
    }

    /**
     * Retrieves all products
     * @return List of all products
     */
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    /**
     * Retrieves a product by ID
     * @param productId Product ID
     * @return Product object
     * @throws ResourceNotFoundException if product not found
     */
    @Transactional(readOnly = true)
    public Product getProductById(int productId) {
        Product product = productDao.getProductById(productId);
        if (product == null) {
            throw new ResourceNotFoundException("Product", "id", productId);
        }
        return product;
    }

    /**
     * Retrieves products by category name
     * @param categoryName Category name
     * @return List of products in the category
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new BusinessException("Category name cannot be empty");
        }

        return productDao.getProductsByCategory(categoryName);
    }

    /**
     * Searches for products by name or description
     * @param searchTerm Search term
     * @return List of matching products
     */
    @Transactional(readOnly = true)
    public List<Product> searchProducts(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new BusinessException("Search term cannot be empty");
        }

        return productDao.searchProducts(searchTerm);
    }

    /**
     * Updates an existing product
     * @param productId Product ID to update
     * @param productDetails Updated product details
     * @return Updated product
     * @throws ResourceNotFoundException if product or category not found
     * @throws BusinessException if update fails
     */
    public Product updateProduct(int productId, Product productDetails) {
        // Check if product exists
        Product existingProduct = productDao.getProductById(productId);
        if (existingProduct == null) {
            throw new ResourceNotFoundException("Product", "id", productId);
        }

        // Validate updated details
        validateProduct(productDetails);

        // Verify category exists (if category is being changed)
        if (existingProduct.getCategoryId() != productDetails.getCategoryId()) {
            Category category = categoryDao.getCategoryById(productDetails.getCategoryId());
            if (category == null) {
                throw new ResourceNotFoundException("Category", "id", productDetails.getCategoryId());
            }
        }

        // Update product details
        existingProduct.setProductName(productDetails.getProductName());
        existingProduct.setDescription(productDetails.getDescription());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setCategoryId(productDetails.getCategoryId());

        // Update quantity if provided
        if (productDetails.getQuantityAvailable() >= 0) {
            existingProduct.setQuantityAvailable(productDetails.getQuantityAvailable());
        }

        // Perform update
        boolean success = productDao.updateProduct(existingProduct);
        if (!success) {
            throw new BusinessException("Failed to update product");
        }

        // Invalidate cache after updating
        productDao.invalidateCache();

        return getProductById(productId);
    }

    /**
     * Updates product quantity
     * @param productId Product ID
     * @param quantity New quantity
     * @return Updated product
     * @throws ResourceNotFoundException if product not found
     * @throws BusinessException if quantity is negative or update fails
     */
    public Product updateProductQuantity(int productId, int quantity) {
        if (quantity < 0) {
            throw new BusinessException("Quantity cannot be negative");
        }

        Product product = getProductById(productId);
        product.setQuantityAvailable(quantity);

        boolean success = productDao.updateProduct(product);
        if (!success) {
            throw new BusinessException("Failed to update product quantity");
        }

        productDao.invalidateCache();
        return getProductById(productId);
    }

    /**
     * Deletes a product
     * @param productId Product ID to delete
     * @throws ResourceNotFoundException if product not found
     * @throws BusinessException if deletion fails
     */
    public void deleteProduct(int productId) {
        // Check if product exists
        Product product = productDao.getProductById(productId);
        if (product == null) {
            throw new ResourceNotFoundException("Product", "id", productId);
        }

        // Perform deletion
        boolean success = productDao.deleteProduct(productId);
        if (!success) {
            throw new BusinessException("Failed to delete product");
        }

        // Invalidate cache after deleting
        productDao.invalidateCache();
    }

    /**
     * Invalidates the product cache
     */
    public void invalidateProductCache() {
        productDao.invalidateCache();
    }

    /**
     * Validates product data
     * @param product Product to validate
     * @throws BusinessException if validation fails
     */
    private void validateProduct(Product product) {
        if (product == null) {
            throw new BusinessException("Product cannot be null");
        }

        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            throw new BusinessException("Product name is required");
        }

        if (product.getProductName().length() > 255) {
            throw new BusinessException("Product name cannot exceed 255 characters");
        }

        if (product.getPrice() == null) {
            throw new BusinessException("Product price is required");
        }

        if (product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Product price cannot be negative");
        }

        if (product.getCategoryId() <= 0) {
            throw new BusinessException("Valid category ID is required");
        }
    }
}

