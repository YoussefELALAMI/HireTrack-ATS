import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl, 
  ValidationErrors } from '@angular/forms'; 
import { Router } from '@angular/router'; 
import { CommonModule } from '@angular/common'; 
import { RouterModule } from '@angular/router'; 
import { AuthService } from '../../../core/services/auth.service';
import { RegisterRequest, roles as Roles } from '../../../core/models/user';
import intlTelInput from 'intl-tel-input';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
  @ViewChild('phoneInput', { static: false }) phoneInput!: ElementRef;
  registerForm! : FormGroup;
  errorMessage : string = '';
  loading : boolean = false;
  roles = Object.values(Roles) as Roles[];
  roleLabels: Record<Roles, string> = {
    [Roles.ADMIN]: 'Administrator',
    [Roles.RECRUITER]: 'Recruiter',
    [Roles.HIRING_MANAGER]: 'Hiring Manager',
    [Roles.INTERVIEWER]: 'Interviewer'
  };  

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ){}

  ngOnInit(): void {
    this.initializeForm();
  }

  ngAfterViewInit() {
    const input = this.phoneInput.nativeElement;
    
    const options = {
      initialCountry: "fr",
      separateDialCode: true
    } as Partial<Parameters<typeof intlTelInput>[1]>;
    
    intlTelInput(input, options);
  }

  private initializeForm(): void {
    this.registerForm = this.formBuilder.group({
      email : ['', [
        Validators.required,
        Validators.email
      ]],
      password : ['', [
        Validators.required,
        this.passwordStrengthValidator
      ]],
      confirmPassword : ['', [
        Validators.required
      ]],
      firstName : ['', [Validators.required]],
      lastName : ['', [Validators.required]],
      role : ['', [Validators.required]],
      companyName : ['', [Validators.required]],
      phoneNumber : ['', [Validators.required, Validators.pattern(/^\+?[1-9]\d{1,14}$/)]]
    }, {
      validator: this.passwordMatchValidator
    });
  }

  private passwordStrengthValidator(control: AbstractControl) : ValidationErrors | null {
    const password = control.value;

    if(!password) return null;

    const hasUpper = /[A-Z]/.test(password);
    const hasLower = /[a-z]/.test(password);
    const hasNumber = /\d/.test(password);
    const hasSpecialCharacter = /[^a-zA-Z0-9]/.test(password);
    const hasMinLength = /^.{8,}$/.test(password);

    const isStrong = hasUpper && hasLower && hasNumber && hasSpecialCharacter && hasMinLength;

    if(!isStrong) {
      control.get('password')?.setErrors({ passwordWeak : true });
      return { passwordWeak : true };
    }
    return null;
  }

  private passwordMatchValidator(control: AbstractControl) : ValidationErrors | null {
    const password = control.get('password')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;
    if(password && confirmPassword && (confirmPassword !== password)){
      control.get('confirmPassword')?.setErrors({ passwordMismatch : true });
      return { passwordMisMatch : true };
    }
    return null;
  }

  onSubmit(): void {
    if(this.registerForm.invalid){
      this.registerForm.markAllAsTouched();
      return;
    }

    this.errorMessage = '';
    this.loading = true;

    // Remove confirmPassword before sending (not needed by API)
    const { confirmPassword, ...userData} = this.registerForm.value;
    
    this.authService.register(userData as RegisterRequest).subscribe({
      next: (response) => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.message || 'Registration failed. Please try again.';
      },
      complete: () => {
        this.loading = false;
      }
    });
  }

  hasError(fieldName: string, errorType: string): boolean {
    const field = this.registerForm.get(fieldName);
    return !!(
      field?.hasError(errorType) && (field?.dirty || field?.touched)
    )
  }
}