import {createContext} from "react";
import type {ApiClient} from "../apiClient.ts";

export const ApiClientContext = createContext<ApiClient | null>(null);
