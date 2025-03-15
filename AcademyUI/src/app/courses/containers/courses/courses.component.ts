import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatDialog } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { ActivatedRoute, Router } from '@angular/router';
import { catchError, Observable, of } from 'rxjs';
import {
  ConfirmationDialogComponent
} from '../../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { ErrorDialogComponent } from '../../../shared/components/error-diolog/error-dialog.component';
import { CoursesListComponent } from '../../components/courses-list/courses-list.component';
import { Course } from '../../model/course';
import { CoursesService } from '../../service/courses.service';

@Component({
  selector: 'app-courses',
  imports: [
    MatTableModule,
    MatCardModule,
    MatToolbarModule,
    MatProgressSpinnerModule,
    CommonModule,
    MatIconModule,
    CoursesListComponent
  ],
  templateUrl: './courses.component.html',
  styleUrl: './courses.component.scss'
})
export class CoursesComponent {

  courses$: Observable<Course[]> = of([]);

  constructor(
    private courseService: CoursesService,
    public dialog: MatDialog,
    private router: Router,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {
    this.refresh()
  }

  onError(errorMsg: string) {
    this.dialog.open(ErrorDialogComponent, {
      data: errorMsg
    });
  }

  onAdd() {
    this.router.navigate(['new'], { relativeTo: this.route });
  }

  onEdit(record: Course) {
    this.router.navigate(['edit', record.id], { relativeTo: this.route });
  }

  refresh() {
    this.courses$ = this.courseService.list()
      .pipe(
        catchError(error => {
          this.onError('Error loading courses.');
          return of([]);
        })
      );
  }

  onRemove(record: Course) {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      data: 'Are you sure you want to remove this course?',
    });

    dialogRef.afterClosed().subscribe((result: boolean) => {
      if (result) {
        this.courseService.remove(record.id)
          .subscribe({
            next: () => {
              this.refresh();
              this.snackBar.open('Course removed.', 'Close', {
                duration: 5000,
                verticalPosition: 'top',
                horizontalPosition: 'center'
              })
            },
            error: () => {
              this.onError('Error removing course.');
            }
          });
      }
    });
  }
}
