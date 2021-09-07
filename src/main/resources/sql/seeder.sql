USE taskmanager_db;

INSERT INTO users(id, email, first_name, last_name, password, username) VALUE (1, 'jearredondo13@gmail.com', 'Jay', 'Arredondo', '$2a$10$d6gQerBRvypsfW56v7rWVOYNHEGfHOaUFcfz0m3AFNkUboChRRT.y', 'jearredondo13@gmail.com');

INSERT INTO categories(id, name) VALUES
(1, 'Finance'),
(2, 'Health'),
(3, 'Chores'),
(4, 'Social'),
(5, 'Work'),
(6, 'Academic'),
(7, 'Hobbies'),
(8, 'Misc.');

INSERT INTO tasks(id, created_at, title, description, is_complete, user_id, category_id) VALUES (1, '2021-08-16 18:30:00', 'test', 'This is a test description.', 0, 1, 5);

INSERT INTO notes(id, body, created_at, title, category_id, user_id) VALUE(1, 'This is my first note! Welcome to Account-a-Buggy!', '021-08-16 18:30:00', 'Welcome!', 5, 1);