-- 科研申报模块数据表

-- 1. 论文表
DROP TABLE IF EXISTS res_paper;
CREATE TABLE res_paper (
    paper_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '论文ID',
    teacher_id BIGINT NOT NULL COMMENT '申报教师ID',
    
    -- 基本信息
    title VARCHAR(500) NOT NULL COMMENT '论文标题',
    journal_name VARCHAR(200) NOT NULL COMMENT '期刊名称',
    author_info VARCHAR(500) NOT NULL COMMENT '作者信息',
    corresponding_author VARCHAR(100) COMMENT '通讯作者',
    
    -- 论文分类
    paper_type VARCHAR(20) NOT NULL COMMENT '论文类型',
    conference_type VARCHAR(50) COMMENT '会议类型',
    
    -- 分区与级别
    sci_zone VARCHAR(10) COMMENT 'SCI分区',
    ccf_zone VARCHAR(10) COMMENT 'CCF分区',
    is_recommended_journal TINYINT(1) DEFAULT 0 COMMENT '是否推荐期刊',
    journal_type VARCHAR(50) COMMENT '期刊类型',
    is_excellent_journal TINYINT(1) DEFAULT 0 COMMENT '是否卓越期刊',
    
    -- 影响力指标
    is_highly_cited TINYINT(1) DEFAULT 0 COMMENT '是否高被引论文',
    is_esi_paper TINYINT(1) DEFAULT 0 COMMENT '是否是ESI论文',
    is_qdu_first_unit TINYINT(1) DEFAULT 0 COMMENT '是否青大第一单位',
    
    -- 奖励与分值
    reward_level VARCHAR(50) COMMENT '奖励级别',
    attribution VARCHAR(100) COMMENT '归属',
    points DECIMAL(10,2) COMMENT '分值',
    
    -- 时间与备注
    publish_date DATE NOT NULL COMMENT '发表日期',
    remark VARCHAR(500) COMMENT '备注',
    
    -- 附件
    pdf_url VARCHAR(500) COMMENT '论文PDF路径',
    proof_url VARCHAR(500) COMMENT '收录证明路径',
    
    -- 审核状态
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

-- 2. 项目表
DROP TABLE IF EXISTS res_project;
CREATE TABLE res_project (
    project_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '项目ID',
    teacher_id BIGINT NOT NULL COMMENT '申报教师ID',
    
    -- 基本信息
    project_no VARCHAR(100) NOT NULL UNIQUE COMMENT '项目编号',
    project_name VARCHAR(200) NOT NULL COMMENT '项目名称',
    
    -- 项目来源
    source VARCHAR(100) NOT NULL COMMENT '项目来源',
    sign_year VARCHAR(10) NOT NULL COMMENT '签订年份',
    sign_date DATE NOT NULL COMMENT '签订日期',
    
    -- 时间与金额
    end_date DATE NOT NULL COMMENT '结束日期',
    contract_amount DECIMAL(12,2) NOT NULL COMMENT '合同金额(万元)',
    received_amount DECIMAL(12,2) DEFAULT 0 COMMENT '到账金额(万元)',
    
    -- 附件
    approval_url VARCHAR(500) COMMENT '立项通知书路径',
    contract_url VARCHAR(500) COMMENT '合同文件路径',
    completion_url VARCHAR(500) COMMENT '结题证明路径',
    
    -- 审核状态
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

-- 3. 科研奖励表
DROP TABLE IF EXISTS res_science_award;
CREATE TABLE res_science_award (
    award_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '奖励ID',
    teacher_id BIGINT NOT NULL COMMENT '申报教师ID',
    
    -- 基本信息
    award_name VARCHAR(200) NOT NULL COMMENT '奖励名称',
    achievement_name VARCHAR(200) COMMENT '成果名称',
    
    -- 奖励级别
    award_level VARCHAR(50) NOT NULL COMMENT '奖励级别',
    award_date DATE NOT NULL COMMENT '获奖日期',
    
    -- 完成人信息
    all_completers VARCHAR(500) NOT NULL COMMENT '全部完成人',
    award_rank VARCHAR(50) NOT NULL COMMENT '获奖排名',
    unit_ranking VARCHAR(20) COMMENT '单位排名',
    
    -- 颁发与证书
    issuing_authority VARCHAR(200) COMMENT '颁发机构',
    certificate_no VARCHAR(100) COMMENT '证书编号',
    
    -- 主要成果
    main_achievement TEXT COMMENT '主要成果描述',
    
    -- 附件
    certificate_url VARCHAR(500) COMMENT '获奖证书路径',
    publicity_url VARCHAR(500) COMMENT '公示文件路径',
    
    -- 审核状态
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
