import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { classToPlain, plainToInstance } from 'class-transformer';
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
      );
  }

  save(record: Partial<Course>): Observable<Course> {

    const transformLessonIds = (lessons: any[]): any[] => {
      return lessons.map(lesson => {
        if (lesson.youtubeUrl) {
          lesson.youtube_url = lesson.youtubeUrl;
          delete lesson.youtubeUrl;
        }
        return lesson;
      });
    };

    const payload = {
      ...record,
      lessons: record.lessons ? transformLessonIds(record.lessons) : []
    };

    if (payload.id) {
      return this.update(payload);
    }
    return this.create(payload);
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
