USE taskmanager_db;

INSERT INTO users(id, email, first_name, last_name, password, username) VALUE (1, 'test@gmail.com', 'Jay', 'Arredondo', 'test', 'test');


INSERT INTO categories(id, name) VALUES
(1, 'Finance'),
(2, 'Health'),
(3, 'Family'),
(4, 'Social'),
(5, 'Work'),
(6, 'Academic'),
(7, 'Hobbies'),
(8, 'Misc.')

INSERT INTO tasks(id, created_at, title, description, is_complete) VALUES (1, '2021-08-16 18:30:00', 'test', 'This is a test description.', 0);
