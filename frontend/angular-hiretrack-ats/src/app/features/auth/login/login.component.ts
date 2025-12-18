import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';
import { response } from 'express';
import { LoginRequest } from '../../../core/models/user';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  loginForm! : FormGroup;
  errorMessage : string = '';
  loading : boolean = false;
  
  constructor(
    private formBuilder: FormBuilder, // FormBuilder helps to create forms
    private authService: AuthService,
    private router: Router
  ){}

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.initializeForm(); // this runs when the component is created

    // if user is already logged in, redirect to the dashboard
    if(this.authService.isAuthenticated()){
      this.router.navigate(['/dashboard']);
    }
  }

  private initializeForm() : void {
    this.loginForm = this.formBuilder.group({
      email : ['', [
        Validators.required,
        Validators.email
      ]],
      password : ['', [
        Validators.required,
        Validators.minLength(6)
      ]]
    });
  }

  onSubmit(): void {

    // Check if the form is invalid
    if(this.loginForm.invalid){
      // Mark all form controls as touched to display validation errors
      this.loginForm.markAllAsTouched();
      return;
    }

    // Clear previous error message
    this.errorMessage = '';
    this.loading = true;

    // Form is valid, get the data
    const credentials = this.loginForm.value;

    // Call auth service
    this.authService.login(credentials as LoginRequest).subscribe({
      next: (response) => {
        // If login is successful, redirect to the dashboard
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        // If there is an error, display the error message
        this.loading = false;
        this.errorMessage = err.error?.message || 'Login failed. Please try again.';
      },
      complete: () => {
        // if the login is complete, stop the loading spinner
        this.loading = false;
      }
    })
  }

  /**
   * Checks if a field has an error
   * @param fieldName - The name of the field to check
   * @param errorType - The type of error to check for
   * @returns true if the field has an error, false otherwise
   */
  hasError(fieldName: string, errorType: string): boolean{
    const field = this.loginForm.get(fieldName);
    return !!(
      field?.hasError(errorType) && (field?.dirty || field?.touched)
    )
  }
}
