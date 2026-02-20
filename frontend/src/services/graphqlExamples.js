import { graphqlQuery } from './api';

// PUBLIC QUERY - No token required
export const getCategories = async () => {
  const query = `
    query {
      categories {
        categoryId
        name
        description
      }
    }
  `;
  return await graphqlQuery(query);
};

// PROTECTED QUERY - Requires JWT token
export const getProducts = async (page = 0, size = 10) => {
  const query = `
    query GetProducts($page: Int, $size: Int) {
      products(page: $page, size: $size) {
        content {
          productId
          name
          description
          price
          categoryId
        }
        totalElements
        totalPages
      }
    }
  `;
  return await graphqlQuery(query, { page, size });
};

// PROTECTED MUTATION - Requires JWT token
export const createProduct = async (input) => {
  const mutation = `
    mutation CreateProduct($input: CreateProductInput!) {
      createProduct(input: $input) {
        productId
        name
        price
      }
    }
  `;
  return await graphqlQuery(mutation, { input });
};
