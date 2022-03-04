CREATE DATABASE IF NOT EXISTS dev;
USE dev;
create table courses (id bigint not null auto_increment, name varchar(255), primary key (id)) engine=InnoDB;
create table student_courses (student_id bigint not null, course_id bigint not null, primary key (student_id, course_id)) engine=InnoDB;
create table students (id bigint not null auto_increment, name varchar(255), primary key (id)) engine=InnoDB;


INSERT INTO courses (id, name) VALUES (1, "Programming");
INSERT INTO courses (id, name) VALUES (2, "Data Bases");
INSERT INTO courses (id, name) VALUES (3, "Algorithms");
INSERT INTO courses (id, name) VALUES (4, "Spring Boot");