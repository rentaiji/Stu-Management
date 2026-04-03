-- 修改 sys_user 表主键为 user_name（学号）
-- 1. 删除旧的自增主键 user_id
ALTER TABLE stu_manage.sys_user DROP PRIMARY KEY;

-- 2. 删除 user_id 列
ALTER TABLE stu_manage.sys_user DROP COLUMN user_id;

-- 3. 将 user_name 设置为主键
ALTER TABLE stu_manage.sys_user ADD PRIMARY KEY (user_name);

-- 4. 设置 user_name 不允许为空
ALTER TABLE stu_manage.sys_user MODIFY COLUMN user_name VARCHAR(50) NOT NULL COMMENT '学号/工号';
