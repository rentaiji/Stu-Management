-- 创建登录日志表
CREATE TABLE IF NOT EXISTS sys_login_log (
    log_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    user_id BIGINT COMMENT '用户ID',
    username VARCHAR(50) COMMENT '用户名/账号',
    real_name VARCHAR(50) COMMENT '真实姓名',
    user_type VARCHAR(10) COMMENT '用户类型:0-学生,1-教师,2-管理员',
    login_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    user_agent VARCHAR(500) COMMENT '浏览器信息',
    login_status VARCHAR(20) DEFAULT '成功' COMMENT '登录状态:成功/失败',
    fail_reason VARCHAR(200) COMMENT '失败原因',
    operation VARCHAR(200) COMMENT '操作描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    INDEX idx_user_id (user_id),
    INDEX idx_login_time (login_time),
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';
