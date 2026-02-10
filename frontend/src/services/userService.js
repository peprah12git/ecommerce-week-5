import api from './api';

const UserService = {
  // Register new user
  register: async (userData) => {
    const response = await api.post('/users', userData);
    return response.data;
  },
};

export default UserService;
