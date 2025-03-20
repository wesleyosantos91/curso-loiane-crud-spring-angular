import { inject } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { of } from 'rxjs';
import { Course } from '../model/course';
import { CoursesService } from '../service/courses.service';

export const courseResolver: ResolveFn<Course> = (route, state) => {
  if (route.params && route.paramMap.get('id')) {
    return inject(CoursesService).loadById(route.paramMap.get('id')!);
  }

  return of<Course>({
    id: '',
    name: '',
    category: '',
    lessons: []
  });
};
