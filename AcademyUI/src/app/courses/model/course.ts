import { Lesson } from './lesson';

export class Course {
  id?: string;
  name?: string;
  category?: string;
  lessons?: Lesson[];
}
