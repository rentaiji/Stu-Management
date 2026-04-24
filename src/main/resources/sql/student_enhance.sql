-- 扩展 sys_user 表学生字段
ALTER TABLE sys_user ADD COLUMN gender CHAR(1) DEFAULT '0' COMMENT '性别 0-男 1-女';
ALTER TABLE sys_user ADD COLUMN birth_date DATE COMMENT '出生日期';
ALTER TABLE sys_user ADD COLUMN id_card VARCHAR(18) COMMENT '身份证号';
ALTER TABLE sys_user ADD COLUMN nation VARCHAR(20) DEFAULT '汉' COMMENT '民族';
ALTER TABLE sys_user ADD COLUMN political_status TINYINT DEFAULT 0 COMMENT '政治面貌 0-群众 1-团员 2-党员';
ALTER TABLE sys_user ADD COLUMN native_place VARCHAR(100) COMMENT '生源地';
ALTER TABLE sys_user ADD COLUMN home_address VARCHAR(255) COMMENT '家庭地址';
ALTER TABLE sys_user ADD COLUMN avatar VARCHAR(255) COMMENT '头像/照片URL';

-- 学生扩展表
DROP TABLE IF EXISTS edu_student_ext;
CREATE TABLE edu_student_ext (
    student_id BIGINT PRIMARY KEY COMMENT '学生ID（关联sys_user.user_id）',
    grade_id BIGINT COMMENT '年级ID',
    class_id BIGINT COMMENT '班级ID',
    major_id BIGINT COMMENT '专业ID',
    dept_id BIGINT COMMENT '院系ID',
    education_level CHAR(1) DEFAULT '0' COMMENT '学历层次 0-本科 1-硕士 2-博士',
    study_length INT DEFAULT 4 COMMENT '学制（年）',
    entrance_year INT COMMENT '入学年份',
    student_status CHAR(1) DEFAULT '0' COMMENT '学籍状态 0-在读 1-休学 2-退学 3-毕业',
    emergency_contact VARCHAR(50) COMMENT '紧急联系人',
    emergency_phone VARCHAR(20) COMMENT '紧急联系电话',
    FOREIGN KEY (student_id) REFERENCES sys_user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生扩展信息表';

-- 年级表
DROP TABLE IF EXISTS edu_grade;
CREATE TABLE edu_grade (
    grade_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    grade_name VARCHAR(20) NOT NULL COMMENT '年级名称',
    entrance_year INT NOT NULL COMMENT '入学年份',
    is_current CHAR(1) DEFAULT '0' COMMENT '是否当前年级',
    status CHAR(1) DEFAULT '0' COMMENT '状态'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='年级表';

-- 专业表
DROP TABLE IF EXISTS edu_major;
CREATE TABLE edu_major (
    major_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    major_name VARCHAR(50) NOT NULL COMMENT '专业名称',
    major_code VARCHAR(20) NOT NULL UNIQUE COMMENT '专业代码',
    dept_id BIGINT COMMENT '所属院系ID',
    education_level CHAR(1) DEFAULT '0' COMMENT '学历层次 0-本科 1-硕士 2-博士',
    status CHAR(1) DEFAULT '0' COMMENT '状态'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专业表';

-- 班级表
DROP TABLE IF EXISTS edu_class;
CREATE TABLE edu_class (
    class_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    class_name VARCHAR(50) NOT NULL COMMENT '班级名称',
    class_code VARCHAR(20) NOT NULL UNIQUE COMMENT '班级代码',
    grade_id BIGINT COMMENT '年级ID',
    major_id BIGINT COMMENT '专业ID',
    student_count INT DEFAULT 0 COMMENT '学生人数',
    status CHAR(1) DEFAULT '0' COMMENT '状态'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- 初始化数据
INSERT INTO edu_grade (grade_name, entrance_year, is_current, status) VALUES 
('2024级', 2024, '1', '0'),
('2023级', 2023, '0', '0');

INSERT INTO edu_major (major_name, major_code, dept_id, education_level, status) VALUES 
('计算机科学与技术', 'CS001', 2, '0', '0'),
('软件工程', 'SE001', 2, '0', '0');

INSERT INTO edu_class (class_name, class_code, grade_id, major_id, student_count, status) VALUES 
('计算机2024级1班', 'CS202401', 1, 1, 0, '0'),
('软件2024级1班', 'SE202401', 1, 2, 0, '0');
