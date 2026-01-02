import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { delay } from 'rxjs/operators';
import { DashboardData, DashboardMetrics, Job, Candidate, Activity } from '../models/dashboard';
import { JobStatus, CandidateStatus, ActivityType } from '../models/dashboard';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private apiUrl = '/api/dashboard';

  constructor(private http: HttpClient) {}

  /**
   * Get dashboard data
   * For now, returns mock data. Replace with actual API call when backend is ready.
   */
  getDashboardData(): Observable<DashboardData> {
    // TODO: Replace with actual API call
    // return this.http.get<DashboardData>(`${this.apiUrl}/overview`);

    // Mock data for development
    const mockData: DashboardData = {
      metrics: {
        totalJobs: 24,
        activeJobs: 18,
        totalCandidates: 342,
        newCandidatesToday: 12,
        averageMatchScore: 78.5,
        interviewsScheduled: 8,
        offersPending: 3,
        timeToHire: 14.5
      },
      recentJobs: [
        {
          id: '1',
          title: 'Senior Full Stack Developer',
          department: 'Engineering',
          location: 'Remote',
          status: JobStatus.PUBLISHED,
          totalCandidates: 45,
          matchedCandidates: 32,
          createdAt: new Date('2024-01-15'),
          updatedAt: new Date('2024-01-20')
        },
        {
          id: '2',
          title: 'Product Manager',
          department: 'Product',
          location: 'San Francisco, CA',
          status: JobStatus.PUBLISHED,
          totalCandidates: 28,
          matchedCandidates: 19,
          createdAt: new Date('2024-01-18'),
          updatedAt: new Date('2024-01-22')
        },
        {
          id: '3',
          title: 'UX Designer',
          department: 'Design',
          location: 'New York, NY',
          status: JobStatus.PUBLISHED,
          totalCandidates: 67,
          matchedCandidates: 48,
          createdAt: new Date('2024-01-10'),
          updatedAt: new Date('2024-01-19')
        },
        {
          id: '4',
          title: 'DevOps Engineer',
          department: 'Engineering',
          location: 'Remote',
          status: JobStatus.ON_HOLD,
          totalCandidates: 15,
          matchedCandidates: 11,
          createdAt: new Date('2024-01-12'),
          updatedAt: new Date('2024-01-21')
        },
        {
          id: '5',
          title: 'Data Scientist',
          department: 'Data',
          location: 'Boston, MA',
          status: JobStatus.PUBLISHED,
          totalCandidates: 34,
          matchedCandidates: 26,
          createdAt: new Date('2024-01-20'),
          updatedAt: new Date('2024-01-23')
        }
      ],
      topCandidates: [
        {
          id: '1',
          firstName: 'Sarah',
          lastName: 'Johnson',
          email: 'sarah.johnson@email.com',
          phoneNumber: '+1-555-0123',
          matchScore: 94,
          status: CandidateStatus.INTERVIEW,
          jobId: '1',
          jobTitle: 'Senior Full Stack Developer',
          appliedAt: new Date('2024-01-19'),
          lastActivity: new Date('2024-01-23')
        },
        {
          id: '2',
          firstName: 'Michael',
          lastName: 'Chen',
          email: 'michael.chen@email.com',
          phoneNumber: '+1-555-0124',
          matchScore: 91,
          status: CandidateStatus.OFFER,
          jobId: '2',
          jobTitle: 'Product Manager',
          appliedAt: new Date('2024-01-18'),
          lastActivity: new Date('2024-01-22')
        },
        {
          id: '3',
          firstName: 'Emily',
          lastName: 'Rodriguez',
          email: 'emily.rodriguez@email.com',
          phoneNumber: '+1-555-0125',
          matchScore: 89,
          status: CandidateStatus.SCREENING,
          jobId: '3',
          jobTitle: 'UX Designer',
          appliedAt: new Date('2024-01-20'),
          lastActivity: new Date('2024-01-23')
        },
        {
          id: '4',
          firstName: 'David',
          lastName: 'Kim',
          email: 'david.kim@email.com',
          phoneNumber: '+1-555-0126',
          matchScore: 87,
          status: CandidateStatus.INTERVIEW,
          jobId: '1',
          jobTitle: 'Senior Full Stack Developer',
          appliedAt: new Date('2024-01-21'),
          lastActivity: new Date('2024-01-23')
        },
        {
          id: '5',
          firstName: 'Jessica',
          lastName: 'Williams',
          email: 'jessica.williams@email.com',
          phoneNumber: '+1-555-0127',
          matchScore: 85,
          status: CandidateStatus.APPLIED,
          jobId: '5',
          jobTitle: 'Data Scientist',
          appliedAt: new Date('2024-01-22'),
          lastActivity: new Date('2024-01-23')
        }
      ],
      recentActivities: [
        {
          id: '1',
          type: ActivityType.CANDIDATE_APPLIED,
          description: 'New candidate applied for Senior Full Stack Developer',
          userId: 'user1',
          userName: 'Sarah Johnson',
          timestamp: new Date('2024-01-23T10:30:00'),
          relatedEntityId: '1',
          relatedEntityType: 'candidate'
        },
        {
          id: '2',
          type: ActivityType.INTERVIEW_SCHEDULED,
          description: 'Interview scheduled with Michael Chen for Product Manager',
          userId: 'user2',
          userName: 'Recruiting Team',
          timestamp: new Date('2024-01-23T09:15:00'),
          relatedEntityId: '2',
          relatedEntityType: 'candidate'
        },
        {
          id: '3',
          type: ActivityType.CANDIDATE_MATCHED,
          description: 'High match score (94%) for Sarah Johnson',
          userId: 'system',
          userName: 'AI System',
          timestamp: new Date('2024-01-23T08:45:00'),
          relatedEntityId: '1',
          relatedEntityType: 'candidate'
        },
        {
          id: '4',
          type: ActivityType.JOB_CREATED,
          description: 'New job posted: Data Scientist',
          userId: 'user3',
          userName: 'Hiring Manager',
          timestamp: new Date('2024-01-22T16:20:00'),
          relatedEntityId: '5',
          relatedEntityType: 'job'
        },
        {
          id: '5',
          type: ActivityType.OFFER_SENT,
          description: 'Offer sent to Michael Chen',
          userId: 'user2',
          userName: 'Recruiting Team',
          timestamp: new Date('2024-01-22T14:10:00'),
          relatedEntityId: '2',
          relatedEntityType: 'candidate'
        }
      ],
      jobStatusDistribution: [
        { status: 'Published', count: 18 },
        { status: 'Draft', count: 4 },
        { status: 'On Hold', count: 2 },
        { status: 'Closed', count: 0 }
      ],
      candidateStatusDistribution: [
        { status: 'Applied', count: 156 },
        { status: 'Screening', count: 78 },
        { status: 'Interview', count: 45 },
        { status: 'Offer', count: 12 },
        { status: 'Hired', count: 38 },
        { status: 'Rejected', count: 13 }
      ]
    };

    return of(mockData).pipe(delay(500)); // Simulate API delay
  }
}

