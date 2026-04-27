package org.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("edu_research_excellent_paper")
public class EduResearchExcellentPaper {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long applicationId;
    
    private String paperTitle;
    
    private String journalName;
    
    private String authorInfo;
    
    private String correspondingAuthor;
    
    private String journalType;
    
    private String isQingdaoFirstUnit; // 0-否, 1-是
    
    private String paperAffiliation;
    
    private Double points;
    
    private String remark;
    
    private String attachmentUrl; // 附件PDF路径
}
