-- 创建表

create table examstudent (
    FlowID int primary key auto_increment,
    `Type` int default 4,
    IDCard varchar(18),
    ExamCard varchar(15),
    StudentName varchar(20),
    Location varchar(20),
    Grade int default 0
);