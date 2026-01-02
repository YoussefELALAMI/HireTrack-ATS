export interface Job {
  id: string;
  title: string;
  department: string;
  location: string;
  status: JobStatus;
  totalCandidates: number;
  matchedCandidates: number;
  createdAt: Date;
  updatedAt: Date;
}

export enum JobStatus {
  DRAFT = 'DRAFT',
  PUBLISHED = 'PUBLISHED',
  CLOSED = 'CLOSED',
  ON_HOLD = 'ON_HOLD'
}

export interface Candidate {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  resumeUrl?: string;
  matchScore: number;
  status: CandidateStatus;
  jobId: string;
  jobTitle: string;
  appliedAt: Date;
  lastActivity: Date;
}

export enum CandidateStatus {
  APPLIED = 'APPLIED',
  SCREENING = 'SCREENING',
  INTERVIEW = 'INTERVIEW',
  OFFER = 'OFFER',
  HIRED = 'HIRED',
  REJECTED = 'REJECTED'
}

export interface DashboardMetrics {
  totalJobs: number;
  activeJobs: number;
  totalCandidates: number;
  newCandidatesToday: number;
  averageMatchScore: number;
  interviewsScheduled: number;
  offersPending: number;
  timeToHire: number; // in days
}

export interface Activity {
  id: string;
  type: ActivityType;
  description: string;
  userId: string;
  userName: string;
  timestamp: Date;
  relatedEntityId?: string;
  relatedEntityType?: string;
}

export enum ActivityType {
  JOB_CREATED = 'JOB_CREATED',
  JOB_UPDATED = 'JOB_UPDATED',
  CANDIDATE_APPLIED = 'CANDIDATE_APPLIED',
  CANDIDATE_MATCHED = 'CANDIDATE_MATCHED',
  INTERVIEW_SCHEDULED = 'INTERVIEW_SCHEDULED',
  OFFER_SENT = 'OFFER_SENT',
  CANDIDATE_HIRED = 'CANDIDATE_HIRED'
}

export interface DashboardData {
  metrics: DashboardMetrics;
  recentJobs: Job[];
  topCandidates: Candidate[];
  recentActivities: Activity[];
  jobStatusDistribution: { status: string; count: number }[];
  candidateStatusDistribution: { status: string; count: number }[];
}

