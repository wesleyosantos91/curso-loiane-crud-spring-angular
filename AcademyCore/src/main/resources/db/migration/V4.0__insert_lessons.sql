INSERT INTO tb_lessons (name, youtube_url, course_id)
VALUES
    ('Introdução ao Angular', 'abc123xyz01', (SELECT id FROM tb_courses WHERE name = 'Angular')),
    ('Componentes em Angular', 'abc123xyz02', (SELECT id FROM tb_courses WHERE name = 'Angular')),
    ('Hooks no React', 'abc123xyz03', (SELECT id FROM tb_courses WHERE name = 'React')),
    ('Gerenciamento de Estado no React', 'abc123xyz04', (SELECT id FROM tb_courses WHERE name = 'React')),
    ('Fundamentos do Vue.js', 'abc123xyz05', (SELECT id FROM tb_courses WHERE name = 'Vue')),
    ('Vue Router e Vuex', 'abc123xyz06', (SELECT id FROM tb_courses WHERE name = 'Vue')),
    ('Criando APIs com Node.js', 'abc123xyz07', (SELECT id FROM tb_courses WHERE name = 'Node')),
    ('Autenticação com JWT no Node.js', 'abc123xyz08', (SELECT id FROM tb_courses WHERE name = 'Node')),
    ('Introdução ao Java', 'abc123xyz09', (SELECT id FROM tb_courses WHERE name = 'Java')),
    ('Spring Boot na Prática', 'abc123xyz10', (SELECT id FROM tb_courses WHERE name = 'Java')),
    ('Concurrency em Golang', 'abc123xyz11', (SELECT id FROM tb_courses WHERE name = 'Golang')),
    ('Criando APIs com Golang', 'abc123xyz12', (SELECT id FROM tb_courses WHERE name = 'Golang'));
