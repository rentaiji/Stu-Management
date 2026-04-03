package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("edu_teacher_family")
public class EduTeacherFamily implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "family_id", type = IdType.AUTO)
    private Long familyId;

    @TableField("teacher_id")
    private Long teacherId;

    @TableField("member_name")
    private String memberName;

    @TableField("relation")
    private String relation; // 0:配偶，1:子女，2:父母

    @TableField("birth_date")
    private LocalDate birthDate;

    @TableField("school_name")
    private String schoolName;

    @TableField("grade_class")
    private String gradeClass;

    @TableField("status")
    private String status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
