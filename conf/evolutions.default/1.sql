# Users schema

# --- !Ups

CREATE TABLE USER (
firstname TEXT NOT NULL,
middlename TEXT,
lastname TEXT NOT NULL,
username TEXT NOT NULL,
passwordTEXT NOT NULL,
verifyPassword TEXT NOT NULL,
mobile TEXT NOT NULL,
gender TEXT NOT NULL,
age INT NOT NULL,
hobbies TEXT,
accountType TEXT NOT NULL
);

# --- !Downs

DROP TABLE USER;