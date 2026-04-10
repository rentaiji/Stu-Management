CREATE TABLE IF NOT EXISTS `edu_competition` (
  `competition_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '竞赛ID',
  `student_id` BIGINT NOT NULL COMMENT '学生ID',
  `competition_name` VARCHAR(200) NOT NULL COMMENT '竞赛名称',
  `competition_type` VARCHAR(10) NOT NULL DEFAULT '0' COMMENT '竞赛类型：0-学科竞赛 1-创新创业 2-体育竞技 3-文艺比赛',
  `level` VARCHAR(10) NOT NULL DEFAULT '0' COMMENT '级别：0-国家级 1-省级 2-校级',
  `award_level` VARCHAR(10) DEFAULT NULL COMMENT '获奖等级：0-一等奖 1-二等奖 2-三等奖 3-优秀奖',
  `award_date` DATE DEFAULT NULL COMMENT '获奖日期',
  `team_members` TEXT COMMENT '团队成员',
  `description` TEXT COMMENT '竞赛描述',
  `attachment_url` VARCHAR(500) DEFAULT NULL COMMENT '附件地址',
  `status` VARCHAR(10) NOT NULL DEFAULT '0' COMMENT '状态：0-待审核 1-已通过 2-已驳回',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` INT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  PRIMARY KEY (`competition_id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生竞赛申报表';
