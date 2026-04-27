-- 为竞赛获奖表添加审批时间字段
ALTER TABLE edu_competition_award ADD COLUMN audit_time DATETIME COMMENT '审批时间';
