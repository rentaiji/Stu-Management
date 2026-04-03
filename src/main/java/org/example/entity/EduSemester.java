package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("edu_semester")
public class EduSemester implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "semester_id", type = IdType.AUTO)
    private Long semesterId;

    @TableField("semester_name")
    private String semesterName;

    @TableField("semester_code")
    private String semesterCode;

    @TableField("start_date")
    private LocalDate startDate;

    @TableField("end_date")
    private LocalDate endDate;

    @TableField("is_current")
    private String isCurrent;

    @TableField("status")
    private String status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
