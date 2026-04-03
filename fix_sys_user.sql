-- 修复 sys_user 表缺少 remark 字段的问题
USE stu_manage;

-- 检查并添加 remark 字段
ALTER TABLE sys_user ADD COLUMN IF NOT EXISTS remark VARCHAR(500) DEFAULT NULL COMMENT '备注' AFTER update_time;

-- 验证修改
DESC sys_user;
