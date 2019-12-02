-- jdbc


USE testdb;


CREATE TABLE employees (
    id INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(32) NOT NULL,
    age TINYINT UNSIGNED DEFAULT 1,
    passwd VARCHAR(32) DEFAULT '123456',
    profile_picture MEDIUMBLOB
);



INSERT INTO employees (`name`, age) VALUES ('', );


UPDATE employees SET age = 18 WHERE `name` = 'Denny';

DELETE FROM employees WHERE age = 23;


ALTER TABLE employees ADD COLUMN passwd VARCHAR(32) DEFAULT '123456';
ALTER TABLE employees ADD COLUMN profile_picture MEDIUMBLOB; 
DESC employees;

UPDATE employees SET passwd = 'katy22' WHERE `name` = 'katy';

SELECT * FROM employees WHERE `name` = 'katy' AND passwd = 'katy22';

SELECT * FROM employees WHERE `name` = 'a'' OR passwd = ' AND passwd = ' OR ''1'' = ''1';

SELECT id, `name`, age, passwd AS `password` FROM employees WHERE id = 7;


SELECT id, `name`, age, passwd AS `password`, profile_picture FROM employees WHERE id = 19;

SELECT COUNT(*) FROM employees;
SELECT id, `name`, age, passwd AS `password` FROM employees WHERE NAME = '马云';

-- 练习1
-- 

-- 创建表

CREATE TABLE examstudent (
    FlowID INT PRIMARY KEY AUTO_INCREMENT,
    `Type` INT DEFAULT 4,
    IDCard VARCHAR(18),
    ExamCard VARCHAR(15),
    StudentName VARCHAR(20),
    Location VARCHAR(20),
    Grade INT DEFAULT 0
);

DESC examstudent;

INSERT INTO examstudent (FlowID, `Type`, IDCard, ExamCard, StudentName, Location, Grade) VALUES (?, ?, ?, ?, ?, ?, ?);

SELECT * FROM examstudent;

SELECT FlowID, `Type`, IDCard, ExamCard, StudentName, Location, Grade FROM examstudent WHERE ExamCard = '412824195263214584';

SELECT FlowID flowID, `Type` TYPE, IDCard idCard, ExamCard examCard, StudentName studentName, Location location, Grade grade 
FROM examstudent 
WHERE IDCard = '412824195263214584';


-- 事务
-- 

CREATE TABLE account (
    id INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(32) UNIQUE,
    balance DOUBLE
);

INSERT INTO account VALUES
(NULL, '张无忌', 2000),
(NULL, '赵敏', 2000);

SELECT * FROM account;
UPDATE account SET balance = balance - 500 WHERE `name` = '张无忌';
UPDATE account SET balance = balance + 500 WHERE `name` = '赵敏';

SELECT balance FROM account WHERE `name` = '赵敏';


-- 批量插入
-- 
TRUNCATE TABLE employees;
SELECT * FROM employees LIMIT 10;

INSERT INTO employees (`name`, age, passwd) VALUES ('', );
SELECT COUNT(*) FROM employees;

INSERT INTO employees (`name`, age, passwd) VALUES ('马云', 40, 'my123');

DESC employees;


-- 存储过程：输入用户名、密码，返回登录是否成功
DELIMITER $
CREATE PROCEDURE login_procdr(IN username VARCHAR(32), IN pwd VARCHAR(32), OUT valid INT)
/*
IN:
    username: 用户名
    pwd: 密码

OUT:
    valid: 登录是否有效
            0: 无效
            非0: 有效

*/
BEGIN
    SET valid = 0;                     
    SELECT COUNT(*) INTO valid
    FROM employees
    WHERE `name` = username AND passwd = pwd;
    
END$
DELIMITER ;

CALL login_procdr('马云', 'my123', @s);
SELECT @s;


-- 函数：输入用户名、密码，返回登录是否成功
DELIMITER $

CREATE FUNCTION login_func(username VARCHAR(32), pwd VARCHAR(32)) RETURNS INT
/*
username: 用户名
pwd: 密码

RETURNS: 登录是否有效
    0: 无效
    非0: 有效
*/
BEGIN
    SET @valid = 0;
    SELECT COUNT(*) INTO @valid
    FROM employees
    WHERE `name` = username AND passwd = pwd;
    RETURN @valid;
END$
DELIMITER ;

SELECT login_func('马云', 'my123');

