// Brain of the authentication system
import { Injectable } from '@angular/core';
import { User, LoginRequest, RegisterRequest, AuthResponse } from '../models/user';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, catchError, Observable, tap, throwError } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = '/api/auth';
  private accessToken: string | null = null;
  
  // BehaviorSubject holds the current logged-in user state. It stores the latest user and emits it to all subscribers.
  private currentUserSubject: BehaviorSubject<User | null> = new BehaviorSubject<User | null>(null);
  currentUser$: Observable<User | null>;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    const storedUser = localStorage.getItem('currentUser'); // Check if user is already logged in
    const user = storedUser ? JSON.parse(storedUser) : null;

    this.currentUserSubject = new BehaviorSubject<User | null>(user);
    this.currentUser$ = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  /**
   * Login Method
   * This method is used to login a user. It sends a POST request to the backend API to authenticate the user.
   * If the login is successful, it stores the user in the local storage and updates the current user state.
   * If the login fails, it throws an error.
   * @param credentials - The login credentials (email and password)
   * @returns An Observable that emits the AuthResponse
   */
  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials)
      .pipe(
        tap((response: AuthResponse) => { // tap is used to perform side effects (e.g. logging, storing data) without affecting the observable chain
          // Store the user in the local storage
          localStorage.setItem('currentUser', JSON.stringify(response.user));
          // Update the current user state
          this.currentUserSubject.next(response.user);
        }),
        catchError((error: any) => {
          // Log the error
          console.error('Login failed:', error);
          // Return an observable with the error
          return throwError(() => error);
        })
      );
  }

  /**
   * Register Method
   * This method is used to register a new user. It sends a POST request to the backend API to create a new user.
   * If the registration is successful, it stores the user in the local storage and updates the current user state.
   * If the registration fails, it throws an error.
   * @param user - The user to register
   * @returns An Observable that emits the AuthResponse
   */
  register(user: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, user)
      .pipe(
        tap((response: AuthResponse) => {
          // Store the user in the local storage
          localStorage.setItem('currentUser', JSON.stringify(response.user));
          // Update the current user state
          this.currentUserSubject.next(response.user);
        }),
        catchError((error: any) => {
          // Log the error
          console.error('Registration failed:', error);
          // Return an observable with the error
          return throwError(() => error);
        })
      );
  }

  /**
   * Logout Method
   * This method is used to logout a user. It removes the user from the local storage and updates the current user state.
   * It also navigates to the login page.
   */
  logout(): void {
    // Remove the user from the local storage
    localStorage.removeItem('currentUser');
    localStorage.removeItem('accessToken');
    // Update the current user state to notify all subscribers that the user is logged out
    this.currentUserSubject.next(null);
    // Navigate to the login page
    this.router.navigate(['/login']);
  }

  /**
   * Get Access Token Method
   * This method is used to get the access token from the local storage.
   * @returns The access token
   */
  getAccessToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  /**
   * Check if the user is authenticated Method
   * This method is used to check if the user is authenticated.
   * @returns True if the user is authenticated, false otherwise
   */
  isAuthenticated(): boolean {
    return this.getAccessToken() !== null;
  }
}
