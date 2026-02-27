import './App.css'
import {useApiClientProviderHook} from "./api/api/hooks/useApiClientProvider.hook.tsx";
import {Fibonacci} from "./feature/fibonacci/Fibonacci.tsx";
import {BrowserRouter, Route, Routes, Navigate} from "react-router-dom";
import {Login} from "./feature/authorization/Login.tsx";
import {ProtectedRoute} from "./feature/authorization/ProtectedRoute.tsx";
import {useAuthMonitor} from "./feature/authorization/hooks/useAuthMonitor.tsx";

function App() {
  const { ApiClientProvider } = useApiClientProviderHook();
  const { isAuthenticated } = useAuthMonitor();

  return (
    <ApiClientProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<Login/>}/>
          <Route path="/fib" element={
            <ProtectedRoute isAuthenticated={isAuthenticated}>
              <Fibonacci/>
            </ProtectedRoute>
          }/>
          <Route path="/" element={<Navigate to="/login" replace />}/>
          <Route path="*" element={<Navigate to="/login" replace />}/>
        </Routes>
      </BrowserRouter>
    </ApiClientProvider>
  )
}

export default App
