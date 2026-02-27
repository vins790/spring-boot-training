import axios from "axios";
import {onRequestFulfilledInterceptorFactory} from "./interceptors/onRequestFulfilled.interceptor.ts";
import {onRequestRejectedInterceptorFactory} from "./interceptors/onRequestRejected.interceptor.ts";
import {onResponseFulfilledInterceptorFactory} from "./interceptors/onResponseFulfilled.interceptor.ts";
import {onResponseRejectedInterceptorFactory} from "./interceptors/onResponseRejected.interceptor.ts";

export type ApiClient = ReturnType<typeof createApiClient>;

export const createApiClient = () => {
  const instance = axios.create({
    baseURL: "http://localhost:8080/api",
    withCredentials: true,
  });

  instance.interceptors.request.use(
    onRequestFulfilledInterceptorFactory(),
    onRequestRejectedInterceptorFactory()
  );

  instance.interceptors.response.use(
    onResponseFulfilledInterceptorFactory(),
    onResponseRejectedInterceptorFactory(instance)
  );

  return {
    get: <T>(url: string, signal?: AbortSignal) =>
      instance.get<T>(url, {signal}).then((r) => r.data),

    post: <T>(url: string, body?: unknown, signal?: AbortSignal) =>
      instance.post<T>(url, body, {signal}).then((r) => r.data),

    put: <T>(url: string, body?: unknown, signal?: AbortSignal) =>
      instance.put<T>(url, body, {signal}).then((r) => r.data),

    delete: <T>(url: string, signal?: AbortSignal) =>
      instance.delete<T>(url, {signal}).then((r) => r.data)
  };
}