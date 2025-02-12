-- Insert into "users" table
INSERT INTO users (id, name, email, username, password, role) VALUES
                                                                  (1, 'Admin User', 'admin@example.com', 'admin', 'password', 'ADMIN'),
                                                                  (2, 'Normal User', 'user@example.com', 'user', 'password', 'USER');

-- Insert initial data for books
INSERT INTO book (id, title, author, reserved) VALUES
                                                   (1, '1984', 'George Orwell', false),
                                                   (2, 'To Kill a Mockingbird', 'Harper Lee', false);
