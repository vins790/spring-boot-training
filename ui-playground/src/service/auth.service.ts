import {useApiClient} from "../api/api/hooks/useApiClient.hook.ts";

export const useAuthService = () => {
  const apiClient = useApiClient();

  return {
    auth: async (args: { username: string, password: string }): Promise<boolean> => {
      try {
        const { token } = await apiClient.post<{ token: string }>("/auth/login", args);
        sessionStorage.setItem('access_token', token);
        return true;
      } catch (error) {
        console.error('Login failed:', error);
        return false;
      }
    }
  }
}