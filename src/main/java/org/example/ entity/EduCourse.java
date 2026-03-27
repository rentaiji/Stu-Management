NEW_FILE_CODE
package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("edu_course")
public class EduCourse implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "course_id", type = IdType.AUTO)
    private Long courseId;

    @TableField("course_code")
    private String courseCode;

    @TableField("course_name")
    private String courseName;

    @TableField("credits")
    private BigDecimal credits;

    @TableField("hours")
    private Integer hours;

    @TableField("course_type")
    private String courseType;

    @TableField("teacher_id")
    private Long teacherId;

    @TableField("capacity")
    private Integer capacity;

    @TableField("selected_count")
    private Integer selectedCount;

    @TableField("description")
    private String description;

    @TableField("status")
    private String status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
