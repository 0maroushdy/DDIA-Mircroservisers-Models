CREATE DATABASE IF NOT EXISTS ratings_db;
USE ratings_db;

CREATE TABLE ratings (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     VARCHAR(255) NOT NULL,
    movie_id    VARCHAR(255) NOT NULL,
    rating      INT          NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_movie (user_id, movie_id),
    INDEX idx_user (user_id),
    INDEX idx_movie (movie_id)
);

INSERT INTO ratings (user_id, movie_id, rating) VALUES
('user1', '550', 4),
('user1', '551', 3),
('user1', '552', 5),
('user2', '550', 5),
('user2', '553', 2);
