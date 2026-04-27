package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("edu_competition_award")
public class EduCompetitionAward implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "award_id", type = IdType.AUTO)
    private Long awardId;

    @TableField("user_id")
    private Long userId;

    // 竞赛信息
    @TableField("competition_name")
    private String competitionName;
    @TableField("host_institution")
    private String hostInstitution;
    @TableField("award_level")
    private String awardLevel;
    @TableField("award_rank")
    private String awardRank;
    @TableField("award_grade")
    private String awardGrade;
    @TableField("project_name")
    private String projectName;

    // 团队信息
    @TableField("is_team_award")
    private Integer isTeamAward;
    @TableField("team_rank")
    private String teamRank;
    @TableField("team_members")
    private String teamMembers;
    @TableField("team_size")
    private Integer teamSize;

    // 获奖信息
    @TableField("award_time")
    private String awardTime;
    @TableField("certificate_no")
    private String certificateNo;
    @TableField("track_group")
    private String trackGroup;

    // 指导教师信息
    @TableField("instructor1_name")
    private String instructor1Name;
    @TableField("instructor1_dept")
    private String instructor1Dept;
    @TableField("instructor2_name")
    private String instructor2Name;
    @TableField("instructor2_dept")
    private String instructor2Dept;

    // 补充信息
    @TableField("official_url")
    private String officialUrl;
    @TableField("data_source")
    private String dataSource;
    @TableField("remark")
    private String remark;
    @TableField("attachment_url")
    private String attachmentUrl;

    // 审核状态
    @TableField("status")
    private String status;
    @TableField("audit_remark")
    private String auditRemark;
    @TableField("audit_time")
    private LocalDateTime auditTime;

    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
