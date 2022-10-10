INSERT INTO role(id,role_name) VALUES(1,'ROLE_USER'),(2,'ROLE_ADMIN');
INSERT INTO usuario( id,age, email, name, password, username) VALUES (1,18, 'admin@admin.com', 'admin', '$2y$10$kAIA.7JFnwiLGDdQATBqp.rkZoM0vQ002.il.HBwwE0SJ85l9KIjy', 'admin');
INSERT INTO usuario_role(usuario_id,role_id) VALUES(1,2);