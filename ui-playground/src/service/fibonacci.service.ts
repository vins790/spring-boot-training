import {useApiClient} from "../api/api/hooks/useApiClient.hook.ts";

export const useFibonacciService = () => {
  const apiClient = useApiClient();

  return {
    getNth: async (index: number) => {
      return apiClient.get<number>(`/fib/${index}`);
    }
  }
}