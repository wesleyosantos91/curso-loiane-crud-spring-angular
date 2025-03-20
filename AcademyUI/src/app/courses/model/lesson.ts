import { Expose } from 'class-transformer';

export class Lesson {
  id?: string;
  name?: string;
  @Expose({ name: 'youtube_url' })
  youtubeUrl?: string;

  constructor(partial: Partial<Lesson> = {}) {
    Object.assign(this, partial);
  }
}
