import { HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';

/**
 * Interceptor that adds the JWT token to the request headers
 * @param req - The HTTP request to intercept
 * @param next - The next HTTP interceptor to pass the request to
 * @returns - The HTTP response from the next interceptor
 */
export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getAccessToken();
  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }
  // Pass the modified request to the next handler
  return next(req);
};
