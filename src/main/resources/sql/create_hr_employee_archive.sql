-- 教职工人事档案主表
CREATE TABLE IF NOT EXISTS hr_employee_archive (
    archive_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '档案ID',
    user_id BIGINT NOT NULL COMMENT '用户ID(关联sys_user)',
    
    -- 基本信息
    name_pinyin VARCHAR(100) COMMENT '姓名拼音',
    nation VARCHAR(50) COMMENT '民族',
    age INT COMMENT '年龄(自动计算)',
    birth_date_archive DATE COMMENT '档案出生日期',
    native_place VARCHAR(100) COMMENT '籍贯',
    nationality_region VARCHAR(50) COMMENT '国籍(地区)',
    marital_status VARCHAR(10) COMMENT '婚否',
    political_status VARCHAR(50) COMMENT '党派/政治面貌',
    join_party_date DATE COMMENT '入党/参加党派日期',
    
    -- 工作信息
    join_work_date DATE COMMENT '参加工作年月',
    join_teaching_date DATE COMMENT '从教年月',
    join_school_date DATE COMMENT '来校/入校时间',
    expected_retire_date DATE COMMENT '拟退休/预计离退休日期',
    department VARCHAR(100) COMMENT '部门/所在部门',
    current_status VARCHAR(20) DEFAULT '在职' COMMENT '当前状态',
    personal_status VARCHAR(50) COMMENT '个人身份',
    
    -- 学历信息
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
    
    -- 职称岗位
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
    
    -- 分类标签
    employee_category VARCHAR(50) COMMENT '分类/教职工类别',
    employee_source VARCHAR(50) COMMENT '教职工来源',
    job_type VARCHAR(50) COMMENT '教师类型',
    
    -- 兼职与双肩挑
    concurrent_post VARCHAR(100) COMMENT '兼聘岗位',
    concurrent_post_level VARCHAR(50) COMMENT '兼聘岗位等级',
    concurrent_post_date DATE COMMENT '兼聘日期',
    is_double_shoulder TINYINT(1) DEFAULT 0 COMMENT '是否双肩挑',
    double_shoulder_dept VARCHAR(100) COMMENT '双肩挑所在单位',
    is_industry_talent TINYINT(1) DEFAULT 0 COMMENT '是否产业人员',
    
    -- 党政职务
    party_position VARCHAR(100) COMMENT '党政职务',
    party_position_level VARCHAR(50) COMMENT '党政职务级别',
    party_position_date DATE COMMENT '任职时间',
    
    -- 教师资格与证书
    teacher_cert_no VARCHAR(50) COMMENT '教师资格证号',
    teacher_cert_date DATE COMMENT '教师资格获得日期',
    is_counselor VARCHAR(10) COMMENT '辅导员类别',
    is_psychology_teacher TINYINT(1) DEFAULT 0 COMMENT '是否专职从事心理咨询工作',
    has_psychology_cert TINYINT(1) DEFAULT 0 COMMENT '是否持有心理咨询资格证书',
    is_dual_qualified TINYINT(1) DEFAULT 0 COMMENT '是否双师型教师',
    is_undergrad_teacher TINYINT(1) DEFAULT 0 COMMENT '是否为本科生上课',
    
    -- 人才与特聘
    talent_title VARCHAR(100) COMMENT '人才称号',
    talent_appointment_date DATE COMMENT '人才评聘时间',
    first_employment_date DATE COMMENT '首聘期日期',
    regularization_date DATE COMMENT '转正日期',
    introduction_level VARCHAR(50) COMMENT '引进层次',
    special_appointment_level VARCHAR(50) COMMENT '特聘层次',
    special_contract_date DATE COMMENT '特聘合同日期',
    special_contract_file VARCHAR(500) COMMENT '特聘合同上传路径',
    
    -- 联系方式
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    address VARCHAR(200) COMMENT '住址',
    emergency_contact VARCHAR(50) COMMENT '紧急联系人',
    emergency_contact_phone VARCHAR(20) COMMENT '紧急联系人手机号',
    
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    
    UNIQUE KEY uk_user_id (user_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教职工人事档案主表';

-- 学历学位子表
CREATE TABLE IF NOT EXISTS hr_employee_education (
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

-- 子女信息子表
CREATE TABLE IF NOT EXISTS hr_employee_child (
    child_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    archive_id BIGINT NOT NULL COMMENT '档案ID',
    child_name VARCHAR(50) COMMENT '子女姓名',
    child_birthday DATE COMMENT '子女出生日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_archive_id (archive_id),
    FOREIGN KEY (archive_id) REFERENCES hr_employee_archive(archive_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='子女信息子表';

-- 创业/兼职子表
CREATE TABLE IF NOT EXISTS hr_employee_business (
    business_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    archive_id BIGINT NOT NULL COMMENT '档案ID',
    type VARCHAR(20) COMMENT '类型:创业/兼职',
    company_name VARCHAR(100) COMMENT '公司名称',
    legal_person VARCHAR(50) COMMENT '法人',
    establishment_date DATE COMMENT '成立日期',
    organization_name VARCHAR(100) COMMENT '组织名称',
    position VARCHAR(50) COMMENT '职位',
    research_field VARCHAR(200) COMMENT '研究领域',
    start_date DATE COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_archive_id (archive_id),
    FOREIGN KEY (archive_id) REFERENCES hr_employee_archive(archive_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='创业/兼职子表';

-- 荣誉奖励子表
CREATE TABLE IF NOT EXISTS hr_employee_honor (
    honor_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    archive_id BIGINT NOT NULL COMMENT '档案ID',
    award_year INT COMMENT '获奖年份',
    award_name VARCHAR(200) COMMENT '奖项名称',
    award_level VARCHAR(50) COMMENT '奖项级别',
    host_unit VARCHAR(100) COMMENT '主办单位',
    document_no VARCHAR(100) COMMENT '文号',
    document_date DATE COMMENT '发文日期',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_archive_id (archive_id),
    FOREIGN KEY (archive_id) REFERENCES hr_employee_archive(archive_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='荣誉奖励子表';

-- 年度考核子表
CREATE TABLE IF NOT EXISTS hr_employee_assessment (
    assessment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    archive_id BIGINT NOT NULL COMMENT '档案ID',
    year INT COMMENT '年份',
    result VARCHAR(20) COMMENT '考核结果',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    INDEX idx_archive_id (archive_id),
    FOREIGN KEY (archive_id) REFERENCES hr_employee_archive(archive_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='年度考核子表';
