import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

/**
 * Guard that checks if the user is authenticated
 * @param route - The route that the user is trying to access
 * @param state - The state of the router
 * @returns True if the user is authenticated, false otherwise
 */
export const jwtGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService); // Inject the AuthService to check if the user is authenticated
  const router = inject(Router); // Inject the Router service to navigate to the login page if the user is not authenticated
  
  // Check if the user is authenticated
  if (authService.isAuthenticated()) {
    return true;
  }

  // Navigate to the login page if the user is not authenticated
  // and pass the current URL as a query parameter to redirect the user back to the original page after login
  router.navigate(['/login'], {
    queryParams: {
      redirect: state.url 
    }
  });
  return false;
};
