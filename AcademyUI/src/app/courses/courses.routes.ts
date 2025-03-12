import { Routes } from '@angular/router';
import { CourseFormComponent } from './course-form/course-form.component';
import { CoursesComponent } from './courses/courses.component';

export const COURSES_ROUTES: Routes = [
  {
    path: '', component: CoursesComponent
  },
  {
    path: 'new', component: CourseFormComponent
  }
];


