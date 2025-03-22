import { Location } from '@angular/common';
import { Component } from '@angular/core';
import { FormGroup, NonNullableFormBuilder, ReactiveFormsModule, UntypedFormArray, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatOptionModule } from '@angular/material/core';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatToolbarModule } from '@angular/material/toolbar';
import { ActivatedRoute } from '@angular/router';
import { FormUtilsService } from '../../../shared/service/form/form-utils.service';
import { Course } from '../../model/course';
import { Lesson } from '../../model/lesson';
import { CoursesService } from '../../service/courses.service';

@Component({
  selector: 'app-course-form',
  imports: [
    MatCardModule,
    MatToolbarModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatOptionModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    MatDialogModule
  ],
  templateUrl: './course-form.component.html',
  styleUrl: './course-form.component.scss'
})
export class CourseFormComponent {

  form!: FormGroup;

  constructor(private readonly formBuilder: NonNullableFormBuilder,
              private readonly service: CoursesService,
              private readonly snackBar: MatSnackBar,
              private readonly location: Location,
              private readonly router: ActivatedRoute,
              private readonly formUtils: FormUtilsService) {

    const course: Course = this.router.snapshot.data['course'];

    this.form = this.formBuilder.group({
      id: [course.id],
      name: [course.name, [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(100)
      ]],
      category: [course.category, [
        Validators.required
      ]],
      lessons: this.formBuilder.array(this.retrieveLessons(course), Validators.required)
    });
  }

  private retrieveLessons(course: Course) {
    const lessons = [];
    if (course?.lessons) {
      course.lessons.forEach(lesson => lessons.push(this.createLesson(lesson)));
    } else {
      lessons.push(this.createLesson());
    }
    return lessons;
  }

  private createLesson(lesson: Lesson = { id: '', name: '', youtubeUrl: '' }) {
    return this.formBuilder.group({
      id: [lesson.id],
      name: [
        lesson.name,
        [Validators.required, Validators.minLength(5), Validators.maxLength(100)]
      ],
      youtubeUrl: [
        lesson.youtubeUrl,
        [Validators.required, Validators.minLength(10), Validators.maxLength(11)]
      ]
    });
  }

  getLessons() {
    return (<UntypedFormArray>this.form.get('lessons')).controls;
  }

  addLesson(): void {
    const lesson = this.form.get('lessons') as UntypedFormArray;
    lesson.push(this.createLesson());
  }

  deleteLesson(index: number) {
    const lesson = this.form.get('lessons') as UntypedFormArray;
    lesson.removeAt(index);
  }


  onSubmit() {
    if (this.form.valid) {
      console.log(this.form.value)
      this.service.save(this.form.value).subscribe({
        next: (result) => this.onSuccess(this.form.value),
        error: (error) => this.onError(this.form.value)
      });
    } else {
      this.formUtils.validateAllFormFields(this.form);
    }
  }

  onCancel() {
    this.location.back();
  }

  private onSuccess(record: Partial<Course>) {
    if (record.id) {
      this.snackBar.open('Success updating course!', 'Close', { duration: 5000 });
    } else {
      this.snackBar.open('Success saving course!', 'Close', { duration: 5000 });
    }

    this.onCancel();
  }

  private onError(record: Partial<Course>) {
    if (record.id) {
      this.snackBar.open('Error updating course', 'Close', { duration: 5000 });
    } else {
      this.snackBar.open('Error saving course', 'Close', { duration: 5000 });
    }
  }

  getErrorMessage(fieldName: string): string {
    return this.formUtils.getFieldErrorMessage(this.form, fieldName);
  }

  getLessonErrorMessage(fieldName: string, index: number) {
    return this.formUtils.getFieldFormArrayErrorMessage(
      this.form,
      'lessons',
      fieldName,
      index
    );
  }
}
