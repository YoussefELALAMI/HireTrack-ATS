import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';

/**
 * Guard that checks if the user is not authenticated
 * @param route - The route that the user is trying to access
 * @param state - The state of the router
 * @returns True if the user is not authenticated, false otherwise
 */
export const guestGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService); // Inject the AuthService to check if the user is authenticated
  const router = inject(Router); // Inject the Router service to navigate to the dashboard page if the user is authenticated

  if (!authService.isAuthenticated()) {
    return true;
  }
  
  // Navigate to the dashboard page if the user is authenticated
  router.navigate(['/dashboard']);
  return false;
};
