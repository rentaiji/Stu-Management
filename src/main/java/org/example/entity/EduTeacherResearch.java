package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("edu_teacher_research")
public class EduTeacherResearch implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "research_id", type = IdType.AUTO)
    private Long researchId;

    @TableField("teacher_id")
    private Long teacherId;

    @TableField("research_type")
    private String researchType; // 0:论文，1:项目，2:竞赛，3:奖励

    @TableField("title")
    private String title;

    @TableField("level")
    private String level; // 国家级/省级/校级

    @TableField("award_date")
    private LocalDate awardDate;

    @TableField("rank")
    private Integer rank;

    @TableField("remark")
    private String remark;

    @TableField("attachment_url")
    private String attachmentUrl;

    @TableField("status")
    private String status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
