import type {InternalAxiosRequestConfig} from "axios";

export const onRequestFulfilledInterceptorFactory =
  () => (config: InternalAxiosRequestConfig) => {
    if (config.url?.includes('/auth/')) return config;
    const token = sessionStorage.getItem("access_token");
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
  }



