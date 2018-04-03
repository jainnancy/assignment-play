# Users schema

# --- !Ups

CREATE TABLE User (
firstname TEXT NOT NULL,
middlename TEXT,
lastname TEXT NOT NULL,
username TEXT NOT NULL,
password TEXT NOT NULL,
verifyPassword TEXT NOT NULL,
mobile TEXT NOT NULL,
gender TEXT NOT NULL,
age INT NOT NULL,
hobbies TEXT,
accountType TEXT NOT NULL
);

# Assignment schema

CREATE TABLE Assignment (
id INT(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
title VARCHAR(20) NOT NULL,
description VARCHAR(100) NOT NULL
);

# --- !Downs

DROP TABLE USER;
DROP TABLE Assignment;
