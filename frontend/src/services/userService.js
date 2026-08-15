import { userApi } from '../api/userApi';

export const userService = {
  getAllUsers: async (pageNo, pageSize) => {
    return await userApi.getAllUsers(pageNo, pageSize);
  },

  getUserById: async (id) => {
    return await userApi.getUserById(id);
  },

  assignRole: async (userId, roleName) => {
    return await userApi.assignRole(userId, roleName);
  },

  deleteUser: async (id) => {
    return await userApi.deleteUser(id);
  },
};
