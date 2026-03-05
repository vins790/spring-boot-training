import {useContext} from "react";
import {ApiClientContext} from "../context/apiClient.context.tsx";

export function useApiClient() {
  const client = useContext(ApiClientContext);
  if (!client) throw new Error("HttpClient not initialized");
  return client;
}
