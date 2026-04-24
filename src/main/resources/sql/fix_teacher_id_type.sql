-- 修复 edu_course_release 表字段类型
ALTER TABLE edu_course_release MODIFY COLUMN teacher_id BIGINT DEFAULT NULL COMMENT '授课教师 ID';
