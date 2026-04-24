package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("edu_research_application")
public class EduResearchApplication {
    @TableId(type = IdType.AUTO)
    private Long applicationId;
    
    private Long teacherId;
    
    private Integer researchType; // 1-SCI论文, 2-卓越期刊论文, 3-CCF会议论文, 4-科研奖励, 5-科研项目
    
    private String title;
    
    private String status; // 0-待审核, 1-已通过, 2-已拒绝
    
    private Long auditBy;
    
    private LocalDateTime auditTime;
    
    private String auditRemark;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
