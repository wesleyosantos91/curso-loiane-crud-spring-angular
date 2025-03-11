import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatDialog } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { catchError, Observable, of } from 'rxjs';
import { ErrorDialogComponent } from '../../shared/components/error-diolog/error-dialog.component';
import { CategoryPipe } from '../../shared/pipes/category.pipe';
import { Course } from '../model/course';
import { CoursesService } from '../service/courses.service';

@Component({
  selector: 'app-courses',
  imports: [MatTableModule, MatCardModule, MatToolbarModule, MatProgressSpinner, CommonModule, MatIconModule, CategoryPipe],
  templateUrl: './courses.component.html',
  styleUrl: './courses.component.scss'
})
export class CoursesComponent {

  courses$: Observable<Course[]>;
  displayedColumns: string[] = ['name', 'category'];

  constructor(
    private courseService: CoursesService,
    public dialog: MatDialog,
  ) {
    this.courses$ = courseService.list()
      .pipe(
        catchError(error => {
          this.onError('Error loading courses.');
          return of([]);
        })
      );
  }

  onError(errorMsg: string) {
    this.dialog.open(ErrorDialogComponent, {
      data: errorMsg
    });
  }
}
