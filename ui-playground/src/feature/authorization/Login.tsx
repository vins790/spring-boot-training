import {useRef, useEffect, useCallback} from "react";
import {useNavigate} from "react-router-dom";
import {useAuthService} from "../../service/auth.service.ts";

export const Login = () => {
  const authService = useAuthService();
  const navigate = useNavigate();

  const usernameRef = useRef<HTMLInputElement>(null);
  const passwordRef = useRef<HTMLInputElement>(null);

  const onAuthorize = useCallback(() => navigate('/fib'), [navigate]);

  const onLogin = useCallback(async () => {
    const username = usernameRef?.current?.value;
    const password = passwordRef?.current?.value;

    if (!username || !password) return;

    const success = await authService.auth({username, password});
    if (success) onAuthorize();
  }, [authService, onAuthorize]);

  useEffect(() => {
    const handleGlobalKeyPress = (event: KeyboardEvent) => {
      if (event.key === 'Enter') onLogin();
    };
    document.addEventListener('keydown', handleGlobalKeyPress);
    return () => document.removeEventListener('keydown', handleGlobalKeyPress);
  }, [onLogin]);

  useEffect(() => {
    if(sessionStorage.getItem('access_token')) onAuthorize();
  }, [onAuthorize])


  return <div className="card">
    <h1>Login</h1>
    <div className="input-group input-group--vertical">
      <input ref={usernameRef} />
      <input type="password" ref={passwordRef} />
    </div>
    <button className="login-button" onClick={onLogin}>
      Login
    </button>
  </div>
}