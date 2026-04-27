package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("res_project")
public class ResProject implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "project_id", type = IdType.AUTO)
    private Long projectId;

    @TableField("teacher_id")
    private Long teacherId;

    // 基本信息
    @TableField("project_no")
    private String projectNo;

    @TableField("project_name")
    private String projectName;

    // 项目来源
    @TableField("source")
    private String source;

    @TableField("sign_year")
    private String signYear;

    @TableField("sign_date")
    private LocalDate signDate;

    // 时间与金额
    @TableField("end_date")
    private LocalDate endDate;

    @TableField("contract_amount")
    private BigDecimal contractAmount;

    @TableField("received_amount")
    private BigDecimal receivedAmount;

    // 附件
    @TableField("approval_url")
    private String approvalUrl;

    @TableField("contract_url")
    private String contractUrl;

    @TableField("completion_url")
    private String completionUrl;

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
