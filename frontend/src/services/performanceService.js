import api from './api';
import graphqlService from './graphqlService';

const PerformanceService = {
  // Benchmark REST API
  benchmarkREST: async (iterations = 10) => {
    const results = [];
    
    for (let i = 0; i < iterations; i++) {
      const start = performance.now();
      await api.get('/products/all');
      const end = performance.now();
      results.push(end - start);
    }
    
    return {
      times: results,
      average: results.reduce((a, b) => a + b, 0) / results.length,
      min: Math.min(...results),
      max: Math.max(...results),
    };
  },

  // Benchmark GraphQL
  benchmarkGraphQL: async (iterations = 10) => {
    const results = [];
    
    for (let i = 0; i < iterations; i++) {
      const start = performance.now();
      await graphqlService.getAllProducts();
      const end = performance.now();
      results.push(end - start);
    }
    
    return {
      times: results,
      average: results.reduce((a, b) => a + b, 0) / results.length,
      min: Math.min(...results),
      max: Math.max(...results),
    };
  },

  // Benchmark Categories REST
  benchmarkCategoriesREST: async (iterations = 10) => {
    const results = [];
    
    for (let i = 0; i < iterations; i++) {
      const start = performance.now();
      await api.get('/categories');
      const end = performance.now();
      results.push(end - start);
    }
    
    return {
      times: results,
      average: results.reduce((a, b) => a + b, 0) / results.length,
      min: Math.min(...results),
      max: Math.max(...results),
    };
  },

  // Benchmark Categories GraphQL
  benchmarkCategoriesGraphQL: async (iterations = 10) => {
    const results = [];
    
    for (let i = 0; i < iterations; i++) {
      const start = performance.now();
      await graphqlService.getAllCategories();
      const end = performance.now();
      results.push(end - start);
    }
    
    return {
      times: results,
      average: results.reduce((a, b) => a + b, 0) / results.length,
      min: Math.min(...results),
      max: Math.max(...results),
    };
  },

  // Run full comparison
  runFullBenchmark: async (iterations = 10) => {
    const productsREST = await PerformanceService.benchmarkREST(iterations);
    const productsGraphQL = await PerformanceService.benchmarkGraphQL(iterations);
    const categoriesREST = await PerformanceService.benchmarkCategoriesREST(iterations);
    const categoriesGraphQL = await PerformanceService.benchmarkCategoriesGraphQL(iterations);

    return {
      products: {
        rest: productsREST,
        graphql: productsGraphQL,
        winner: productsREST.average < productsGraphQL.average ? 'REST' : 'GraphQL',
        difference: Math.abs(productsREST.average - productsGraphQL.average).toFixed(2),
      },
      categories: {
        rest: categoriesREST,
        graphql: categoriesGraphQL,
        winner: categoriesREST.average < categoriesGraphQL.average ? 'REST' : 'GraphQL',
        difference: Math.abs(categoriesREST.average - categoriesGraphQL.average).toFixed(2),
      },
    };
  },
};

export default PerformanceService;
