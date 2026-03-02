import type { AxiosInstance } from "axios";
import type { RetryConfig } from "../types/RetryConfig.type.ts";

let refreshPromise: Promise<void> | null = null;

const createRefreshToken = (apiClient: AxiosInstance) => async (): Promise<string> => {
    const { data: { token } } = await apiClient.post("/auth/refresh", {}, {
      refreshLoopGuard: true,
      headers: { Authorization: undefined }
    } as RetryConfig);
   return token;
 }

export const refreshUtils = (apiClient: AxiosInstance, refreshToken?: () => Promise<string>) => {
  const refreshTokenFn = refreshToken || createRefreshToken(apiClient);

  const saveToken = (token: string) => {
    sessionStorage.setItem("access_token", token);
  }

  const redirectToLogin = () => {
    sessionStorage.removeItem("access_token");
    window.location.href = '/login';
  }

  const refresh = async (): Promise<void> => {
    if (refreshPromise) return refreshPromise;

    refreshPromise = (async () => {
      try {
        const token = await refreshTokenFn();
        saveToken(token);
      } catch {
        redirectToLogin();
        throw new Error("Token refresh failed");
      } finally {
        refreshPromise = null;
      }
    })();

    return refreshPromise;
  }

  const shouldRetryWithCurrentToken = (config: RetryConfig): boolean => {
    const currentToken = sessionStorage.getItem("access_token");
    return !!(config.accessToken && currentToken && config.accessToken !== currentToken);
  }

  const retryRequest = (config: RetryConfig) => {
    config.retry = true;
    return apiClient(config);
  }

  const handleUnauthorized = async (config: RetryConfig): Promise<unknown> => {
    if (config.refreshLoopGuard) {
      return Promise.reject(new Error("Refresh loop detected"));
    }

    if (config.retry) {
      return Promise.reject(new Error("Already retried"));
    }

    if (shouldRetryWithCurrentToken(config)) {
      return retryRequest(config);
    }

    await refresh();
    return retryRequest(config);
  }

  return { refresh, handleUnauthorized }
}