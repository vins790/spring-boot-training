import type {InternalAxiosRequestConfig} from "axios";

export type RetryConfig = InternalAxiosRequestConfig & {
  retry?: boolean;
  refreshLoopGuard?: boolean;
  accessToken?: string | null;
}