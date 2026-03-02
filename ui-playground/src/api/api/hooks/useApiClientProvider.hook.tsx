import {createApiClient} from "../apiClient.ts";
import { ApiClientContext } from "../context/apiClient.context.tsx";
import {type PropsWithChildren, useMemo} from "react";

export const useApiClientProviderHook = () => {
  const apiClient = useMemo(() => createApiClient(), [])
  return {
    ApiClientProvider: ({children}: PropsWithChildren) =>
      <ApiClientContext.Provider value={apiClient}>
        {children}
      </ApiClientContext.Provider>
  }
}