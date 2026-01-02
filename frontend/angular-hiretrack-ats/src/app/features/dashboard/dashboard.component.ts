import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { DashboardService } from '../../core/services/dashboard.service';
import { DashboardData } from '../../core/models/dashboard';
import { AuthService } from '../../core/services/auth.service';
import { User } from '../../core/models/user';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit, OnDestroy {
  dashboardData: DashboardData | null = null;
  loading = true;
  error: string | null = null;
  currentUser: User | null = null;
  
  private destroy$ = new Subject<void>();

  constructor(
    private dashboardService: DashboardService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.currentUserValue;
    this.loadDashboardData();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadDashboardData(): void {
    this.loading = true;
    this.error = null;

    this.dashboardService.getDashboardData()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (data) => {
          this.dashboardData = data;
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Failed to load dashboard data. Please try again later.';
          this.loading = false;
          console.error('Dashboard error:', err);
        }
      });
  }

  getStatusColor(status: string): string {
    const statusColors: { [key: string]: string } = {
      'PUBLISHED': 'status-published',
      'DRAFT': 'status-draft',
      'ON_HOLD': 'status-on-hold',
      'CLOSED': 'status-closed',
      'APPLIED': 'status-applied',
      'SCREENING': 'status-screening',
      'INTERVIEW': 'status-interview',
      'OFFER': 'status-offer',
      'HIRED': 'status-hired',
      'REJECTED': 'status-rejected'
    };
    return statusColors[status] || 'status-default';
  }

  getMatchScoreColor(score: number): string {
    if (score >= 90) return 'score-excellent';
    if (score >= 80) return 'score-good';
    if (score >= 70) return 'score-fair';
    return 'score-poor';
  }

  getActivityIcon(type: string): string {
    const icons: { [key: string]: string } = {
      'JOB_CREATED': '📝',
      'JOB_UPDATED': '✏️',
      'CANDIDATE_APPLIED': '👤',
      'CANDIDATE_MATCHED': '🎯',
      'INTERVIEW_SCHEDULED': '📅',
      'OFFER_SENT': '💼',
      'CANDIDATE_HIRED': '🎉'
    };
    return icons[type] || '📌';
  }

  formatDate(date: Date): string {
    const d = new Date(date);
    const now = new Date();
    const diffTime = Math.abs(now.getTime() - d.getTime());
    const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));

    if (diffDays === 0) {
      const diffHours = Math.floor(diffTime / (1000 * 60 * 60));
      if (diffHours === 0) {
        const diffMinutes = Math.floor(diffTime / (1000 * 60));
        return diffMinutes <= 1 ? 'Just now' : `${diffMinutes} minutes ago`;
      }
      return `${diffHours} hour${diffHours > 1 ? 's' : ''} ago`;
    } else if (diffDays === 1) {
      return 'Yesterday';
    } else if (diffDays < 7) {
      return `${diffDays} days ago`;
    } else {
      return d.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
    }
  }

  getInitials(firstName: string, lastName: string): string {
    return `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase();
  }

  logout(): void {
    this.authService.logout();
  }
}

