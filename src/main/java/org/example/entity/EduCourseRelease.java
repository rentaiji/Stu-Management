package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("edu_course_release")
public class EduCourseRelease implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("course_id")
    private Long courseId;

    @TableField("semester_id")
    private Long semesterId;

    @TableField("teacher_id")
    private Long teacherId;

    @TableField("capacity")
    private Integer capacity;

    @TableField("selected_count")
    private Integer selectedCount;

    @TableField("release_status")
    private String releaseStatus;

    @TableField("release_time")
    private LocalDateTime releaseTime;

    @TableField("release_by")
    private Long releaseBy;

    @TableField("start_week")
    private Integer startWeek;

    @TableField("end_week")
    private Integer endWeek;

    @TableField("class_time")
    private String classTime;

    @TableField("class_location")
    private String classLocation;

    @TableField("remark")
    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    @TableField("deleted")
    private Integer deleted;
}
