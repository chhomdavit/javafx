-- Create a database
CREATE DATABASE test_javafx;

-- Switch to the created database
\c test_javafx

-- Create a table
CREATE TABLE test_javafx (
    id SERIAL PRIMARY KEY, -- SERIAL automatically creates an auto-increment column
    FirstName VARCHAR(255) NOT NULL,
    LastName VARCHAR(255) NOT NULL,
    COURSE VARCHAR(20) NOT NULL
);

-- Insert data into the table
INSERT INTO test_javafx (FirstName, LastName, COURSE)
VALUES ('Ivan', 'Ivanov', 'JavaFX');

CREATE TABLE login_users (
    ID SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    salt VARCHAR(255) NOT NULL,
);

INSERT INTO login_users (username, password)
VALUES ('admin', '123456');

-- Show the table
SELECT * FROM test_javafx;

-- Show the table
SELECT * FROM login_users;