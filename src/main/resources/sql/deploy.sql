-- =============================================
-- 学生综合服务与教学协同平台 - 完整部署脚本
-- 数据库: MySQL 5.7+
-- 字符集: utf8mb4
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS stu_manage DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE stu_manage;

-- =============================================
-- 1. 系统基础表
-- =============================================

-- 部门表
DROP TABLE IF EXISTS sys_department;
CREATE TABLE sys_department (
    dept_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '部门ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父部门ID',
    dept_name VARCHAR(50) NOT NULL COMMENT '部门名称',
    dept_code VARCHAR(30) NOT NULL COMMENT '部门编码',
    dept_type TINYINT DEFAULT 0 COMMENT '部门类型 0-学校 1-院系 2-专业',
    leader_id BIGINT DEFAULT NULL COMMENT '负责人ID',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    status CHAR(1) DEFAULT '0' COMMENT '状态 0-正常 1-停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 用户表
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    user_name VARCHAR(30) NOT NULL UNIQUE COMMENT '用户名（学号/工号）',
    real_name VARCHAR(30) DEFAULT NULL COMMENT '真实姓名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    user_type CHAR(1) DEFAULT '0' COMMENT '用户类型 0-学生 1-教师 2-管理员',
    email VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    gender CHAR(1) DEFAULT '0' COMMENT '性别 0-男 1-女',
    birth_date DATE COMMENT '出生日期',
    id_card VARCHAR(18) COMMENT '身份证号',
    nation VARCHAR(20) DEFAULT '汉' COMMENT '民族',
    political_status TINYINT DEFAULT 0 COMMENT '政治面貌 0-群众 1-团员 2-党员',
    native_place VARCHAR(100) COMMENT '生源地',
    home_address VARCHAR(255) COMMENT '家庭地址',
    avatar VARCHAR(255) COMMENT '头像/照片URL',
    dept_id BIGINT DEFAULT NULL COMMENT '所属部门',
    login_ip VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
    login_date DATETIME DEFAULT NULL COMMENT '最后登录时间',
    status CHAR(1) DEFAULT '0' COMMENT '状态 0-正常 1-停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
    role_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(30) NOT NULL COMMENT '角色名称',
    role_key VARCHAR(30) NOT NULL COMMENT '角色权限字符',
    role_sort INT NOT NULL COMMENT '角色顺序',
    status CHAR(1) DEFAULT '0' COMMENT '状态 0-正常 1-停用',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 用户角色关联表
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id),
    FOREIGN KEY (role_id) REFERENCES sys_role(role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 菜单表
DROP TABLE IF EXISTS sys_menu;
CREATE TABLE sys_menu (
    menu_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '菜单ID',
    menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    path VARCHAR(200) DEFAULT '' COMMENT '路由地址',
    component VARCHAR(255) DEFAULT NULL COMMENT '组件路径',
    is_frame INT DEFAULT 1 COMMENT '是否为外链 0-是 1-否',
    menu_type CHAR(1) DEFAULT '' COMMENT '菜单类型 M-目录 C-菜单 F-按钮',
    perms VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
    icon VARCHAR(100) DEFAULT '#' COMMENT '菜单图标',
    status CHAR(1) DEFAULT '0' COMMENT '状态 0-正常 1-停用',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- 角色菜单关联表
DROP TABLE IF EXISTS sys_role_menu;
CREATE TABLE sys_role_menu (
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (role_id, menu_id),
    FOREIGN KEY (role_id) REFERENCES sys_role(role_id),
    FOREIGN KEY (menu_id) REFERENCES sys_menu(menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 登录日志表
DROP TABLE IF EXISTS sys_login_log;
CREATE TABLE sys_login_log (
    log_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    user_id BIGINT COMMENT '用户ID',
    username VARCHAR(50) COMMENT '用户名/账号',
    real_name VARCHAR(50) COMMENT '真实姓名',
    user_type VARCHAR(10) COMMENT '用户类型 0-学生 1-教师 2-管理员',
    login_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    user_agent VARCHAR(500) COMMENT '浏览器信息',
    login_status VARCHAR(20) DEFAULT '成功' COMMENT '登录状态 成功/失败',
    fail_reason VARCHAR(200) COMMENT '失败原因',
    operation VARCHAR(200) COMMENT '操作描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_login_time (login_time),
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';

-- =============================================
-- 2. 教务管理表
-- =============================================

-- 年级表
DROP TABLE IF EXISTS edu_grade;
CREATE TABLE edu_grade (
    grade_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '年级ID',
    grade_name VARCHAR(20) NOT NULL COMMENT '年级名称',
    grade_code VARCHAR(10) NOT NULL COMMENT '年级编码',
    entrance_year INT NOT NULL COMMENT '入学年份',
    is_current CHAR(1) DEFAULT '0' COMMENT '是否当前年级 0-否 1-是',
    status CHAR(1) DEFAULT '0' COMMENT '状态 0-正常 1-停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='年级表';

-- 专业表
DROP TABLE IF EXISTS edu_major;
CREATE TABLE edu_major (
    major_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '专业ID',
    major_name VARCHAR(50) NOT NULL COMMENT '专业名称',
    major_code VARCHAR(20) NOT NULL UNIQUE COMMENT '专业代码',
    dept_id BIGINT COMMENT '所属院系ID',
    education_level CHAR(1) DEFAULT '0' COMMENT '学历层次 0-本科 1-硕士 2-博士',
    status CHAR(1) DEFAULT '0' COMMENT '状态 0-正常 1-停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专业表';

-- 班级表
DROP TABLE IF EXISTS edu_class;
CREATE TABLE edu_class (
    class_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '班级ID',
    class_name VARCHAR(50) NOT NULL COMMENT '班级名称',
    class_code VARCHAR(20) NOT NULL UNIQUE COMMENT '班级代码',
    grade_id BIGINT COMMENT '年级ID',
    major_id BIGINT COMMENT '专业ID',
    head_teacher_id BIGINT DEFAULT NULL COMMENT '班主任ID',
    student_count INT DEFAULT 0 COMMENT '学生人数',
    status CHAR(1) DEFAULT '0' COMMENT '状态 0-正常 1-停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- 学期表
DROP TABLE IF EXISTS edu_semester;
CREATE TABLE edu_semester (
    semester_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '学期ID',
    semester_name VARCHAR(50) NOT NULL COMMENT '学期名称',
    semester_code VARCHAR(20) NOT NULL COMMENT '学期编码',
    start_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE NOT NULL COMMENT '结束日期',
    is_current CHAR(1) DEFAULT '0' COMMENT '是否当前学期 0-否 1-是',
    status CHAR(1) DEFAULT '0' COMMENT '状态 0-正常 1-停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学期表';

-- 课程表
DROP TABLE IF EXISTS edu_course;
CREATE TABLE edu_course (
    course_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '课程ID',
    course_code VARCHAR(20) NOT NULL UNIQUE COMMENT '课程代码',
    course_name VARCHAR(100) NOT NULL COMMENT '课程名称',
    credits DECIMAL(3,1) NOT NULL COMMENT '学分',
    hours INT NOT NULL COMMENT '学时',
    course_type CHAR(1) DEFAULT '0' COMMENT '课程类型 0-必修 1-选修',
    teacher_id BIGINT DEFAULT NULL COMMENT '主讲教师ID',
    capacity INT DEFAULT 0 COMMENT '容量',
    selected_count INT DEFAULT 0 COMMENT '已选人数',
    description TEXT DEFAULT NULL COMMENT '课程描述',
    status CHAR(1) DEFAULT '0' COMMENT '状态 0-正常 1-停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 课程发布表
DROP TABLE IF EXISTS edu_course_release;
CREATE TABLE edu_course_release (
    release_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '发布ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    teacher_id BIGINT NOT NULL COMMENT '授课教师ID',
    semester_id BIGINT NOT NULL COMMENT '学期ID',
    class_ids VARCHAR(500) COMMENT '班级IDs,逗号分隔',
    capacity INT DEFAULT 0 COMMENT '课程容量',
    selected_count INT DEFAULT 0 COMMENT '已选人数',
    status CHAR(1) DEFAULT '0' COMMENT '状态 0-未发布 1-已发布 2-已结束',
    publish_time DATETIME DEFAULT NULL COMMENT '发布时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    INDEX idx_course (course_id),
    INDEX idx_teacher (teacher_id),
    INDEX idx_semester (semester_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程发布表';

-- 学生选课表
DROP TABLE IF EXISTS edu_student_course;
CREATE TABLE edu_student_course (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    semester_id BIGINT NOT NULL COMMENT '学期ID',
    select_time DATETIME NOT NULL COMMENT '选课时间',
    select_status CHAR(1) DEFAULT '0' COMMENT '选课状态 0-已选 1-退选',
    audit_status CHAR(1) DEFAULT '0' COMMENT '审核状态 0-待审核 1-已通过 2-已驳回',
    audit_by BIGINT DEFAULT NULL COMMENT '审核人',
    audit_time DATETIME DEFAULT NULL COMMENT '审核时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    INDEX idx_student (student_id),
    INDEX idx_course (course_id),
    INDEX idx_semester (semester_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生选课表';

-- 成绩表
DROP TABLE IF EXISTS edu_score;
CREATE TABLE edu_score (
    score_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '成绩ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    semester_id BIGINT NOT NULL COMMENT '学期ID',
    regular_score DECIMAL(5,2) DEFAULT NULL COMMENT '平时成绩',
    final_score DECIMAL(5,2) DEFAULT NULL COMMENT '期末成绩',
    total_score DECIMAL(5,2) NOT NULL COMMENT '总成绩',
    grade_point DECIMAL(4,2) DEFAULT NULL COMMENT '绩点',
    grade_level VARCHAR(10) DEFAULT NULL COMMENT '成绩等级',
    credit_earned DECIMAL(3,1) DEFAULT NULL COMMENT '获得学分',
    input_by BIGINT NOT NULL COMMENT '录入人',
    input_time DATETIME NOT NULL COMMENT '录入时间',
    status CHAR(1) DEFAULT '0' COMMENT '状态 0-正常 1-异常',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    INDEX idx_student (student_id),
    INDEX idx_course (course_id),
    INDEX idx_semester (semester_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩表';

-- 学生扩展信息表
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

-- =============================================
-- 3. 教师工作台表
-- =============================================

-- 教师基本信息表
DROP TABLE IF EXISTS edu_teacher;
CREATE TABLE edu_teacher (
    teacher_id BIGINT PRIMARY KEY COMMENT '教师ID（关联sys_user.user_id）',
    teacher_no VARCHAR(20) NOT NULL UNIQUE COMMENT '教师工号',
    department VARCHAR(100) COMMENT '所属部门',
    professional_title VARCHAR(50) COMMENT '职称',
    position VARCHAR(50) COMMENT '职务',
    research_direction VARCHAR(200) COMMENT '研究方向',
    office_location VARCHAR(100) COMMENT '办公地点',
    office_phone VARCHAR(20) COMMENT '办公电话',
    FOREIGN KEY (teacher_id) REFERENCES sys_user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师基本信息表';

-- 教师家属表
DROP TABLE IF EXISTS edu_teacher_family;
CREATE TABLE edu_teacher_family (
    family_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '家属ID',
    teacher_id BIGINT NOT NULL COMMENT '教师ID',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    relationship VARCHAR(20) NOT NULL COMMENT '关系',
    birth_date DATE COMMENT '出生日期',
    occupation VARCHAR(100) COMMENT '职业',
    phone VARCHAR(20) COMMENT '联系电话',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    INDEX idx_teacher (teacher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师家属表';

-- 教师科研表
DROP TABLE IF EXISTS edu_teacher_research;
CREATE TABLE edu_teacher_research (
    research_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '科研ID',
    teacher_id BIGINT NOT NULL COMMENT '教师ID',
    project_name VARCHAR(200) NOT NULL COMMENT '项目名称',
    project_type VARCHAR(50) COMMENT '项目类型',
    funding_source VARCHAR(100) COMMENT '资助来源',
    total_funding DECIMAL(12,2) DEFAULT 0 COMMENT '总经费',
    start_date DATE COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    status VARCHAR(20) DEFAULT '进行中' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    INDEX idx_teacher (teacher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师科研表';

-- 通知表
DROP TABLE IF EXISTS edu_notice;
CREATE TABLE edu_notice (
    notice_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '通知ID',
    teacher_id BIGINT NOT NULL COMMENT '发布教师ID',
    title VARCHAR(200) NOT NULL COMMENT '通知标题',
    content TEXT NOT NULL COMMENT '通知内容',
    target_type VARCHAR(20) DEFAULT 'all' COMMENT '通知对象类型 all-全部 course-指定课程 class-指定班级',
    target_id BIGINT DEFAULT NULL COMMENT '目标ID(课程ID或班级ID)',
    target_desc VARCHAR(200) DEFAULT NULL COMMENT '通知对象描述',
    read_count INT DEFAULT 0 COMMENT '已读人数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    INDEX idx_teacher (teacher_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师通知表';

-- 教师项目经费表
DROP TABLE IF EXISTS edu_teacher_fund;
CREATE TABLE edu_teacher_fund (
    fund_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '经费ID',
    teacher_id BIGINT NOT NULL COMMENT '教师ID',
    project_name VARCHAR(200) NOT NULL COMMENT '项目名称',
    fund_source VARCHAR(100) DEFAULT NULL COMMENT '经费来源',
    total_amount DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '总金额',
    used_amount DECIMAL(12,2) DEFAULT 0 COMMENT '已使用金额',
    balance DECIMAL(12,2) DEFAULT 0 COMMENT '余额',
    start_date DATE DEFAULT NULL COMMENT '开始日期',
    end_date DATE DEFAULT NULL COMMENT '结束日期',
    status CHAR(1) DEFAULT '0' COMMENT '状态 0-进行中 1-已结题',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    INDEX idx_teacher (teacher_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师项目经费表';

-- 课时费记录表
DROP TABLE IF EXISTS edu_teaching_fee;
CREATE TABLE edu_teaching_fee (
    fee_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    teacher_id BIGINT NOT NULL COMMENT '教师ID',
    semester VARCHAR(50) NOT NULL COMMENT '学期',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    course_name VARCHAR(100) NOT NULL COMMENT '课程名称',
    hours INT NOT NULL DEFAULT 0 COMMENT '课时数',
    rate DECIMAL(8,2) NOT NULL DEFAULT 0 COMMENT '课时费标准(元/课时)',
    amount DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '金额',
    status VARCHAR(20) DEFAULT 'pending' COMMENT '状态 pending-待发放 paid-已发放',
    pay_date DATE DEFAULT NULL COMMENT '发放日期',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    INDEX idx_teacher (teacher_id),
    INDEX idx_semester (semester),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师课时费记录表';

-- =============================================
-- 4. 竞赛管理表
-- =============================================

-- 通用竞赛获奖表
DROP TABLE IF EXISTS edu_competition_award;
CREATE TABLE edu_competition_award (
    award_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '获奖ID',
    user_id BIGINT NOT NULL COMMENT '用户ID(关联sys_user)',
    competition_name VARCHAR(200) NOT NULL COMMENT '竞赛名称',
    host_institution VARCHAR(200) COMMENT '主办单位',
    award_level VARCHAR(50) NOT NULL COMMENT '获奖级别 国家级/省部级/校级/院级',
    award_rank VARCHAR(50) NOT NULL COMMENT '获奖等级 一等奖/二等奖/三等奖/金奖/银奖/铜奖/优秀奖',
    award_grade VARCHAR(50) COMMENT '获奖级别细分 A类/B类/C类',
    project_name VARCHAR(200) COMMENT '项目名称',
    is_team_award TINYINT(1) DEFAULT 0 COMMENT '是否团队奖 0-个人奖 1-团队奖',
    team_rank VARCHAR(20) COMMENT '团队排名 第一作者/第二作者/成员',
    team_members VARCHAR(500) COMMENT '团队成员,逗号分隔',
    team_size INT COMMENT '团队人数',
    award_time VARCHAR(6) NOT NULL COMMENT '获奖时间,格式:202504',
    certificate_no VARCHAR(100) COMMENT '证书编号',
    track_group VARCHAR(100) COMMENT '赛道/组别',
    instructor1_name VARCHAR(50) COMMENT '指导教师1姓名',
    instructor1_dept VARCHAR(100) COMMENT '指导教师1部门',
    instructor2_name VARCHAR(50) COMMENT '指导教师2姓名',
    instructor2_dept VARCHAR(100) COMMENT '指导教师2部门',
    official_url VARCHAR(500) COMMENT '官方链接',
    data_source VARCHAR(20) DEFAULT '学生自填' COMMENT '数据来源',
    remark VARCHAR(500) COMMENT '备注',
    attachment_url VARCHAR(500) COMMENT '附件路径',
    status VARCHAR(20) DEFAULT '待审核' COMMENT '状态 待审核/已通过/已驳回',
    audit_remark VARCHAR(500) COMMENT '审核备注',
    audit_time DATETIME COMMENT '审批时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-存在 1-删除',
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通用竞赛获奖表';

-- 蓝桥杯获奖表
DROP TABLE IF EXISTS edu_lanqiao_award;
CREATE TABLE edu_lanqiao_award (
    award_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '获奖ID',
    user_id BIGINT NOT NULL COMMENT '用户ID(关联sys_user)',
    competition_session VARCHAR(20) NOT NULL COMMENT '竞赛届次',
    competition_category VARCHAR(50) NOT NULL COMMENT '竞赛类别 软件类/电子类/设计类',
    competition_level VARCHAR(50) NOT NULL COMMENT '竞赛级别 国家级/省部级',
    subject VARCHAR(100) NOT NULL COMMENT '科目 Java/Python/C++/Web/嵌入式/单片机',
    group_type VARCHAR(100) NOT NULL COMMENT '组别 研究生组/大学A组/大学B组/大学C组',
    instructor_name VARCHAR(50) COMMENT '指导教师姓名',
    award VARCHAR(50) NOT NULL COMMENT '获奖等级 一等奖/二等奖/三等奖/优秀奖',
    certificate_no VARCHAR(100) COMMENT '证书编号',
    award_time VARCHAR(6) COMMENT '获奖时间',
    attachment_url VARCHAR(500) COMMENT '附件路径',
    status VARCHAR(20) DEFAULT '待审核' COMMENT '状态 待审核/已通过/已驳回',
    audit_remark VARCHAR(500) COMMENT '审核备注',
    audit_time DATETIME COMMENT '审批时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-存在 1-删除',
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='蓝桥杯获奖表';

-- 竞赛基础信息表
DROP TABLE IF EXISTS edu_competition;
CREATE TABLE edu_competition (
    competition_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '竞赛ID',
    competition_name VARCHAR(200) NOT NULL COMMENT '竞赛名称',
    competition_type VARCHAR(50) COMMENT '竞赛类型',
    host_organization VARCHAR(200) COMMENT '主办单位',
    level VARCHAR(50) COMMENT '竞赛级别',
    start_date DATE COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    description TEXT COMMENT '竞赛描述',
    status CHAR(1) DEFAULT '0' COMMENT '状态 0-进行中 1-已结束',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='竞赛基础信息表';

-- =============================================
-- 5. 科研管理表
-- =============================================

-- 科研项目表
DROP TABLE IF EXISTS edu_research_project;
CREATE TABLE edu_research_project (
    project_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '项目ID',
    teacher_id BIGINT NOT NULL COMMENT '教师ID',
    project_name VARCHAR(200) NOT NULL COMMENT '项目名称',
    project_type VARCHAR(50) COMMENT '项目类型',
    funding_source VARCHAR(100) COMMENT '资助来源',
    total_funding DECIMAL(12,2) DEFAULT 0 COMMENT '总经费',
    start_date DATE COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    status VARCHAR(20) DEFAULT '进行中' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    INDEX idx_teacher (teacher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科研项目表';

-- 科研申请表
DROP TABLE IF EXISTS edu_research_application;
CREATE TABLE edu_research_application (
    application_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '申请ID',
    teacher_id BIGINT NOT NULL COMMENT '教师ID',
    project_name VARCHAR(200) NOT NULL COMMENT '项目名称',
    project_type VARCHAR(50) COMMENT '项目类型',
    funding_amount DECIMAL(12,2) COMMENT '申请经费',
    application_date DATE COMMENT '申请日期',
    status VARCHAR(20) DEFAULT '待审核' COMMENT '状态',
    audit_remark VARCHAR(500) COMMENT '审核意见',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    INDEX idx_teacher (teacher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科研申请表';

-- 科研获奖表
DROP TABLE IF EXISTS edu_research_award;
CREATE TABLE edu_research_award (
    award_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '获奖ID',
    teacher_id BIGINT NOT NULL COMMENT '教师ID',
    award_name VARCHAR(200) NOT NULL COMMENT '奖项名称',
    award_level VARCHAR(50) COMMENT '获奖级别',
    award_date DATE COMMENT '获奖日期',
    certificate_no VARCHAR(100) COMMENT '证书编号',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    INDEX idx_teacher (teacher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科研获奖表';

-- SCI论文表
DROP TABLE IF EXISTS edu_research_sci_paper;
CREATE TABLE edu_research_sci_paper (
    paper_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '论文ID',
    teacher_id BIGINT NOT NULL COMMENT '教师ID',
    title VARCHAR(500) NOT NULL COMMENT '论文标题',
    journal VARCHAR(200) COMMENT '期刊名称',
    publish_date DATE COMMENT '发表日期',
    impact_factor DECIMAL(5,2) COMMENT '影响因子',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    INDEX idx_teacher (teacher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='SCI论文表';

-- CCF论文表
DROP TABLE IF EXISTS edu_research_ccf_paper;
CREATE TABLE edu_research_ccf_paper (
    paper_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '论文ID',
    teacher_id BIGINT NOT NULL COMMENT '教师ID',
    title VARCHAR(500) NOT NULL COMMENT '论文标题',
    conference VARCHAR(200) COMMENT '会议名称',
    ccf_level VARCHAR(20) COMMENT 'CCF等级',
    publish_date DATE COMMENT '发表日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    INDEX idx_teacher (teacher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='CCF论文表';

-- 优秀论文表
DROP TABLE IF EXISTS edu_research_excellent_paper;
CREATE TABLE edu_research_excellent_paper (
    paper_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '论文ID',
    teacher_id BIGINT NOT NULL COMMENT '教师ID',
    title VARCHAR(500) NOT NULL COMMENT '论文标题',
    award_level VARCHAR(50) COMMENT '获奖级别',
    award_date DATE COMMENT '获奖日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    INDEX idx_teacher (teacher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优秀论文表';

-- =============================================
-- 6. 人事档案表
-- =============================================

-- 教职工档案表
DROP TABLE IF EXISTS hr_employee_archive;
CREATE TABLE hr_employee_archive (
    archive_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '档案ID',
    user_id BIGINT COMMENT '用户ID',
    archive_status VARCHAR(20) DEFAULT '未建档' COMMENT '档案状态 未建档/已建档',
    name_pinyin VARCHAR(100) COMMENT '姓名拼音',
    nation VARCHAR(20) COMMENT '民族',
    age INT COMMENT '年龄',
    birth_date_archive DATE COMMENT '出生日期',
    native_place VARCHAR(100) COMMENT '籍贯',
    nationality_region VARCHAR(100) COMMENT '国籍地区',
    marital_status VARCHAR(20) COMMENT '婚姻状况',
    political_status VARCHAR(20) COMMENT '政治面貌',
    join_party_date DATE COMMENT '入党日期',
    join_work_date DATE COMMENT '参加工作日期',
    join_teaching_date DATE COMMENT '从教日期',
    join_school_date DATE COMMENT '入校日期',
    expected_retire_date DATE COMMENT '预计退休日期',
    department VARCHAR(100) COMMENT '部门',
    current_status VARCHAR(20) COMMENT '当前状态',
    personal_status VARCHAR(20) COMMENT '人员状态',
    highest_degree VARCHAR(50) COMMENT '最高学位',
    highest_degree_school VARCHAR(200) COMMENT '最高学位学校',
    highest_degree_major VARCHAR(100) COMMENT '最高学位专业',
    highest_degree_start DATE COMMENT '最高学位开始时间',
    highest_degree_end DATE COMMENT '最高学位结束时间',
    first_degree VARCHAR(50) COMMENT '第一学历',
    first_degree_school VARCHAR(200) COMMENT '第一学历学校',
    first_degree_major VARCHAR(100) COMMENT '第一学历专业',
    first_degree_end DATE COMMENT '第一学历毕业时间',
    discipline_category VARCHAR(50) COMMENT '学科门类',
    professional_title VARCHAR(50) COMMENT '专业技术职务',
    title_appointment_date DATE COMMENT '职务聘任日期',
    professional_title_level VARCHAR(50) COMMENT '专业技术职务等级',
    position VARCHAR(50) COMMENT '岗位',
    position_level VARCHAR(50) COMMENT '岗位等级',
    position_appointment_date DATE COMMENT '岗位聘任日期',
    is_management_post INT DEFAULT 0 COMMENT '是否管理岗',
    is_professional_post INT DEFAULT 0 COMMENT '是否专技岗',
    is_worker_post INT DEFAULT 0 COMMENT '是否工勤岗',
    professional_post_category VARCHAR(50) COMMENT '专技岗位类别',
    employee_category VARCHAR(50) COMMENT '人员类别',
    employee_source VARCHAR(50) COMMENT '人员来源',
    job_type VARCHAR(50) COMMENT '用工形式',
    concurrent_post VARCHAR(100) COMMENT '兼任职务',
    concurrent_post_level VARCHAR(50) COMMENT '兼任职务级别',
    concurrent_post_date DATE COMMENT '兼职时间',
    is_double_shoulder INT DEFAULT 0 COMMENT '是否双肩挑',
    double_shoulder_dept VARCHAR(100) COMMENT '双肩挑部门',
    is_industry_talent INT DEFAULT 0 COMMENT '是否行业人才',
    party_position VARCHAR(100) COMMENT '党政职务',
    party_position_level VARCHAR(50) COMMENT '党政职务级别',
    party_position_date DATE COMMENT '党政职务时间',
    teacher_cert_no VARCHAR(50) COMMENT '教师资格证号',
    teacher_cert_date DATE COMMENT '教师资格证日期',
    is_counselor VARCHAR(10) COMMENT '是否辅导员',
    is_psychology_teacher INT DEFAULT 0 COMMENT '是否心理教师',
    has_psychology_cert INT DEFAULT 0 COMMENT '是否有心理证',
    is_dual_qualified INT DEFAULT 0 COMMENT '是否双师型',
    is_undergrad_teacher INT DEFAULT 0 COMMENT '是否本科生导师',
    talent_title VARCHAR(100) COMMENT '人才称号',
    talent_appointment_date DATE COMMENT '人才聘任日期',
    first_employment_date DATE COMMENT '首次聘用日期',
    regularization_date DATE COMMENT '转正日期',
    introduction_level VARCHAR(50) COMMENT '引进层次',
    special_appointment_level VARCHAR(50) COMMENT '特聘层级',
    special_contract_date DATE COMMENT '特聘合同日期',
    special_contract_file VARCHAR(255) COMMENT '特聘合同文件',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    address VARCHAR(255) COMMENT '地址',
    emergency_contact VARCHAR(50) COMMENT '紧急联系人',
    emergency_contact_phone VARCHAR(20) COMMENT '紧急联系人电话',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教职工档案表';

-- 教职工教育经历表
DROP TABLE IF EXISTS hr_employee_education;
CREATE TABLE hr_employee_education (
    education_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '教育ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    school VARCHAR(200) NOT NULL COMMENT '学校',
    major VARCHAR(100) COMMENT '专业',
    degree VARCHAR(50) COMMENT '学位',
    start_date DATE COMMENT '开始时间',
    end_date DATE COMMENT '结束时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教职工教育经历表';

-- =============================================
-- 7. 初始化数据
-- =============================================

-- 插入部门数据
INSERT INTO sys_department (dept_name, dept_code, dept_type, status) VALUES 
('清华大学', 'THU', 0, '0'),
('计算机学院', 'CS', 1, '0');

-- 插入用户数据 (密码均为: admin123)
INSERT INTO sys_user (user_name, real_name, password, user_type, dept_id, status) VALUES 
('admin', '系统管理员', '$2a$10$N.zxrWvOlqMDvpQfMzKWyO3Yp4JKVLPqN9sVN8bEJxLZLKxLKxLKx', '2', 1, '0'),
('T001', '李老师', '$2a$10$N.zxrWvOlqMDvpQfMzKWyO3Yp4JKVLPqN9sVN8bEJxLZLKxLKxLKx', '1', 2, '0'),
('2024001', '张三', '$2a$10$N.zxrWvOlqMDvpQfMzKWyO3Yp4JKVLPqN9sVN8bEJxLZLKxLKxLKx', '0', 2, '0');

-- 插入角色数据
INSERT INTO sys_role (role_name, role_key, role_sort, status) VALUES 
('超级管理员', 'admin', 1, '0'),
('学校教务管理员', 'school_academic', 2, '0'),
('院系教务秘书', 'dept_academic', 3, '0'),
('普通教师', 'teacher', 4, '0'),
('系主任', 'dept_head', 5, '0'),
('辅导员', 'counselor', 6, '0'),
('学生', 'student', 7, '0');

-- 插入用户角色关联
INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1), (2, 4), (3, 7);

-- 插入年级数据
INSERT INTO edu_grade (grade_name, grade_code, entrance_year, is_current, status) VALUES 
('2024级', '2024', 2024, '1', '0');

-- 插入专业数据
INSERT INTO edu_major (major_name, major_code, dept_id, education_level, status) VALUES 
('计算机科学与技术', 'CS001', 2, '0', '0'),
('软件工程', 'SE001', 2, '0', '0');

-- 插入班级数据
INSERT INTO edu_class (class_name, class_code, grade_id, major_id, student_count, status) VALUES 
('计算机2024级1班', 'CS202401', 1, 1, 0, '0'),
('软件2024级1班', 'SE202401', 1, 2, 0, '0');

-- 插入课程数据
INSERT INTO edu_course (course_code, course_name, credits, hours, course_type, teacher_id, capacity) VALUES 
('CS101', '程序设计基础', 3.0, 48, '0', 2, 100),
('CS201', '数据结构', 4.0, 64, '0', 2, 80);

-- 插入学期数据
INSERT INTO edu_semester (semester_name, semester_code, start_date, end_date, is_current, status) VALUES 
('2025-2026秋季学期', '2025F', '2025-09-01', '2026-01-20', '1', '0');
