CREATE TABLE tb_courses
(
    id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name     VARCHAR(100)  NOT NULL UNIQUE,
    category VARCHAR(10)  NOT NULL,
    status   VARCHAR(10) NOT NULL
);