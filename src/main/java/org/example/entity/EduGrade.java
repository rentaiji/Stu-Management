package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("edu_grade")
public class EduGrade implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "grade_id", type = IdType.AUTO)
    private Long gradeId;

    @TableField("grade_name")
    private String gradeName;

    @TableField("grade_code")
    private String gradeCode;

    @TableField("entrance_year")
    private Integer entranceYear;

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
