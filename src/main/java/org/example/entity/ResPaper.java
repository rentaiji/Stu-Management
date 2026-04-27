package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("res_paper")
public class ResPaper implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "paper_id", type = IdType.AUTO)
    private Long paperId;

    @TableField("teacher_id")
    private Long teacherId;

    // 基本信息
    @TableField("title")
    private String title;

    @TableField("journal_name")
    private String journalName;

    @TableField("author_info")
    private String authorInfo;

    @TableField("corresponding_author")
    private String correspondingAuthor;

    // 论文分类
    @TableField("paper_type")
    private String paperType;

    @TableField("conference_type")
    private String conferenceType;

    // 分区与级别
    @TableField("sci_zone")
    private String sciZone;

    @TableField("ccf_zone")
    private String ccfZone;

    @TableField("is_recommended_journal")
    private Integer isRecommendedJournal;

    @TableField("journal_type")
    private String journalType;

    @TableField("is_excellent_journal")
    private Integer isExcellentJournal;

    // 影响力指标
    @TableField("is_highly_cited")
    private Integer isHighlyCited;

    @TableField("is_esi_paper")
    private Integer isEsiPaper;

    @TableField("is_qdu_first_unit")
    private Integer isQduFirstUnit;

    // 奖励与分值
    @TableField("reward_level")
    private String rewardLevel;

    @TableField("attribution")
    private String attribution;

    @TableField("points")
    private BigDecimal points;

    // 时间与备注
    @TableField("publish_date")
    private LocalDate publishDate;

    @TableField("remark")
    private String remark;

    // 附件
    @TableField("pdf_url")
    private String pdfUrl;

    @TableField("proof_url")
    private String proofUrl;

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
