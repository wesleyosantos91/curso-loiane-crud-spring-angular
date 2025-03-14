import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import { MatTableModule } from "@angular/material/table";
import { CategoryPipe } from '../../../shared/pipes/category.pipe';
import { Course } from '../../model/course';

@Component({
  selector: 'app-courses-list',
  imports: [
    MatTableModule,
    MatIconModule,
    MatButtonModule,
    CategoryPipe
  ],
  templateUrl: './courses-list.component.html',
  styleUrl: './courses-list.component.scss'
})
export class CoursesListComponent {

  @Input() courses: Course[] = [];
  @Output() add: EventEmitter<boolean> = new EventEmitter(false);
  @Output() edit: EventEmitter<Course> = new EventEmitter(false);

  readonly displayedColumns: string[] = ['name', 'category', 'actions'];

  constructor() {
  }

  onAdd() {
    this.add.emit(true)
  }

  onEdit(record: Course) {
    this.edit.emit(record)
  }
}
