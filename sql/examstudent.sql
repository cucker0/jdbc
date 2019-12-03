CREATE TABLE examstudent (
    FlowID INT PRIMARY KEY AUTO_INCREMENT,
    `Type` INT DEFAULT 4,
    IDCard VARCHAR(18),
    ExamCard VARCHAR(15),
    StudentName VARCHAR(20),
    Location VARCHAR(20),
    Grade INT DEFAULT 0
);