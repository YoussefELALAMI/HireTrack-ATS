export interface User {
    id: string;
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    role: string;
    companyName: string;
    phoneNumber: string;
    createdAt: Date;
    updatedAt: Date;
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface RegisterRequest {
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    role: string;
    companyName: string;
    phoneNumber: string;
}

export interface AuthResponse {
    token: string;
    tokenType: string;
    user: User;
}

export enum roles {
  ADMIN = 'ADMIN',
  RECRUITER = 'RECRUITER',
  HIRING_MANAGER = 'HIRING_MANAGER',
  INTERVIEWER = 'INTERVIEWER'
}