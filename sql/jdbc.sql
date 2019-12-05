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



-- BookMalls
-- 

-- books
CREATE TABLE books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL,
    price DOUBLE(11, 2) NOT NULL,
    sales INT NOT NULL,
    stock INT NOT NULL,
    img_path VARCHAR(100)
);

-- users
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) UNIQUE KEY NOT NULL,
    `password` VARCHAR(100),
    email VARCHAR(100) DEFAULT NULL
);

-- orders
CREATE TABLE orders (
    id VARCHAR(100) PRIMARY KEY,
    order_time DATETIME NOT NULL,
    total_count INT NOT NULL,
    total_amount DOUBLE(11, 2) NOT NULL,
    state INT NOT NULL,
    user_id INT NOT NULL,
    
    CONSTRAINT orders_user_id_fk_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);


-- order_items
CREATE TABLE order_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    `count` INT NOT NULL,
    amount DOUBLE(11, 2) NOT NULL,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL,
    price DOUBLE(11, 2) NOT NULL,
    img_path VARCHAR(100) NOT NULL,
    order_id VARCHAR(100) NOT NULL,
    
    CONSTRAINT order_items_orders_id_fk_orders_id FOREIGN KEY (order_id) REFERENCES orders (id)
);

-- 
INSERT  INTO `books`(`title`, `author`, `price`, `sales`, `stock`, `img_path`) VALUES 
('解忧杂货店','东野圭吾',27.20,102,98,'upload/books/解忧杂货店.jpg'),
('边城','沈从文',23.00,102,98,'upload/books/边城.jpg'),
('中国哲学史','冯友兰',44.50,101,99,'upload/books/中国哲学史.jpg'),
('忽然七日',' 劳伦',19.33,101,99,'upload/books/忽然七日.jpg'),
('苏东坡传','林语堂',19.30,100,100,'upload/books/苏东坡传.jpg'),
('百年孤独','马尔克斯',29.50,100,100,'upload/books/百年孤独.jpg'),
('扶桑','严歌苓',19.80,100,100,'upload/books/扶桑.jpg'),
('给孩子的诗','北岛',22.20,100,100,'upload/books/给孩子的诗.jpg'),
('为奴十二年','所罗门',16.50,100,100,'upload/books/为奴十二年.jpg'),
('平凡的世界','路遥',55.00,100,100,'upload/books/平凡的世界.jpg'),
('悟空传','今何在',14.00,100,100,'upload/books/悟空传.jpg'),
('硬派健身','斌卡',31.20,100,100,'upload/books/硬派健身.jpg'),
('从晚清到民国','唐德刚',39.90,100,100,'upload/books/从晚清到民国.jpg'),
('三体','刘慈欣',56.50,100,100,'upload/books/三体.jpg'),
('看见','柴静',19.50,100,100,'upload/books/看见.jpg'),
('活着','余华',11.00,100,100,'upload/books/活着.jpg'),
('小王子','安托万',19.20,100,100,'upload/books/小王子.jpg'),
('我们仨','杨绛',11.30,100,100,'upload/books/我们仨.jpg'),
('生命不息,折腾不止','罗永浩',25.20,100,100,'upload/books/生命不息.jpg'),
('皮囊','蔡崇达',23.90,100,100,'upload/books/皮囊.jpg'),
('恰到好处的幸福','毕淑敏',16.40,100,100,'upload/books/恰到好处的幸福.jpg'),
('大数据预测','埃里克',37.20,100,100,'upload/books/大数据预测.jpg'),
('人月神话','布鲁克斯',55.90,100,100,'upload/books/人月神话.jpg'),
('C语言入门经典','霍尔顿',45.00,100,100,'upload/books/C语言入门经典.jpg'),
('数学之美','吴军',29.90,100,100,'upload/books/数学之美.jpg'),
('Java编程思想','埃史尔',70.50,100,100,'upload/books/Java编程思想.jpg'),
('设计模式之禅','秦小波',20.20,100,100,'upload/books/设计模式之禅.jpg'),
('图解机器学习','杉山将',33.80,100,100,'upload/books/图解机器学习.jpg'),
('艾伦图灵传','安德鲁',47.20,100,100,'upload/books/艾伦图灵传.jpg'),
('教父','马里奥普佐',29.00,100,100,'upload/books/教父.jpg');

-- 
INSERT INTO `users`(`username`,`password`,`email`) VALUES
('admin',SHA1('123456'),'admin@atguigu.com'),
('admin2',SHA1('123456'),'admin2@atguigu.com'),
('admin3',SHA1('123456'),'admin3@atguigu.com'),
('chai',SHA1('123'),'chai@atguigu.com');

-- 
INSERT  INTO `orders`(`id`,`order_time`,`total_count`,`total_amount`,`state`,`user_id`) VALUES 
('15275760194821','2018-05-29 14:40:19',4,114.03,2,1),
('15294258455691','2018-06-20 00:30:45',2,50.20,0,1);

-- 
INSERT  INTO `order_items`
(`count`,`amount`,`title`,`author`,`price`,`img_path`,`order_id`) VALUES 
(1,27.20,'解忧杂货店','东野圭吾',27.20,'static/img/default.jpg','15275760194821'),
(1,23.00,'边城','沈从文',23.00,'static/img/default.jpg','15275760194821'),
(1,44.50,'中国哲学史','冯友兰',44.50,'static/img/default.jpg','15275760194821'),
(1,19.33,'忽然七日',' 劳伦',19.33,'static/img/default.jpg','15275760194821'),
(1,27.20,'解忧杂货店','东野圭吾',27.20,'static/img/default.jpg','15294258455691'),
(1,23.00,'边城','沈从文',23.00,'static/img/default.jpg','15294258455691');

SELECT * FROM order_items;

SELECT * FROM orders;

INSERT INTO books(title,author,price,sales,stock,img_path) VALUES(?,?,?,?,?,?)

SELECT SHA1('123456');

SELECT MAX(sales) FROM books
;
SELECT * FROM books WHERE sales = (SELECT MAX(sales) FROM books)
;

UPDATE books SET stock = ? WHERE id = ?;

SELECT * FROM users;

SELECT id, order_time, total_count, total_amount, state, user_id FROM orders WHERE id = ?;
SELECT * FROM orders;
SELECT * FROM order_items;

INSERT INTO (order_time, total_count, total_amount, state, user_id) VALUES (?, ?, ?, ?, ?, ?);
SELECT NOW();

SELECT `id`, `count`,`amount`,`title`,`author`,`price`,`img_path`,`order_id` FROM order_items WHERE id = 1;