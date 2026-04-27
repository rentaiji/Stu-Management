-- 通用竞赛获奖表
CREATE TABLE IF NOT EXISTS edu_competition_award (
    award_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '获奖ID',
    user_id BIGINT NOT NULL COMMENT '用户ID(关联sys_user)',
    
    -- 竞赛信息
    competition_name VARCHAR(200) NOT NULL COMMENT '竞赛名称',
    host_institution VARCHAR(200) COMMENT '主办单位',
    award_level VARCHAR(50) NOT NULL COMMENT '获奖级别:国家级/省部级/校级/院级',
    award_rank VARCHAR(50) NOT NULL COMMENT '获奖等级:一等奖/二等奖/三等奖/金奖/银奖/铜奖/优秀奖',
    award_grade VARCHAR(50) COMMENT '获奖级别细分:A类/B类/C类',
    project_name VARCHAR(200) COMMENT '项目名称',
    
    -- 团队信息
    is_team_award TINYINT(1) DEFAULT 0 COMMENT '是否团队奖:0-个人奖,1-团队奖',
    team_rank VARCHAR(20) COMMENT '团队排名:第一作者/第二作者/成员',
    team_members VARCHAR(500) COMMENT '团队成员,逗号分隔',
    team_size INT COMMENT '团队人数',
    
    -- 获奖信息
    award_time VARCHAR(6) NOT NULL COMMENT '获奖时间,格式:202504',
    certificate_no VARCHAR(100) COMMENT '证书编号',
    track_group VARCHAR(100) COMMENT '赛道/组别',
    
    -- 指导教师信息
    instructor1_name VARCHAR(50) COMMENT '指导教师1姓名',
    instructor1_dept VARCHAR(100) COMMENT '指导教师1部门',
    instructor2_name VARCHAR(50) COMMENT '指导教师2姓名',
    instructor2_dept VARCHAR(100) COMMENT '指导教师2部门',
    
    -- 补充信息
    official_url VARCHAR(500) COMMENT '官方链接',
    data_source VARCHAR(20) DEFAULT '学生自填' COMMENT '数据来源',
    remark VARCHAR(500) COMMENT '备注',
    attachment_url VARCHAR(500) COMMENT '附件路径',
    
    -- 审核状态
    status VARCHAR(20) DEFAULT '待审核' COMMENT '状态:待审核/已通过/已驳回',
    audit_remark VARCHAR(500) COMMENT '审核备注',
    
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通用竞赛获奖表';

-- 蓝桥杯获奖表
CREATE TABLE IF NOT EXISTS edu_lanqiao_award (
    award_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '获奖ID',
    user_id BIGINT NOT NULL COMMENT '用户ID(关联sys_user)',
    
    -- 蓝桥杯竞赛信息
    competition_session VARCHAR(20) NOT NULL COMMENT '竞赛届次',
    competition_category VARCHAR(50) NOT NULL COMMENT '竞赛类别:软件类/电子类/设计类',
    competition_level VARCHAR(50) NOT NULL COMMENT '竞赛级别:国家级/省部级',
    subject VARCHAR(100) NOT NULL COMMENT '科目:Java/Python/C++/Web/嵌入式/单片机',
    group_type VARCHAR(100) NOT NULL COMMENT '组别:研究生组/大学A组/大学B组/大学C组',
    instructor_name VARCHAR(50) COMMENT '指导教师姓名',
    award VARCHAR(50) NOT NULL COMMENT '获奖等级:一等奖/二等奖/三等奖/优秀奖',
    
    -- 补充信息
    certificate_no VARCHAR(100) COMMENT '证书编号',
    award_time VARCHAR(6) COMMENT '获奖时间',
    attachment_url VARCHAR(500) COMMENT '附件路径',
    
    -- 审核状态
    status VARCHAR(20) DEFAULT '待审核' COMMENT '状态:待审核/已通过/已驳回',
    audit_remark VARCHAR(500) COMMENT '审核备注',
    
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    FOREIGN KEY (user_id) REFERENCES sys_user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='蓝桥杯获奖表';
