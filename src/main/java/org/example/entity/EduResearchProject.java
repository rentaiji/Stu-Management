package org.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;

@Data
@TableName("edu_research_project")
public class EduResearchProject {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long applicationId;
    
    private String projectNo;
    
    private String projectName;
    
    private String projectSource;
    
    private String leader;
    
    private Integer signYear;
    
    private LocalDate contractStartDate;
    
    private LocalDate contractEndDate;
    
    private Double contractAmount;
    
    private Double receivedAmount;
    
    private String remark;
    
    private String attachmentUrl; // 附件PDF路径
}
