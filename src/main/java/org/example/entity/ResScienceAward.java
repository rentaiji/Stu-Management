package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("res_science_award")
public class ResScienceAward implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "award_id", type = IdType.AUTO)
    private Long awardId;

    @TableField("teacher_id")
    private Long teacherId;

    // 基本信息
    @TableField("award_name")
    private String awardName;

    @TableField("achievement_name")
    private String achievementName;

    // 奖励级别
    @TableField("award_level")
    private String awardLevel;

    @TableField("award_date")
    private LocalDate awardDate;

    // 完成人信息
    @TableField("all_completers")
    private String allCompleters;

    @TableField("award_rank")
    private String awardRank;

    @TableField("unit_ranking")
    private String unitRanking;

    // 颁发与证书
    @TableField("issuing_authority")
    private String issuingAuthority;

    @TableField("certificate_no")
    private String certificateNo;

    // 主要成果
    @TableField("main_achievement")
    private String mainAchievement;

    // 附件
    @TableField("certificate_url")
    private String certificateUrl;

    @TableField("publicity_url")
    private String publicityUrl;

    // 审核状态
    @TableField("status")
    private String status;

    @TableField("audit_remark")
    private String auditRemark;

    @TableField("audit_time")
    private LocalDateTime auditTime;

    @TableField("auditor_id")
    private Long auditorId;

    @TableField("submit_time")
    private LocalDateTime submitTime;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
