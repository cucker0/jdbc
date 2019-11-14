-- jdbc


USE testdb;


CREATE TABLE employees (
    id INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(32) NOT NULL,
    age TINYINT UNSIGNED DEFAULT 1
);