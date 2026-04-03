package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("edu_teacher")
public class EduTeacher implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "teacher_id", type = IdType.AUTO)
    private Long teacherId;

    @TableField("user_id")
    private Long userId;

    @TableField("teacher_no")
    private String teacherNo;

    @TableField("dept_id")
    private Long deptId;

    @TableField("title")
    private Integer title;

    @TableField("degree")
    private Integer degree;

    @TableField("research_direction")
    private String researchDirection;

    @TableField("first_hire_date")
    private LocalDate firstHireDate;

    @TableField("regular_date")
    private LocalDate regularDate;

    @TableField("hire_level")
    private String hireLevel;

    @TableField("special_level")
    private String specialLevel;

    @TableField("special_contract_date")
    private LocalDate specialContractDate;

    @TableField("special_contract_file")
    private String specialContractFile;

    @TableField("education_certificates")
    private String educationCertificates;

    @TableField("entry_date")
    private LocalDate entryDate;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
