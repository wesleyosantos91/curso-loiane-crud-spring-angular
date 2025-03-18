CREATE TABLE tb_lessons
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(100) NOT NULL UNIQUE,
    youtube_url VARCHAR(11)  NOT NULL,
    course_id   UUID REFERENCES tb_courses (id)
);