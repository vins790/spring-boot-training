import type { AxiosInstance } from "axios";
import type { RetryConfig } from "../types/RetryConfig.type.ts";
import { refreshUtils } from "../utils/refresh.utils.ts";

export const onResponseRejectedInterceptorFactory = (apiClient: AxiosInstance) => {
  const { handleUnauthorized } = refreshUtils(apiClient);

  return async (error: { config: RetryConfig, response: { status: number } }) => {
    const requestConfig = error.config;

    if (!requestConfig) return Promise.reject(error);
    if (error.response?.status === 401) return handleUnauthorized(requestConfig);

    return Promise.reject(error);
  }
}