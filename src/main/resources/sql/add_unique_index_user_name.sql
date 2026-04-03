-- 为 sys_user 表的 user_name 字段添加唯一索引
CREATE UNIQUE INDEX idx_user_name ON stu_manage.sys_user (user_name);
