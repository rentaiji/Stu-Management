-- 为科研申报相关表添加附件字段

-- 1. SCI论文表添加附件字段
ALTER TABLE edu_research_sci_paper 
ADD COLUMN attachment_url VARCHAR(500) COMMENT '附件PDF路径' AFTER remark;

-- 2. 卓越期刊论文表添加附件字段
ALTER TABLE edu_research_excellent_paper 
ADD COLUMN attachment_url VARCHAR(500) COMMENT '附件PDF路径' AFTER remark;

-- 3. CCF会议论文表添加附件字段
ALTER TABLE edu_research_ccf_paper 
ADD COLUMN attachment_url VARCHAR(500) COMMENT '附件PDF路径' AFTER remark;

-- 4. 科研奖励表添加附件字段
ALTER TABLE edu_research_award 
ADD COLUMN attachment_url VARCHAR(500) COMMENT '附件PDF路径' AFTER remark;

-- 5. 科研项目表添加附件字段
ALTER TABLE edu_research_project 
ADD COLUMN attachment_url VARCHAR(500) COMMENT '附件PDF路径' AFTER remark;
