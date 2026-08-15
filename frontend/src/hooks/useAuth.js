import { useAuthContext } from '../context/AuthContext';

/**
 * Custom Hook: useAuth
 *
 * Exposes authentication methods and user session details.
 */
export const useAuth = () => {
  return useAuthContext();
};
