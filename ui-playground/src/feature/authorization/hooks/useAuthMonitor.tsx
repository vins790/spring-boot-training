import {useEffect, useState} from "react";

export const useAuthMonitor = () => {

  const [isAuthenticated, setIsAuthenticated] = useState(!!sessionStorage.getItem('access_token'));

  const checkAuth = () => {
    setIsAuthenticated(!!sessionStorage.getItem('access_token'));
  };

  useEffect(() => {
    const interval = setInterval(checkAuth, 200);

    window.addEventListener('storage', checkAuth);
    return () => {
      window.removeEventListener('storage', checkAuth);
      clearInterval(interval);
    };
  }, []);

  return {
    isAuthenticated
  }
}