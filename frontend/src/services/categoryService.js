import api from './api';
import graphqlService from './graphqlService';

const CategoryService = {
  // Get all categories - USE GRAPHQL
  getAllCategories: async () => {
    return await graphqlService.getAllCategories();
  },

  // Get category by ID - USE GRAPHQL
  getCategoryById: async (id) => {
    return await graphqlService.getCategoryById(id);
  },

  // Get category by name
  getCategoryByName: async (name) => {
    const response = await api.get(`/categories/name/${encodeURIComponent(name)}`);
    return response.data;
  },

  // Create new category
  createCategory: async (categoryData) => {
    const response = await api.post('/categories', categoryData);
    return response.data;
  },

  // Update category
  updateCategory: async (id, categoryData) => {
    const response = await api.put(`/categories/${id}`, categoryData);
    return response.data;
  },

  // Delete category
  deleteCategory: async (id) => {
    await api.delete(`/categories/${id}`);
  },
};

export default CategoryService;
