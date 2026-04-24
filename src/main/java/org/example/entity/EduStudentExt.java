package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("edu_student_ext")
public class EduStudentExt implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId("student_id")
    private Long studentId;

    @TableField("grade_id")
    private Long gradeId;

    @TableField("class_id")
    private Long classId;

    @TableField("major_id")
    private Long majorId;

    @TableField("dept_id")
    private Long deptId;

    @TableField("education_level")
    private String educationLevel;

    @TableField("study_length")
    private Integer studyLength;

    @TableField("entrance_year")
    private Integer entranceYear;

    @TableField("student_status")
    private String studentStatus;

    @TableField("emergency_contact")
    private String emergencyContact;

    @TableField("emergency_phone")
    private String emergencyPhone;
}
