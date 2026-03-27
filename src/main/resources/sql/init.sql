CREATE DATABASE IF NOT EXISTS stu_manage DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE stu_manage;

DROP TABLE IF EXISTS sys_department;
CREATE TABLE sys_department (
    dept_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '部门 ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父部门 ID',
    dept_name VARCHAR(50) NOT NULL COMMENT '部门名称',
    dept_code VARCHAR(30) NOT NULL COMMENT '部门编码',
    dept_type TINYINT DEFAULT 0 COMMENT '部门类型',
    leader_id BIGINT DEFAULT NULL COMMENT '负责人 ID',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    status CHAR(1) DEFAULT '0' COMMENT '状态',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户 ID',
    user_name VARCHAR(30) NOT NULL COMMENT '用户名',
    real_name VARCHAR(30) DEFAULT NULL COMMENT '真实姓名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    user_type CHAR(1) DEFAULT '0' COMMENT '用户类型',
    email VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
    phone VARCHAR(11) DEFAULT NULL COMMENT '手机号',
    gender CHAR(1) DEFAULT '0' COMMENT '性别',
    avatar VARCHAR(200) DEFAULT NULL COMMENT '头像路径',
    dept_id BIGINT DEFAULT NULL COMMENT '所属部门',
    login_ip VARCHAR(50) DEFAULT NULL COMMENT '最后登录 IP',
    login_date DATETIME DEFAULT NULL COMMENT '最后登录时间',
    status CHAR(1) DEFAULT '0' COMMENT '状态',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志',
    UNIQUE KEY uk_username (user_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
    role_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色 ID',
    role_name VARCHAR(30) NOT NULL COMMENT '角色名称',
    role_key VARCHAR(30) NOT NULL COMMENT '角色权限字符',
    role_sort INT NOT NULL COMMENT '角色顺序',
    status CHAR(1) DEFAULT '0' COMMENT '状态',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL COMMENT '用户 ID',
    role_id BIGINT NOT NULL COMMENT '角色 ID',
    PRIMARY KEY (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

DROP TABLE IF EXISTS edu_grade;
CREATE TABLE edu_grade (
    grade_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '年级 ID',
    grade_name VARCHAR(20) NOT NULL COMMENT '年级名称',
    grade_code VARCHAR(10) NOT NULL COMMENT '年级编码',
    entrance_year INT NOT NULL COMMENT '入学年份',
    is_current CHAR(1) DEFAULT '0' COMMENT '是否当前年级',
    status CHAR(1) DEFAULT '0' COMMENT '状态',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='年级表';

DROP TABLE IF EXISTS edu_class;
CREATE TABLE edu_class (
    class_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '班级 ID',
    class_name VARCHAR(50) NOT NULL COMMENT '班级名称',
    class_code VARCHAR(20) NOT NULL COMMENT '班级编码',
    grade_id BIGINT NOT NULL COMMENT '所属年级 ID',
    head_teacher_id BIGINT DEFAULT NULL COMMENT '班主任 ID',
    student_count INT DEFAULT 0 COMMENT '学生人数',
    status CHAR(1) DEFAULT '0' COMMENT '状态',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

DROP TABLE IF EXISTS edu_course;
CREATE TABLE edu_course (
    course_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '课程 ID',
    course_code VARCHAR(20) NOT NULL COMMENT '课程代码',
    course_name VARCHAR(100) NOT NULL COMMENT '课程名称',
    credits DECIMAL(3,1) NOT NULL COMMENT '学分',
    hours INT NOT NULL COMMENT '学时',
    course_type CHAR(1) DEFAULT '0' COMMENT '课程类型',
    teacher_id BIGINT DEFAULT NULL COMMENT '主讲教师',
    capacity INT DEFAULT 0 COMMENT '容量',
    selected_count INT DEFAULT 0 COMMENT '已选人数',
    description TEXT DEFAULT NULL COMMENT '课程描述',
    status CHAR(1) DEFAULT '0' COMMENT '状态',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

DROP TABLE IF EXISTS edu_semester;
CREATE TABLE edu_semester (
    semester_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '学期 ID',
    semester_name VARCHAR(50) NOT NULL COMMENT '学期名称',
    semester_code VARCHAR(20) NOT NULL COMMENT '学期编码',
    start_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE NOT NULL COMMENT '结束日期',
    is_current CHAR(1) DEFAULT '0' COMMENT '是否当前学期',
    status CHAR(1) DEFAULT '0' COMMENT '状态',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学期表';

DROP TABLE IF EXISTS edu_student_course;
CREATE TABLE edu_student_course (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键 ID',
    student_id BIGINT NOT NULL COMMENT '学生 ID',
    course_id BIGINT NOT NULL COMMENT '课程 ID',
    semester_id BIGINT NOT NULL COMMENT '学期 ID',
    select_time DATETIME NOT NULL COMMENT '选课时间',
    select_status CHAR(1) DEFAULT '0' COMMENT '选课状态',
    audit_status CHAR(1) DEFAULT '0' COMMENT '审核状态',
    audit_by BIGINT DEFAULT NULL COMMENT '审核人',
    audit_time DATETIME DEFAULT NULL COMMENT '审核时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生选课表';

DROP TABLE IF EXISTS edu_score;
CREATE TABLE edu_score (
    score_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '成绩 ID',
    student_id BIGINT NOT NULL COMMENT '学生 ID',
    course_id BIGINT NOT NULL COMMENT '课程 ID',
    semester_id BIGINT NOT NULL COMMENT '学期 ID',
    regular_score DECIMAL(5,2) DEFAULT NULL COMMENT '平时成绩',
    final_score DECIMAL(5,2) DEFAULT NULL COMMENT '期末成绩',
    total_score DECIMAL(5,2) NOT NULL COMMENT '总成绩',
    grade_point DECIMAL(4,2) DEFAULT NULL COMMENT '绩点',
    grade_level VARCHAR(10) DEFAULT NULL COMMENT '成绩等级',
    credit_earned DECIMAL(3,1) DEFAULT NULL COMMENT '获得学分',
    input_by BIGINT NOT NULL COMMENT '录入人',
    input_time DATETIME NOT NULL COMMENT '录入时间',
    status CHAR(1) DEFAULT '0' COMMENT '状态',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩表';

INSERT INTO sys_department (dept_name, dept_code, dept_type, status) VALUES 
('清华大学', 'THU', 0, '0'),
('计算机学院', 'CS', 1, '0');

INSERT INTO sys_user (user_name, real_name, password, user_type, dept_id, status) VALUES 
('admin', '系统管理员', '$2a$10$N.zxrWvOlqMDvpQfMzKWyO3Yp4JKVLPqN9sVN8bEJxLZLKxLKxLKx', '2', 1, '0'),
('teacher001', '李老师', '$2a$10$N.zxrWvOlqMDvpQfMzKWyO3Yp4JKVLPqN9sVN8bEJxLZLKxLKxLKx', '1', 2, '0'),
('stu001', '张三', '$2a$10$N.zxrWvOlqMDvpQfMzKWyO3Yp4JKVLPqN9sVN8bEJxLZLKxLKxLKx', '0', 2, '0');

INSERT INTO sys_role (role_name, role_key, role_sort, status) VALUES 
('学生', 'student', 1, '0'),
('教师', 'teacher', 2, '0'),
('管理员', 'admin', 3, '0');

INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 3), (2, 2), (3, 1);

INSERT INTO edu_grade (grade_name, grade_code, entrance_year, is_current, status) VALUES 
('2024 级', '2024', 2024, '1', '0');

INSERT INTO edu_class (class_name, class_code, grade_id, student_count, status) VALUES 
('计算机 2024 级 1 班', 'CS202401', 1, 45, '0');

INSERT INTO edu_course (course_code, course_name, credits, hours, course_type, teacher_id, capacity) VALUES 
('CS101', '程序设计基础', 3.0, 48, '0', 2, 100),
('CS201', '数据结构', 4.0, 64, '0', 2, 80);

INSERT INTO edu_semester (semester_name, semester_code, start_date, end_date, is_current, status) VALUES 
('2025-2026 秋季学期', '2025F', '2025-09-01', '2026-01-20', '1', '0');
