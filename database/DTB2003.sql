USE master;
GO
ALTER DATABASE FlashcardDB1 SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
GO
DROP DATABASE FlashcardDB1;
GO

CREATE DATABASE FlashcardDB1;
GO
USE FlashcardDB1;
GO

-- Tạo bảng User
CREATE TABLE [user](
    user_id INT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(12) UNIQUE,
    [password] VARCHAR(100) NOT NULL,
    vip_status BIT DEFAULT 0,
    vip_type NVARCHAR(10) CHECK (vip_type IN ('vip1', 'vip2', 'vip3')),
    vip_start_date DATETIME,
    vip_end_date DATETIME,
    timesCreate INT DEFAULT 0,
    createdDay DATE DEFAULT CAST(GETDATE() AS DATE), 
    [role] BIT DEFAULT 0,
    dob DATE,
    verificationCode TEXT,
    isVerification BIT DEFAULT 0
);
GO

-- Tạo bảng User Activity
CREATE TABLE user_activity (
    activity_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    activity_date DATE NOT NULL,
    start_time DATETIME,
    end_time DATETIME,
    hours_spent FLOAT,
    FOREIGN KEY (user_id) REFERENCES [user](user_id) ON DELETE CASCADE
);
GO

-- Tạo bảng User Wallet
CREATE TABLE user_wallet (
    wallet_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    balance FLOAT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES [user](user_id) ON DELETE CASCADE
);
GO

-- Tạo bảng Transactions
CREATE TABLE transactions (
    transaction_id INT IDENTITY(1,1) PRIMARY KEY,
    wallet_id INT NOT NULL,
    transaction_type NVARCHAR(10) CHECK (transaction_type IN ('Deposit', 'Withdraw', 'Payment')),
    amount FLOAT NOT NULL,
    transaction_date DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (wallet_id) REFERENCES user_wallet(wallet_id) ON DELETE CASCADE
);
GO

-- Tạo bảng Flashcard Set
CREATE TABLE flashcard_set(
    flashcard_set_id INT IDENTITY(1,1) PRIMARY KEY,
    time_created DATETIME DEFAULT GETDATE(),
    user_id INT NOT NULL,
    name_of_flashcardset NVARCHAR(200),
    [status] NVARCHAR(10) CHECK ([status] IN ('New', 'Learning', 'Known', 'Forgotten')) DEFAULT 'New',
    score FLOAT DEFAULT 0,
    tags VARCHAR(200),
    visibility NVARCHAR(10) CHECK (visibility IN ('Everyone', 'Only me')) DEFAULT 'Only me',
    [view] INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES [user](user_id) ON DELETE CASCADE
);
GO

-- Tạo bảng Flashcard
CREATE TABLE flashcard (
    flashcard_id INT IDENTITY(1,1) PRIMARY KEY,
    flashcard_set_id INT NOT NULL,
    term NVARCHAR(1000) NOT NULL,
    flashcard_definition NVARCHAR(1000) NOT NULL,
    FOREIGN KEY (flashcard_set_id) REFERENCES flashcard_set(flashcard_set_id) ON DELETE CASCADE
);
GO

-- Tạo bảng Quick Storage
CREATE TABLE quick_storage (
    user_id INT NOT NULL,
    flashcard_set_id INT NOT NULL,
    PRIMARY KEY (user_id, flashcard_set_id),
    FOREIGN KEY (user_id) REFERENCES [user](user_id) ON DELETE NO ACTION,
    FOREIGN KEY (flashcard_set_id) REFERENCES flashcard_set(flashcard_set_id) ON DELETE CASCADE
);
GO

-- Thêm dữ liệu mẫu vào bảng User
INSERT INTO [user] (username, email, phone_number, [password], vip_status, vip_type, vip_start_date, vip_end_date, [role], dob, verificationCode, isVerification)
VALUES 
('HuyAnhDang', 'huy.anh@example.com', '0987654321', 'A123456789', 1, 'vip1', '2025-01-01', '2025-12-31', 1, '1990-01-15', 'ABC123', 1),
('TranDat', 'tran.dat@example.com', '0987654322', 'A123456789', 1, 'vip2', '2025-02-01', '2025-12-31', 0, '1992-02-25', 'DEF456', 1),
('MaiLan', 'mai.lan@example.com', '0987654323', 'A123456789', 0, NULL, NULL, NULL, 0, '1995-03-10', 'GHI789', 0),
('NguyenMinh', 'nguyen.minh@example.com', '0987654324', 'A123456789', 1, 'vip3', '2025-03-01', '2025-12-31', 0, '1988-05-20', 'JKL012', 1),
('PhanLinh', 'phan.linh@example.com', '0987654325', 'A123456789', 0, NULL, NULL, NULL, 0, '2000-07-30', 'MNO345', 1);
GO

-- Thêm dữ liệu mẫu cho Flashcard Set
INSERT INTO flashcard_set (user_id, name_of_flashcardset, [status], score, tags, visibility, [view])
VALUES 
(1, 'English Vocabulary', 'New', 0, 'language,study', 'Everyone', 0),
(2, 'Math Formulas', 'Learning', 0, 'math,formulas', 'Only me', 5),
(1, 'World History', 'New', 0, 'history,education', 'Everyone', 2),
(2, 'Animal Kingdom', 'Learning', 0, 'biology,animals', 'Only me', 4),
(1, 'Computer Science Basics', 'New', 0, 'computer science,programming', 'Everyone', 3),
(2, 'Chemistry Basics', 'Known', 0, 'chemistry,science', 'Everyone', 1);
GO

-- Thêm dữ liệu mẫu cho Flashcard
INSERT INTO flashcard (flashcard_set_id, term, flashcard_definition)
VALUES 
(1, 'Apple', 'A round fruit with red, green, or yellow skin.'),
(1, 'Banana', 'A long, curved fruit with a yellow skin.'),
(2, 'Pythagorean Theorem', 'In a right triangle, a² + b² = c².'),
(2, 'Quadratic Formula', 'x = (-b ± √(b²-4ac)) / 2a.'),
(3, 'World War II', 'A global conflict from 1939 to 1945 involving many world powers.'),
(4, 'Mammal', 'A warm-blooded vertebrate animal that has hair or fur and produces milk.'),
(5, 'Algorithm', 'A step-by-step procedure for solving a problem or performing a task.'),
(6, 'Atom', 'The basic unit of a chemical element.');
GO

-- Kiểm tra dữ liệu
SELECT * FROM [user];
GO
SELECT * FROM flashcard_set;
GO
SELECT * FROM flashcard;
GO
SELECT * FROM user_activity;
GO

