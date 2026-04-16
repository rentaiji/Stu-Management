-- Student Management Platform Database Script
-- Version: V1.0

CREATE DATABASE IF NOT EXISTS stu_manage DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE stu_manage;

-- Department Table
DROP TABLE IF EXISTS sys_department;
CREATE TABLE sys_department (
    dept_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    parent_id BIGINT DEFAULT 0,
    dept_name VARCHAR(50) NOT NULL,
    dept_code VARCHAR(30) NOT NULL,
    dept_type TINYINT DEFAULT 0,
    leader_id BIGINT DEFAULT NULL,
    order_num INT DEFAULT 0,
    status CHAR(1) DEFAULT '0',
    create_time DATETIME DEFAULT NULL,
    update_time DATETIME DEFAULT NULL,
    remark VARCHAR(500) DEFAULT NULL,
    deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Major Table
DROP TABLE IF EXISTS edu_major;
CREATE TABLE edu_major (
    major_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    major_name VARCHAR(50) NOT NULL,
    major_code VARCHAR(20) NOT NULL,
    dept_id BIGINT NOT NULL,
    degree_type TINYINT DEFAULT 0,
    study_length YEAR DEFAULT 4,
    status CHAR(1) DEFAULT '0',
    create_time DATETIME DEFAULT NULL,
    deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Grade Table
DROP TABLE IF EXISTS edu_grade;
CREATE TABLE edu_grade (
    grade_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    grade_name VARCHAR(20) NOT NULL,
    grade_code VARCHAR(10) NOT NULL,
    entrance_year INT NOT NULL,
    is_current CHAR(1) DEFAULT '0',
    status CHAR(1) DEFAULT '0',
    create_time DATETIME DEFAULT NULL,
    deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Class Table
DROP TABLE IF EXISTS edu_class;
CREATE TABLE edu_class (
    class_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    class_name VARCHAR(50) NOT NULL,
    class_code VARCHAR(20) NOT NULL,
    grade_id BIGINT NOT NULL,
    major_id BIGINT DEFAULT NULL,
    head_teacher_id BIGINT DEFAULT NULL,
    student_count INT DEFAULT 0,
    status CHAR(1) DEFAULT '0',
    create_time DATETIME DEFAULT NULL,
    deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- User Table
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(30) NOT NULL UNIQUE COMMENT '用户名（学号/工号）',
    real_name VARCHAR(30) DEFAULT NULL COMMENT '真实姓名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    user_type CHAR(1) NOT NULL DEFAULT '0' COMMENT '用户类型 0-学生 1-教师 2-管理员',
    email VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    gender CHAR(1) DEFAULT '0' COMMENT '性别',
    avatar VARCHAR(200) DEFAULT NULL COMMENT '头像路径',
    dept_id BIGINT DEFAULT NULL COMMENT '所属部门',
    status CHAR(1) DEFAULT '0' COMMENT '状态',
    login_ip VARCHAR(50) DEFAULT NULL COMMENT '最后登录 IP',
    login_date DATETIME DEFAULT NULL COMMENT '最后登录时间',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志',
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Student Table
DROP TABLE IF EXISTS edu_student;
CREATE TABLE edu_student (
    student_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    student_no VARCHAR(20) NOT NULL UNIQUE,
    grade_id BIGINT NOT NULL,
    class_id BIGINT DEFAULT NULL,
    major_id BIGINT DEFAULT NULL,
    enrollment_year INT NOT NULL,
    study_mode TINYINT DEFAULT 0,
    student_status TINYINT DEFAULT 0,
    id_card VARCHAR(18) DEFAULT NULL,
    birth_date DATE DEFAULT NULL,
    birth_place VARCHAR(100) DEFAULT NULL,
    native_place VARCHAR(100) DEFAULT NULL,
    nation VARCHAR(20) DEFAULT 'Han',
    political_status TINYINT DEFAULT 0,
    entry_date DATE DEFAULT NULL,
    graduate_date DATE DEFAULT NULL,
    create_time DATETIME DEFAULT NULL,
    update_time DATETIME DEFAULT NULL,
    deleted TINYINT DEFAULT 0,
    INDEX idx_grade (grade_id),
    INDEX idx_class (class_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Teacher Table
DROP TABLE IF EXISTS edu_teacher;
CREATE TABLE edu_teacher (
    teacher_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,
    teacher_no VARCHAR(20) NOT NULL UNIQUE,
    dept_id BIGINT NOT NULL,
    title TINYINT DEFAULT 0,
    degree TINYINT DEFAULT 0,
    research_direction VARCHAR(200) DEFAULT NULL,
    first_hire_date DATE DEFAULT NULL,
    regular_date DATE DEFAULT NULL,
    hire_level VARCHAR(50) DEFAULT NULL,
    special_level VARCHAR(50) DEFAULT NULL,
    special_contract_date DATE DEFAULT NULL,
    special_contract_file VARCHAR(500) DEFAULT NULL,
    education_certificates TEXT DEFAULT NULL,
    entry_date DATE DEFAULT NULL,
    create_time DATETIME DEFAULT NULL,
    update_time DATETIME DEFAULT NULL,
    deleted TINYINT DEFAULT 0,
    INDEX idx_dept (dept_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Role Table
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
    role_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(30) NOT NULL,
    role_key VARCHAR(30) NOT NULL,
    role_sort INT NOT NULL,
    status CHAR(1) DEFAULT '0',
    remark VARCHAR(500) DEFAULT NULL,
    create_time DATETIME DEFAULT NULL,
    deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- User Role Table
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Course Table
DROP TABLE IF EXISTS edu_course;
CREATE TABLE edu_course (
    course_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_code VARCHAR(20) NOT NULL,
    course_name VARCHAR(100) NOT NULL,
    credits DECIMAL(3,1) NOT NULL,
    hours INT NOT NULL,
    course_type CHAR(1) DEFAULT '0',
    teacher_id BIGINT DEFAULT NULL,
    capacity INT DEFAULT 0,
    selected_count INT DEFAULT 0,
    description TEXT DEFAULT NULL,
    status CHAR(1) DEFAULT '0',
    create_time DATETIME DEFAULT NULL,
    deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Semester Table
DROP TABLE IF EXISTS edu_semester;
CREATE TABLE edu_semester (
    semester_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    semester_name VARCHAR(50) NOT NULL,
    semester_code VARCHAR(20) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_current CHAR(1) DEFAULT '0',
    status CHAR(1) DEFAULT '0',
    create_time DATETIME DEFAULT NULL,
    deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Student Course Table
DROP TABLE IF EXISTS edu_student_course;
CREATE TABLE edu_student_course (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    semester_id BIGINT NOT NULL,
    select_time DATETIME NOT NULL,
    select_status CHAR(1) DEFAULT '0',
    audit_status CHAR(1) DEFAULT '0',
    audit_by BIGINT DEFAULT NULL,
    audit_time DATETIME DEFAULT NULL,
    remark VARCHAR(500) DEFAULT NULL,
    deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Score Table
DROP TABLE IF EXISTS edu_score;
CREATE TABLE edu_score (
    score_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    semester_id BIGINT NOT NULL,
    regular_score DECIMAL(5,2) DEFAULT NULL,
    final_score DECIMAL(5,2) DEFAULT NULL,
    total_score DECIMAL(5,2) NOT NULL,
    grade_point DECIMAL(4,2) DEFAULT NULL,
    grade_level VARCHAR(10) DEFAULT NULL,
    credit_earned DECIMAL(3,1) DEFAULT NULL,
    input_by BIGINT NOT NULL,
    input_time DATETIME NOT NULL,
    status CHAR(1) DEFAULT '0',
    remark VARCHAR(500) DEFAULT NULL,
    deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Teacher Research Table
DROP TABLE IF EXISTS edu_teacher_research;
CREATE TABLE edu_teacher_research (
    research_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    teacher_id BIGINT NOT NULL,
    research_type TINYINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    level TINYINT DEFAULT 0,
    award_date DATE DEFAULT NULL,
    ranking INT DEFAULT NULL,
    remark TEXT DEFAULT NULL,
    attachment_url VARCHAR(500) DEFAULT NULL,
    status CHAR(1) DEFAULT '0',
    create_time DATETIME DEFAULT NULL,
    deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Teacher Family Table
DROP TABLE IF EXISTS edu_teacher_family;
CREATE TABLE edu_teacher_family (
    family_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    teacher_id BIGINT NOT NULL,
    member_name VARCHAR(50) NOT NULL,
    relation TINYINT DEFAULT 0,
    birth_date DATE DEFAULT NULL,
    school_name VARCHAR(100) DEFAULT NULL,
    grade_class VARCHAR(50) DEFAULT NULL,
    status CHAR(1) DEFAULT '0',
    create_time DATETIME DEFAULT NULL,
    deleted TINYINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Init Data
INSERT INTO sys_department (dept_name, dept_code, dept_type, status) VALUES 
('清华大学', 'THU', 0, '0'),
('计算机学院', 'CS', 1, '0');

INSERT INTO sys_user (user_name, real_name, password, user_type, dept_id, status) VALUES 
('admin', '系统管理员', '$2a$10$N.zxrWvOlqMDvpQfMzKWyO3Yp4JKVLPqN9sVN8bEJxLZLKxLKxLKx', '2', 1, '0'),
('T001', '李老师', '$2a$10$N.zxrWvOlqMDvpQfMzKWyO3Yp4JKVLPqN9sVN8bEJxLZLKxLKxLKx', '1', 2, '0'),
('2024001', '张三', '$2a$10$N.zxrWvOlqMDvpQfMzKWyO3Yp4JKVLPqN9sVN8bEJxLZLKxLKxLKx', '0', 2, '0');

INSERT INTO edu_teacher (user_id, teacher_no, dept_id, title, degree) VALUES 
(2, 'T001', 2, 1, 2);

INSERT INTO edu_student (user_id, student_no, grade_id, enrollment_year, nation, political_status) VALUES 
(3, '2024001', 1, 2024, '汉', 1);

INSERT INTO sys_role (role_name, role_key, role_sort, status) VALUES 
('超级管理员', 'admin', 1, '0'),
('学校教务管理员', 'school_academic', 2, '0'),
('院系教务秘书', 'dept_academic', 3, '0'),
('普通教师', 'teacher', 4, '0'),
('系主任', 'dept_head', 5, '0'),
('辅导员', 'counselor', 6, '0'),
('学生', 'student', 7, '0');

INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1), (2, 4), (3, 7);

INSERT INTO edu_grade (grade_name, grade_code, entrance_year, is_current, status) VALUES 
('Grade 2024', '2024', 2024, '1', '0');

INSERT INTO edu_class (class_name, class_code, grade_id, student_count, status) VALUES 
('Class CS202401', 'CS202401', 1, 45, '0');

INSERT INTO edu_course (course_code, course_name, credits, hours, course_type, teacher_id, capacity) VALUES 
('CS101', 'Programming Basics', 3.0, 48, '0', 2, 100),
('CS201', 'Data Structures', 4.0, 64, '0', 2, 80);

INSERT INTO edu_semester (semester_name, semester_code, start_date, end_date, is_current, status) VALUES 
('2025-2026 Fall', '2025F', '2025-09-01', '2026-01-20', '1', '0');
