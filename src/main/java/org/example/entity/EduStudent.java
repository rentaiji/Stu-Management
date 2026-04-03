package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("edu_student")
public class EduStudent implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "student_id", type = IdType.AUTO)
    private Long studentId;

    @TableField("user_id")
    private Long userId;

    @TableField("student_no")
    private String studentNo;

    @TableField("birth_date")
    private LocalDate birthDate;

    @TableField("birth_place")
    private String birthPlace;

    @TableField("grade_id")
    private Long gradeId;

    @TableField("class_id")
    private Long classId;

    @TableField("major_id")
    private Long majorId;

    @TableField("enrollment_year")
    private Integer enrollmentYear;

    @TableField("study_mode")
    private Integer studyMode;

    @TableField("student_status")
    private Integer studentStatus;

    @TableField("id_card")
    private String idCard;

    @TableField("native_place")
    private String nativePlace;

    @TableField("nation")
    private String nation;

    @TableField("political_status")
    private Integer politicalStatus;

    @TableField("entry_date")
    private LocalDate entryDate;

    @TableField("graduate_date")
    private LocalDate graduateDate;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
