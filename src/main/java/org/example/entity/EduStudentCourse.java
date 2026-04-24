package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("edu_student_course")
public class EduStudentCourse implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("student_id")
    private Long studentId;

    @TableField("course_id")
    private Long courseId;

    @TableField("semester_id")
    private Long semesterId;

    @TableField("select_time")
    private LocalDateTime selectTime;

    @TableField("select_status")
    private String selectStatus;

    @TableField("audit_status")
    private String auditStatus;

    @TableField("audit_by")
    private Long auditBy;

    @TableField("audit_time")
    private LocalDateTime auditTime;

    @TableField("remark")
    private String remark;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
