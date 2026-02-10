import api from './api';

const ProductService = {
  // Get all products without pagination
  getAllProducts: async () => {
    const response = await api.get('/products/all');
    return response.data;
  },

  // Get products with pagination and filtering
  getProducts: async (params = {}) => {
    const {
      page = 0,
      size = 10,
      sortBy = 'productId',
      sortDirection = 'ASC',
      category,
      minPrice,
      maxPrice,
      searchTerm,
      inStock,
    } = params;

    const queryParams = new URLSearchParams({
      page: page.toString(),
      size: size.toString(),
      sortBy,
      sortDirection,
    });

    if (category) queryParams.append('category', category);
    if (minPrice) queryParams.append('minPrice', minPrice.toString());
    if (maxPrice) queryParams.append('maxPrice', maxPrice.toString());
    if (searchTerm) queryParams.append('searchTerm', searchTerm);
    if (inStock !== undefined) queryParams.append('inStock', inStock.toString());

    const response = await api.get(`/products?${queryParams}`);
    return response.data;
  },

  // Get single product by ID
  getProductById: async (id) => {
    const response = await api.get(`/products/${id}`);
    return response.data;
  },

  // Get products by category
  getProductsByCategory: async (categoryName) => {
    const response = await api.get(`/products/category/${categoryName}`);
    return response.data;
  },

  // Search products
  searchProducts: async (term) => {
    const response = await api.get(`/products/search?term=${encodeURIComponent(term)}`);
    return response.data;
  },

  // Create new product
  createProduct: async (productData) => {
    const response = await api.post('/products', productData);
    return response.data;
  },

  // Update product
  updateProduct: async (id, productData) => {
    const response = await api.put(`/products/${id}`, productData);
    return response.data;
  },

  // Update product quantity
  updateProductQuantity: async (id, quantity) => {
    const response = await api.patch(`/products/${id}/quantity`, { quantity });
    return response.data;
  },

  // Delete product
  deleteProduct: async (id) => {
    await api.delete(`/products/${id}`);
  },

  // Invalidate cache
  invalidateCache: async () => {
    await api.post('/products/cache/invalidate');
  },
};

export default ProductService;
