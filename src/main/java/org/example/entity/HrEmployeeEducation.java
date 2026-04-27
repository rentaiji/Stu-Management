package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("hr_employee_education")
public class HrEmployeeEducation implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "education_id", type = IdType.AUTO)
    private Long educationId;

    @TableField("archive_id")
    private Long archiveId;

    @TableField("degree_type")
    private String degreeType;
    @TableField("school_name")
    private String schoolName;
    @TableField("major")
    private String major;
    @TableField("start_date")
    private LocalDate startDate;
    @TableField("end_date")
    private LocalDate endDate;
    @TableField("degree_cert_no")
    private String degreeCertNo;
    @TableField("degree_cert_url")
    private String degreeCertUrl;
    @TableField("diploma_cert_no")
    private String diplomaCertNo;
    @TableField("diploma_cert_url")
    private String diplomaCertUrl;

    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
