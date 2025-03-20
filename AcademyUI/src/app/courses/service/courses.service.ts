import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { plainToInstance } from 'class-transformer';
import { delay, first, map, Observable, tap } from 'rxjs';
import { Course } from '../model/course';
import { Lesson } from '../model/lesson';

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
        delay(3000),
        tap(courses => console.log(courses))
      );
  }

  save(record: Partial<Course>): Observable<Course> {
    console.log(record);
    if (record.id) {
      return this.update(record);
    }
    return this.create(record);
  }

  loadById(id: string) {
    return this.http.get<Course>(`${this.API}/${id}`)
      .pipe(
        map(course => {
          if (course.lessons) {
            course.lessons = plainToInstance(Lesson, course.lessons);
          }
          return course;
        }),
        first(),
        tap(courses => console.log(courses))
      );
  }

  remove(id: string) {
    return this.http.delete(`${this.API}/${id}`).pipe(first());
  }

  private update(record: Partial<Course>) {
    return this.http.put<Course>(`${this.API}/${record.id}`, record).pipe(first());
  }

  private create(record: Partial<Course>) {
    return this.http.post<Course>(this.API, record).pipe(first());
  }

}
