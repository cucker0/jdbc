-- jdbc


USE testdb;


CREATE TABLE employees (
    id INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(32) NOT NULL,
    age TINYINT UNSIGNED DEFAULT 1
);

SELECT * FROM employees;

INSERT INTO employees (`name`, age) VALUES ('', );


UPDATE employees SET age = 18 WHERE `name` = 'Denny';

DELETE FROM employees WHERE age = 23;


ALTER TABLE employees ADD COLUMN passwd VARCHAR(32) DEFAULT '123456';

UPDATE employees SET passwd = 'katy22' WHERE `name` = 'katy';

SELECT * FROM employees WHERE `name` = 'katy' AND passwd = 'katy22';



