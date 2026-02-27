import type {AxiosInstance, InternalAxiosRequestConfig} from "axios";

interface RetryConfig extends InternalAxiosRequestConfig {
  _retry?: boolean;
  skipAuthRefresh?: boolean;
}

export const onResponseRejectedInterceptorFactory = (http: AxiosInstance) => {
  let isRefreshing = false;
  let queue: Array<(token: string) => void> = [];

  return async (error: { config: RetryConfig, response: { status: number } }) => {
    const original = error.config as RetryConfig;

    if (!original) {
      return Promise.reject(error);
    }

    if (original.skipAuthRefresh) {
      return Promise.reject(error);
    }

    if (error.response?.status === 401 && !original._retry) {
      original._retry = true;

      if (!isRefreshing) {
        isRefreshing = true;

        try {
          const res = await http.post("/auth/refresh", {}, {
            skipAuthRefresh: true,
            headers: {
              Authorization: undefined
            }
          } as RetryConfig);

          const newToken = res.data.token;

          sessionStorage.setItem("access_token", newToken);
          http.defaults.headers.Authorization = `Bearer ${newToken}`;

          queue.forEach((cb) => cb(newToken));
          queue = [];

          if (original.headers) {
            original.headers.Authorization = `Bearer ${newToken}`;
          }
          return http(original);

        } catch (refreshError) {
          queue = [];
          sessionStorage.removeItem("access_token");
          window.location.href = '/login';
          return Promise.reject(refreshError);
        } finally {
          isRefreshing = false;
        }
      }

      return new Promise((resolve) => {
        queue.push((token) => {
          if (original.headers) {
            original.headers.Authorization = `Bearer ${token}`;
          }
          resolve(http(original));
        });
      });
    }

    return Promise.reject(error);
  }
}