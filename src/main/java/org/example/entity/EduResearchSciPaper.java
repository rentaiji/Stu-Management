package org.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("edu_research_sci_paper")
public class EduResearchSciPaper {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long applicationId;
    
    private String paperTitle;
    
    private String journalName;
    
    private String authorInfo;
    
    private String correspondingAuthor;
    
    private String sciZone;
    
    private String isRecommendedJournal; // 0-否, 1-是
    
    private String ccfZone;
    
    private String isExcellentJournal; // 0-否, 1-是
    
    private String isQingdaoFirstUnit; // 0-否, 1-是
    
    private String isHighlyCited; // 0-否, 1-是
    
    private String isEsiPaper; // 0-否, 1-是
    
    private String awardLevel;
    
    private String paperAffiliation;
    
    private Double points;
    
    private String remark;
    
    private String attachmentUrl; // 附件PDF路径
}
