import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { delay, first, Observable, tap } from 'rxjs';
import { Course } from '../model/course';

@Injectable({
  providedIn: 'root'
})
export class CoursesService {

  private readonly API = '/api/v1/courses';

  constructor(private http: HttpClient) { }

  list(): Observable<Course[]>  {
    return this.http.get<Course[]>(this.API)
      .pipe(
        first(),
        delay(5000),
        tap(courses => console.log(courses))
      );
  }

  save(record: Course): Observable<Course> {
    return this.http.post<Course>(this.API, record)
      .pipe(
        first()
      );
  }
}
