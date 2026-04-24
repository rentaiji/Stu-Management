-- 课程发布表：教务老师将课程发布到指定学期，学生才能选课
DROP TABLE IF EXISTS edu_course_release;
CREATE TABLE edu_course_release (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键 ID',
    course_id BIGINT NOT NULL COMMENT '课程 ID',
    semester_id BIGINT NOT NULL COMMENT '学期 ID',
    teacher_id BIGINT DEFAULT NULL COMMENT '授课教师 ID',
    capacity INT DEFAULT 0 COMMENT '本学期容量',
    selected_count INT DEFAULT 0 COMMENT '已选人数',
    release_status CHAR(1) DEFAULT '0' COMMENT '发布状态 0-未发布 1-已发布 2-已关闭',
    release_time DATETIME DEFAULT NULL COMMENT '发布时间',
    release_by BIGINT DEFAULT NULL COMMENT '发布人 ID',
    start_week INT DEFAULT 1 COMMENT '开始周次',
    end_week INT DEFAULT 18 COMMENT '结束周次',
    class_time VARCHAR(100) DEFAULT NULL COMMENT '上课时间',
    class_location VARCHAR(100) DEFAULT NULL COMMENT '上课地点',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志',
    UNIQUE KEY uk_course_semester (course_id, semester_id, deleted),
    INDEX idx_semester (semester_id),
    INDEX idx_teacher (teacher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程发布表';
