-- =============================================
-- 学生综合服务与教学协同平台 - 完整部署脚本
-- 版本: v1.0
-- 数据库: MySQL 5.7+ / 8.0+
-- 字符集: utf8mb4
-- 说明: 执行此脚本前请确保MySQL服务已启动
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS stu_manage DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE stu_manage;

-- =============================================
-- 第一部分：系统基础表
-- =============================================

-- 1. 部门表
DROP TABLE IF EXISTS sys_department;
CREATE TABLE sys_department (
    dept_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '部门ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父部门ID',
    dept_name VARCHAR(50) NOT NULL COMMENT '部门名称',
    dept_code VARCHAR(30) NOT NULL COMMENT '部门编码',
    dept_type TINYINT DEFAULT 0 COMMENT '部门类型 0-学校 1-学院 2-系',
    leader_id BIGINT DEFAULT NULL COMMENT '负责人ID',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    status CHAR(1) DEFAULT '0' COMMENT '状态 0-正常 1-停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 2. 用户表
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

-- 3. 角色表
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

-- 4. 用户角色关联表
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id),
    FOREIGN KEY (role_id) REFERENCES sys_role(role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 5. 菜单表
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

-- 6. 角色菜单关联表
DROP TABLE IF EXISTS sys_role_menu;
CREATE TABLE sys_role_menu (
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (role_id, menu_id),
    FOREIGN KEY (role_id) REFERENCES sys_role(role_id),
    FOREIGN KEY (menu_id) REFERENCES sys_menu(menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 7. 登录日志表
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
-- 第二部分：教务管理表
-- =============================================

-- 8. 年级表
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

-- 9. 专业表
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

-- 10. 班级表
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

-- 11. 学期表
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

-- 12. 课程表
DROP TABLE IF EXISTS edu_course;
CREATE TABLE edu_course (
    course_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '课程ID',
    course_code VARCHAR(20) NOT NULL UNIQUE COMMENT '课程代码',
    course_name VARCHAR(100) NOT NULL COMMENT '课程名称',
    credits DECIMAL(3,1) NOT NULL COMMENT '学分',
    hours INT NOT NULL COMMENT '学时',
    course_type CHAR(1) DEFAULT '0' COMMENT '课程类型 0-必修 1-选修',
    teacher_id BIGINT DEFAULT NULL COMMENT '主讲教师ID',
    capacity INT DEFAULT 100 COMMENT '容量',
    status CHAR(1) DEFAULT '0' COMMENT '状态 0-正常 1-停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 13. 课程发布表
DROP TABLE IF EXISTS edu_course_release;
CREATE TABLE edu_course_release (
    release_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '发布ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    semester_id BIGINT NOT NULL COMMENT '学期ID',
    teacher_id BIGINT NOT NULL COMMENT '授课教师ID',
    capacity INT NOT NULL COMMENT '容量',
    selected_count INT DEFAULT 0 COMMENT '已选人数',
    class_time VARCHAR(100) COMMENT '上课时间',
    class_location VARCHAR(100) COMMENT '上课地点',
    weeks_range VARCHAR(50) COMMENT '周次范围',
    status CHAR(1) DEFAULT '0' COMMENT '状态 0-正常 1-关闭',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    FOREIGN KEY (course_id) REFERENCES edu_course(course_id),
    FOREIGN KEY (semester_id) REFERENCES edu_semester(semester_id),
    FOREIGN KEY (teacher_id) REFERENCES sys_user(user_id),
    UNIQUE KEY uk_course_semester (course_id, semester_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程发布表';

-- 14. 学生选课表
DROP TABLE IF EXISTS edu_student_course;
CREATE TABLE edu_student_course (
    enroll_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '选课ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    release_id BIGINT NOT NULL COMMENT '课程发布ID',
    enroll_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
    status CHAR(1) DEFAULT '0' COMMENT '状态 0-正常 1-退课',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    FOREIGN KEY (student_id) REFERENCES sys_user(user_id),
    FOREIGN KEY (release_id) REFERENCES edu_course_release(release_id),
    UNIQUE KEY uk_student_release (student_id, release_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生选课表';

-- 15. 成绩表
DROP TABLE IF EXISTS edu_score;
CREATE TABLE edu_score (
    score_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '成绩ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    release_id BIGINT NOT NULL COMMENT '课程发布ID',
    semester_id BIGINT NOT NULL COMMENT '学期ID',
    regular_score DECIMAL(5,2) COMMENT '平时成绩',
    midterm_score DECIMAL(5,2) COMMENT '期中成绩',
    final_score DECIMAL(5,2) COMMENT '期末成绩',
    total_score DECIMAL(5,2) COMMENT '总评成绩',
    status CHAR(1) DEFAULT '0' COMMENT '状态 0-草稿 1-待审核 2-已发布 3-已打回',
    audit_remark VARCHAR(500) COMMENT '审核意见',
    audit_by BIGINT COMMENT '审核人ID',
    audit_time DATETIME COMMENT '审核时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    FOREIGN KEY (student_id) REFERENCES sys_user(user_id),
    FOREIGN KEY (release_id) REFERENCES edu_course_release(release_id),
    FOREIGN KEY (semester_id) REFERENCES edu_semester(semester_id),
    UNIQUE KEY uk_student_release (student_id, release_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩表';

-- =============================================
-- 第三部分：学生扩展信息表
-- =============================================

-- 16. 学生扩展信息表
DROP TABLE IF EXISTS edu_student_ext;
CREATE TABLE edu_student_ext (
    ext_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '扩展ID',
    user_id BIGINT NOT NULL UNIQUE COMMENT '用户ID',
    grade_id BIGINT COMMENT '年级ID',
    class_id BIGINT COMMENT '班级ID',
    major_id BIGINT COMMENT '专业ID',
    dept_id BIGINT COMMENT '院系ID',
    entrance_year INT COMMENT '入学年份',
    study_length INT COMMENT '学制',
    education_level CHAR(1) DEFAULT '0' COMMENT '学历层次 0-本科 1-硕士 2-博士',
    entry_date DATE COMMENT '入学日期',
    graduate_date DATE COMMENT '预计毕业日期',
    student_status CHAR(1) DEFAULT '0' COMMENT '学籍状态 0-在读 1-休学 2-退学 3-毕业',
    study_mode TINYINT DEFAULT 0 COMMENT '学习形式 0-全日制 1-非全日制',
    emergency_contact VARCHAR(50) COMMENT '紧急联系人',
    emergency_phone VARCHAR(20) COMMENT '紧急联系电话',
    birth_place VARCHAR(100) COMMENT '出生地',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生扩展信息表';

-- =============================================
-- 第四部分：科研管理表
-- =============================================

-- 17. 科研论文表
DROP TABLE IF EXISTS res_paper;
CREATE TABLE res_paper (
    paper_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '论文ID',
    teacher_id BIGINT NOT NULL COMMENT '申报教师ID',
    title VARCHAR(500) NOT NULL COMMENT '论文标题',
    journal_name VARCHAR(200) NOT NULL COMMENT '期刊名称',
    author_info VARCHAR(500) NOT NULL COMMENT '作者信息',
    corresponding_author VARCHAR(100) COMMENT '通讯作者',
    paper_type VARCHAR(20) NOT NULL COMMENT '论文类型',
    conference_type VARCHAR(50) COMMENT '会议类型',
    sci_zone VARCHAR(10) COMMENT 'SCI分区',
    ccf_zone VARCHAR(10) COMMENT 'CCF分区',
    is_recommended_journal TINYINT(1) DEFAULT 0 COMMENT '是否推荐期刊',
    journal_type VARCHAR(50) COMMENT '期刊类型',
    is_excellent_journal TINYINT(1) DEFAULT 0 COMMENT '是否卓越期刊',
    is_highly_cited TINYINT(1) DEFAULT 0 COMMENT '是否高被引论文',
    is_esi_paper TINYINT(1) DEFAULT 0 COMMENT '是否是ESI论文',
    is_qdu_first_unit TINYINT(1) DEFAULT 0 COMMENT '是否青大第一单位',
    reward_level VARCHAR(50) COMMENT '奖励级别',
    attribution VARCHAR(100) COMMENT '归属',
    points DECIMAL(10,2) COMMENT '分值',
    publish_date DATE NOT NULL COMMENT '发表日期',
    remark VARCHAR(500) COMMENT '备注',
    pdf_url VARCHAR(500) COMMENT '论文PDF路径',
    proof_url VARCHAR(500) COMMENT '收录证明路径',
    status VARCHAR(20) DEFAULT '待审核' COMMENT '状态:待审核/已通过/已驳回',
    audit_remark VARCHAR(500) COMMENT '审核意见',
    audit_time DATETIME COMMENT '审核时间',
    auditor_id BIGINT COMMENT '审核人ID',
    submit_time DATETIME COMMENT '提交时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志',
    INDEX idx_teacher_id (teacher_id),
    INDEX idx_status (status),
    FOREIGN KEY (teacher_id) REFERENCES sys_user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科研论文申报表';

-- 18. 科研项目表
DROP TABLE IF EXISTS res_project;
CREATE TABLE res_project (
    project_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '项目ID',
    teacher_id BIGINT NOT NULL COMMENT '申报教师ID',
    project_no VARCHAR(100) NOT NULL UNIQUE COMMENT '项目编号',
    project_name VARCHAR(200) NOT NULL COMMENT '项目名称',
    source VARCHAR(100) NOT NULL COMMENT '项目来源',
    sign_year VARCHAR(10) NOT NULL COMMENT '签订年份',
    sign_date DATE NOT NULL COMMENT '签订日期',
    end_date DATE NOT NULL COMMENT '结束日期',
    contract_amount DECIMAL(12,2) NOT NULL COMMENT '合同金额(万元)',
    received_amount DECIMAL(12,2) DEFAULT 0 COMMENT '到账金额(万元)',
    approval_url VARCHAR(500) COMMENT '立项通知书路径',
    contract_url VARCHAR(500) COMMENT '合同文件路径',
    completion_url VARCHAR(500) COMMENT '结题证明路径',
    status VARCHAR(20) DEFAULT '待审核' COMMENT '状态:待审核/已通过/已驳回',
    audit_remark VARCHAR(500) COMMENT '审核意见',
    audit_time DATETIME COMMENT '审核时间',
    auditor_id BIGINT COMMENT '审核人ID',
    submit_time DATETIME COMMENT '提交时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志',
    INDEX idx_teacher_id (teacher_id),
    INDEX idx_status (status),
    FOREIGN KEY (teacher_id) REFERENCES sys_user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科研项目申报表';

-- 19. 科研奖励表
DROP TABLE IF EXISTS res_science_award;
CREATE TABLE res_science_award (
    award_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '奖励ID',
    teacher_id BIGINT NOT NULL COMMENT '申报教师ID',
    award_name VARCHAR(200) NOT NULL COMMENT '奖励名称',
    achievement_name VARCHAR(200) COMMENT '成果名称',
    award_level VARCHAR(50) NOT NULL COMMENT '奖励级别',
    award_date DATE NOT NULL COMMENT '获奖日期',
    all_completers VARCHAR(500) NOT NULL COMMENT '全部完成人',
    award_rank VARCHAR(50) NOT NULL COMMENT '获奖排名',
    unit_ranking VARCHAR(20) COMMENT '单位排名',
    issuing_authority VARCHAR(200) COMMENT '颁发机构',
    certificate_no VARCHAR(100) COMMENT '证书编号',
    main_achievement TEXT COMMENT '主要成果描述',
    certificate_url VARCHAR(500) COMMENT '获奖证书路径',
    publicity_url VARCHAR(500) COMMENT '公示文件路径',
    status VARCHAR(20) DEFAULT '待审核' COMMENT '状态:待审核/已通过/已驳回',
    audit_remark VARCHAR(500) COMMENT '审核意见',
    audit_time DATETIME COMMENT '审核时间',
    auditor_id BIGINT COMMENT '审核人ID',
    submit_time DATETIME COMMENT '提交时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志',
    INDEX idx_teacher_id (teacher_id),
    INDEX idx_status (status),
    FOREIGN KEY (teacher_id) REFERENCES sys_user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='科研奖励申报表';

-- =============================================
-- 第五部分：竞赛管理表
-- =============================================

-- 20. 竞赛申报表
DROP TABLE IF EXISTS edu_competition;
CREATE TABLE edu_competition (
    competition_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '竞赛ID',
    student_id BIGINT NOT NULL COMMENT '申报学生ID',
    competition_name VARCHAR(200) NOT NULL COMMENT '竞赛名称',
    competition_level VARCHAR(50) NOT NULL COMMENT '竞赛级别',
    competition_date DATE COMMENT '竞赛时间',
    team_members VARCHAR(500) COMMENT '团队成员',
    advisor VARCHAR(100) COMMENT '指导教师',
    reason TEXT COMMENT '申报理由',
    attachment_url VARCHAR(500) COMMENT '附件路径',
    status CHAR(1) DEFAULT '0' COMMENT '状态 0-待审批 1-已通过 2-已拒绝',
    audit_remark VARCHAR(500) COMMENT '审批意见',
    audit_by BIGINT COMMENT '审批人ID',
    audit_time DATETIME COMMENT '审批时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    FOREIGN KEY (student_id) REFERENCES sys_user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='竞赛申报表';

-- 21. 竞赛获奖表
DROP TABLE IF EXISTS edu_competition_award;
CREATE TABLE edu_competition_award (
    award_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '获奖ID',
    competition_id BIGINT NOT NULL COMMENT '竞赛ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    award_level VARCHAR(50) NOT NULL COMMENT '获奖等级',
    award_date DATE NOT NULL COMMENT '获奖日期',
    certificate_no VARCHAR(100) COMMENT '证书编号',
    certificate_url VARCHAR(500) COMMENT '证书路径',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志 0-存在 1-删除',
    FOREIGN KEY (competition_id) REFERENCES edu_competition(competition_id),
    FOREIGN KEY (student_id) REFERENCES sys_user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='竞赛获奖表';

-- =============================================
-- 第六部分：人事档案表
-- =============================================

-- 22. 教职工档案主表
DROP TABLE IF EXISTS hr_employee_archive;
CREATE TABLE hr_employee_archive (
    archive_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '档案ID',
    user_id BIGINT NOT NULL COMMENT '用户ID(关联sys_user)',
    name_pinyin VARCHAR(100) COMMENT '姓名拼音',
    nation VARCHAR(50) COMMENT '民族',
    age INT COMMENT '年龄(自动计算)',
    birth_date_archive DATE COMMENT '档案出生日期',
    native_place VARCHAR(100) COMMENT '籍贯',
    nationality_region VARCHAR(50) COMMENT '国籍(地区)',
    marital_status VARCHAR(10) COMMENT '婚否',
    political_status VARCHAR(50) COMMENT '党派/政治面貌',
    join_party_date DATE COMMENT '入党/参加党派日期',
    join_work_date DATE COMMENT '参加工作年月',
    join_teaching_date DATE COMMENT '从教年月',
    join_school_date DATE COMMENT '来校/入校时间',
    expected_retire_date DATE COMMENT '拟退休/预计离退休日期',
    department VARCHAR(100) COMMENT '部门/所在部门',
    current_status VARCHAR(20) DEFAULT '在职' COMMENT '当前状态',
    personal_status VARCHAR(50) COMMENT '个人身份',
    highest_degree VARCHAR(50) COMMENT '最高学位',
    highest_degree_school VARCHAR(100) COMMENT '最高学历毕业院校',
    highest_degree_major VARCHAR(100) COMMENT '所学专业',
    highest_degree_start DATE COMMENT '最高学历开始时间',
    highest_degree_end DATE COMMENT '最高学历毕业时间',
    first_degree VARCHAR(50) COMMENT '第一学历',
    first_degree_school VARCHAR(100) COMMENT '毕业院校',
    first_degree_major VARCHAR(100) COMMENT '所学专业',
    first_degree_end DATE COMMENT '毕业时间',
    discipline_category VARCHAR(50) COMMENT '所属门类',
    professional_title VARCHAR(50) COMMENT '职称/专业技术职务',
    title_appointment_date DATE COMMENT '评聘/评定年月',
    professional_title_level VARCHAR(50) COMMENT '专业技术职务级别',
    position VARCHAR(50) COMMENT '岗位/聘任岗位',
    position_level VARCHAR(50) COMMENT '岗位聘任等级',
    position_appointment_date DATE COMMENT '现岗位/岗位聘任时间',
    is_management_post TINYINT(1) DEFAULT 0 COMMENT '是否管理岗位',
    is_professional_post TINYINT(1) DEFAULT 0 COMMENT '是否专技岗位',
    is_worker_post TINYINT(1) DEFAULT 0 COMMENT '是否工勤岗位',
    professional_post_category VARCHAR(50) COMMENT '专业技术岗位类别',
    employee_category VARCHAR(50) COMMENT '分类/教职工类别',
    employee_source VARCHAR(50) COMMENT '教职工来源',
    job_type VARCHAR(50) COMMENT '教师类型',
    concurrent_post VARCHAR(100) COMMENT '兼聘岗位',
    concurrent_post_level VARCHAR(50) COMMENT '兼聘岗位等级',
    concurrent_post_date DATE COMMENT '兼聘日期',
    is_double_shoulder TINYINT(1) DEFAULT 0 COMMENT '是否双肩挑',
    double_shoulder_dept VARCHAR(100) COMMENT '双肩挑所在单位',
    is_industry_talent TINYINT(1) DEFAULT 0 COMMENT '是否产业人员',
    party_position VARCHAR(100) COMMENT '党政职务',
    party_position_level VARCHAR(50) COMMENT '党政职务级别',
    party_position_date DATE COMMENT '任职时间',
    teacher_cert_no VARCHAR(50) COMMENT '教师资格证号',
    teacher_cert_date DATE COMMENT '教师资格获得日期',
    is_counselor VARCHAR(10) COMMENT '辅导员类别',
    is_psychology_teacher TINYINT(1) DEFAULT 0 COMMENT '是否专职从事心理咨询工作',
    has_psychology_cert TINYINT(1) DEFAULT 0 COMMENT '是否持有心理咨询资格证书',
    is_dual_qualified TINYINT(1) DEFAULT 0 COMMENT '是否双师型教师',
    is_undergrad_teacher TINYINT(1) DEFAULT 0 COMMENT '是否为本科生上课',
    talent_title VARCHAR(100) COMMENT '人才称号',
    talent_appointment_date DATE COMMENT '人才评聘时间',
    first_employment_date DATE COMMENT '首聘期日期',
    regularization_date DATE COMMENT '转正日期',
    introduction_level VARCHAR(50) COMMENT '引进层次',
    special_appointment_level VARCHAR(50) COMMENT '特聘层次',
    special_contract_date DATE COMMENT '特聘合同日期',
    special_contract_file VARCHAR(500) COMMENT '特聘合同上传路径',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    address VARCHAR(200) COMMENT '住址',
    emergency_contact VARCHAR(50) COMMENT '紧急联系人',
    emergency_contact_phone VARCHAR(20) COMMENT '紧急联系人手机号',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    UNIQUE KEY uk_user_id (user_id),
    INDEX idx_user_id (user_id),
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教职工人事档案主表';

-- 23. 教职工教育经历表
DROP TABLE IF EXISTS hr_employee_education;
CREATE TABLE hr_employee_education (
    education_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    archive_id BIGINT NOT NULL COMMENT '档案ID',
    degree_type VARCHAR(50) COMMENT '学历/学位类型',
    school_name VARCHAR(100) COMMENT '毕业院校',
    major VARCHAR(100) COMMENT '所学专业',
    start_date DATE COMMENT '开始时间',
    end_date DATE COMMENT '毕业时间',
    degree_cert_no VARCHAR(50) COMMENT '学位证书编号',
    degree_cert_url VARCHAR(500) COMMENT '学位证书路径',
    diploma_cert_no VARCHAR(50) COMMENT '毕业证书编号',
    diploma_cert_url VARCHAR(500) COMMENT '毕业证书路径',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_archive_id (archive_id),
    FOREIGN KEY (archive_id) REFERENCES hr_employee_archive(archive_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学历学位子表';

-- =============================================
-- 第七部分：初始化数据
-- =============================================

-- 1. 插入部门数据
INSERT INTO sys_department (dept_name, dept_code, dept_type, status) VALUES 
('清华大学', 'THU', 0, '0'),
('计算机学院', 'CS', 1, '0'),
('软件学院', 'SE', 1, '0');

-- 2. 插入用户数据
-- 默认密码均为: admin123
-- BCrypt加密后的密码哈希值
INSERT INTO sys_user (user_name, real_name, password, user_type, dept_id, phone, email, status) VALUES 
('admin', '系统管理员', 'admin123', '2', 1, '13800138000', 'admin@stu-manage.edu.cn', '0'),
('T001', '李老师', '123456', '1', 2, '13800138001', 'teacher@stu-manage.edu.cn', '0'),
('T002', '王老师', '123456', '1', 2, '13800138002', 'wang@stu-manage.edu.cn', '0'),
('2024001', '张三', '123456', '0', 2, '13800138003', 'zhangsan@stu-manage.edu.cn', '0'),
('2024002', '李四', '123456', '0', 2, '13800138004', 'lisi@stu-manage.edu.cn', '0');

-- 3. 插入角色数据
INSERT INTO sys_role (role_name, role_key, role_sort, status) VALUES 
('超级管理员', 'admin', 1, '0'),
('学校教务管理员', 'school_academic', 2, '0'),
('院系教务秘书', 'dept_academic', 3, '0'),
('普通教师', 'teacher', 4, '0'),
('系主任', 'dept_head', 5, '0'),
('辅导员', 'counselor', 6, '0'),
('学生', 'student', 7, '0');

-- 4. 插入用户角色关联
INSERT INTO sys_user_role (user_id, role_id) VALUES 
(1, 1),   -- admin -> 超级管理员
(2, 4),   -- T001 -> 普通教师
(3, 4),   -- T002 -> 普通教师
(4, 7),   -- 2024001 -> 学生
(5, 7);   -- 2024002 -> 学生

-- 5. 插入菜单数据（简化版，实际使用时可根据需要扩展）
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, menu_type, perms, icon, status) VALUES 
('系统管理', 0, 1, 'system', 'M', NULL, 'el-icon-setting', '0'),
('学生管理', 1, 1, 'student', 'C', 'student:list', 'el-icon-user', '0'),
('课程管理', 1, 2, 'course', 'C', 'course:list', 'el-icon-reading', '0'),
('教师管理', 1, 3, 'teacher', 'C', 'teacher:list', 'el-icon-s-custom', '0'),
('部门管理', 1, 4, 'department', 'C', 'dept:list', 'el-icon-office-building', '0'),
('权限查看', 1, 5, 'permission', 'C', 'permission:view', 'el-icon-s-check', '0'),
('登录日志', 1, 6, 'login-log', 'C', 'log:list', 'el-icon-document-copy', '0'),
('人事档案', 1, 7, 'hr-archive', 'C', 'hr:view', 'el-icon-document', '0'),
('教学工作', 0, 2, 'teaching', 'M', NULL, 'el-icon-s-opportunity', '0'),
('我的课程', 9, 1, 'my-course', 'C', 'academic:course:list', 'el-icon-reading', '0'),
('成绩管理', 9, 2, 'score', 'C', 'academic:score:input', 'el-icon-edit', '0'),
('学生名单', 9, 3, 'students', 'C', 'student:list', 'el-icon-user', '0'),
('科研申报', 9, 4, 'research', 'C', 'research:view', 'el-icon-document', '0'),
('课程发布', 9, 5, 'course-release', 'C', 'academic:course:release', 'el-icon-plus', '0'),
('科研审核', 9, 6, 'research-audit', 'C', 'research:audit', 'el-icon-document-checked', '0'),
('竞赛审批', 9, 7, 'competition-approve', 'C', 'competition:approve', 'el-icon-trophy', '0'),
('学生学习', 0, 3, 'study', 'M', NULL, 'el-icon-s-flag', '0'),
('个人信息', 17, 1, 'myinfo', 'C', 'student:profile', 'el-icon-user', '0'),
('我的课程', 17, 2, 'my-course', 'C', 'student:course', 'el-icon-reading', '0'),
('我的成绩', 17, 3, 'my-score', 'C', 'student:score', 'el-icon-edit', '0'),
('选课中心', 17, 4, 'select', 'C', 'student:select', 'el-icon-circle-check', '0'),
('课表查询', 17, 5, 'schedule', 'C', 'student:schedule', 'el-icon-time', '0'),
('信息维护', 17, 6, 'profile', 'C', 'student:edit', 'el-icon-setting', '0'),
('竞赛申报', 17, 7, 'competition', 'C', 'student:competition', 'el-icon-trophy', '0');

-- 6. 为超级管理员分配所有菜单权限
INSERT INTO sys_role_menu (role_id, menu_id) 
SELECT 1, menu_id FROM sys_menu WHERE deleted = 0;

-- 7. 为普通教师分配菜单权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES 
(4, 10),  -- 我的课程
(4, 11),  -- 成绩管理
(4, 12),  -- 学生名单
(4, 13);  -- 科研申报

-- 8. 为学生分配菜单权限
INSERT INTO sys_role_menu (role_id, menu_id) VALUES 
(7, 18),  -- 个人信息
(7, 19),  -- 我的课程
(7, 20),  -- 我的成绩
(7, 21),  -- 选课中心
(7, 22),  -- 课表查询
(7, 23),  -- 信息维护
(7, 24);  -- 竞赛申报

-- 9. 插入年级数据
INSERT INTO edu_grade (grade_name, grade_code, entrance_year, is_current, status) VALUES 
('2024级', '2024', 2024, '1', '0'),
('2023级', '2023', 2023, '0', '0'),
('2022级', '2022', 2022, '0', '0');

-- 10. 插入专业数据
INSERT INTO edu_major (major_name, major_code, dept_id, education_level, status) VALUES 
('计算机科学与技术', 'CS001', 2, '0', '0'),
('软件工程', 'SE001', 3, '0', '0'),
('人工智能', 'AI001', 2, '0', '0');

-- 11. 插入班级数据
INSERT INTO edu_class (class_name, class_code, grade_id, major_id, student_count, status) VALUES 
('计算机2024级1班', 'CS202401', 1, 1, 2, '0'),
('软件2024级1班', 'SE202401', 1, 2, 0, '0'),
('计算机2023级1班', 'CS202301', 2, 1, 0, '0');

-- 12. 插入课程数据
INSERT INTO edu_course (course_code, course_name, credits, hours, course_type, teacher_id, capacity, status) VALUES 
('CS101', '程序设计基础', 3.0, 48, '0', 2, 100, '0'),
('CS201', '数据结构', 4.0, 64, '0', 2, 80, '0'),
('CS301', '操作系统', 3.5, 56, '0', 3, 90, '0'),
('SE101', '软件工程导论', 2.5, 40, '0', 3, 100, '0');

-- 13. 插入学期数据
INSERT INTO edu_semester (semester_name, semester_code, start_date, end_date, is_current, status) VALUES 
('2025-2026秋季学期', '2025F', '2025-09-01', '2026-01-20', '1', '0'),
('2025-2026春季学期', '2026S', '2026-02-20', '2026-07-10', '0', '0');

-- 14. 插入课程发布数据
INSERT INTO edu_course_release (course_id, semester_id, teacher_id, capacity, selected_count, class_time, class_location, weeks_range, status) VALUES 
(1, 1, 2, 100, 2, '周一 1-2节, 周三 3-4节', '教学楼A101', '1-16周', '0'),
(2, 1, 2, 80, 0, '周二 1-2节, 周四 3-4节', '教学楼B202', '1-16周', '0'),
(3, 1, 3, 90, 0, '周一 5-6节, 周三 7-8节', '教学楼C303', '1-16周', '0');

-- 15. 插入学生选课数据
INSERT INTO edu_student_course (student_id, release_id, enroll_time, status) VALUES 
(4, 1, NOW(), '0'),  -- 张三选了CS101
(5, 1, NOW(), '0');  -- 李四选了CS101

-- 16. 插入学生扩展信息
INSERT INTO edu_student_ext (user_id, grade_id, class_id, major_id, dept_id, entrance_year, study_length, education_level, entry_date, graduate_date, student_status, study_mode, emergency_contact, emergency_phone, birth_place) VALUES 
(4, 1, 1, 1, 2, 2024, 4, '0', '2024-09-01', '2028-06-30', '0', 0, '张父', '13900139001', '北京'),
(5, 1, 1, 1, 2, 2024, 4, '0', '2024-09-01', '2028-06-30', '0', 0, '李母', '13900139002', '上海');

-- =============================================
-- 部署完成提示
-- =============================================
SELECT '====================================' AS message;
SELECT '数据库部署完成！' AS message;
SELECT '====================================' AS message;
SELECT '默认账号信息：' AS message;
SELECT '管理员 - 账号: admin, 密码: admin123' AS account;
SELECT '教师 - 账号: T001, 密码: 123456' AS account;
SELECT '教师 - 账号: T002, 密码: 123456' AS account;
SELECT '学生 - 账号: 2024001, 密码: 123456' AS account;
SELECT '学生 - 账号: 2024002, 密码: 123456' AS account;
SELECT '====================================' AS message;
SELECT '访问地址：http://localhost:8080/index.html' AS message;
SELECT '====================================' AS message;
