CREATE TABLE tb_courses
(
    id         UUID             PRIMARY KEY DEFAULT gen_random_uuid(),
    name       VARCHAR(255)     NOT NULL,
    category   VARCHAR(255)     NOT NULL
);