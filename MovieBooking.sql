CREATE DATABASE IF NOT EXISTS MovieBooking;
USE MovieBooking;

CREATE TABLE IF NOT EXISTS reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    movie_name VARCHAR(50),
    date DATE,
    time TIME,
    seat_label VARCHAR(5),
    UNIQUE(movie_name, date, time, seat_label) 
);
SELECT * FROM reservations;