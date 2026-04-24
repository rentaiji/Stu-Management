-- 教师工作台新增数据表

-- 1. 通知表
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
    deleted TINYINT DEFAULT 0 COMMENT '删除标志',
    INDEX idx_teacher (teacher_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师通知表';

-- 2. 教师项目经费表
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
    deleted TINYINT DEFAULT 0 COMMENT '删除标志',
    INDEX idx_teacher (teacher_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师项目经费表';

-- 3. 课时费记录表
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
    deleted TINYINT DEFAULT 0 COMMENT '删除标志',
    INDEX idx_teacher (teacher_id),
    INDEX idx_semester (semester),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师课时费记录表';

-- 插入测试数据

-- 测试通知数据
INSERT INTO edu_notice (teacher_id, title, content, target_type, target_id, target_desc, read_count) VALUES
(2, '期中考试安排', '各位同学,期中考试将于第10周进行,请认真复习准备。考试范围:第1-8章内容。', 'course', 1, '计算机基础全体学生', 45),
(2, '作业提交提醒', '第三次作业截止日期为本周五下午5点,请及时提交到系统。', 'course', 1, '数据结构全体学生', 38);

-- 测试项目经费数据
INSERT INTO edu_teacher_fund (teacher_id, project_name, fund_source, total_amount, used_amount, balance, start_date, end_date, status) VALUES
(2, '人工智能研究', '国家自然科学基金', 500000.00, 280000.00, 220000.00, '2024-01-01', '2026-12-31', '0'),
(2, '教学改革项目', '省教育厅', 50000.00, 35000.00, 15000.00, '2024-03-01', '2025-02-28', '0');

-- 测试课时费数据
INSERT INTO edu_teaching_fee (teacher_id, semester, course_id, course_name, hours, rate, amount, status) VALUES
(2, '2024-2025-1', 1, '计算机基础', 48, 100.00, 4800.00, 'paid'),
(2, '2024-2025-1', 2, '数据结构', 64, 120.00, 7680.00, 'pending');
