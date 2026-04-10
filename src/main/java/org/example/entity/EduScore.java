package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("edu_score")
public class EduScore implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "score_id", type = IdType.AUTO)
    private Long scoreId;

    @TableField("student_id")
    private Long studentId;

    @TableField("course_id")
    private Long courseId;

    @TableField("semester_id")
    private Long semesterId;

    @TableField("regular_score")
    private BigDecimal regularScore;

    @TableField("final_score")
    private BigDecimal finalScore;

    @TableField("total_score")
    private BigDecimal totalScore;

    @TableField("grade_point")
    private BigDecimal gradePoint;

    @TableField("grade_level")
    private String gradeLevel;

    @TableField("credit_earned")
    private BigDecimal creditEarned;

    @TableField(value = "input_by", fill = FieldFill.INSERT)
    private Long inputBy;

    @TableField(value = "input_time", fill = FieldFill.INSERT)
    private LocalDateTime inputTime;

    @TableField("status")
    private String status;

    @TableField("remark")
    private String remark;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
