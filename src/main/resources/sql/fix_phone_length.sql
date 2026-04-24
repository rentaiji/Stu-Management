USE stu_manage;

-- 修改 sys_user 表 phone 字段长度
ALTER TABLE sys_user MODIFY COLUMN phone VARCHAR(20) DEFAULT NULL COMMENT '手机号';

-- 修改 edu_student_ext 表 emergency_phone 字段长度
ALTER TABLE edu_student_ext MODIFY COLUMN emergency_phone VARCHAR(20) DEFAULT NULL COMMENT '紧急联系电话';
