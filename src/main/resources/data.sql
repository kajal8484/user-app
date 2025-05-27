CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(255),
  username VARCHAR(255) UNIQUE,
  password VARCHAR(255),
  email VARCHAR(255)
);

INSERT INTO users (id, full_name, username, password, email) VALUES
(1, 'Test User', 'testuser', 'password123', 'test@example.com');
