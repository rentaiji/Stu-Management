DROP TABLE IF EXISTS edu_course_release;
CREATE TABLE edu_course_release (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id BIGINT NOT NULL,
    semester_id BIGINT NOT NULL,
    teacher_id BIGINT DEFAULT NULL,
    capacity INT DEFAULT 0,
    selected_count INT DEFAULT 0,
    release_status CHAR(1) DEFAULT '0',
    release_time DATETIME DEFAULT NULL,
    release_by BIGINT DEFAULT NULL,
    start_week INT DEFAULT 1,
    end_week INT DEFAULT 18,
    class_time VARCHAR(100) DEFAULT NULL,
    class_location VARCHAR(100) DEFAULT NULL,
    remark VARCHAR(500) DEFAULT NULL,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0,
    UNIQUE KEY uk_course_semester (course_id, semester_id, deleted),
    INDEX idx_semester (semester_id),
    INDEX idx_teacher (teacher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
