package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("edu_competition")
public class EduCompetition implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "competition_id", type = IdType.AUTO)
    private Long competitionId;

    @TableField("student_id")
    private Long studentId;

    @TableField("competition_name")
    private String competitionName;

    @TableField("competition_type")
    private String competitionType; // 0:学科竞赛 1:创新创业 2:体育竞技 3:文艺比赛

    @TableField("level")
    private String level; // 0:国家级 1:省级 2:校级

    @TableField("award_level")
    private String awardLevel; // 0:一等奖 1:二等奖 2:三等奖 3:优秀奖

    @TableField("award_date")
    private LocalDate awardDate;

    @TableField("team_members")
    private String teamMembers;

    @TableField("description")
    private String description;

    @TableField("attachment_url")
    private String attachmentUrl;

    @TableField("status")
    private String status; // 0:待审核 1:已通过 2:已驳回

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
