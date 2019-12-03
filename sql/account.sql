CREATE TABLE account (
    id INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(32) UNIQUE,
    balance DOUBLE
);

INSERT INTO account VALUES
(NULL, '张无忌', 2000),
(NULL, '赵敏', 2000);