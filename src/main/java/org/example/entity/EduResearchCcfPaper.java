package org.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("edu_research_ccf_paper")
public class EduResearchCcfPaper {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long applicationId;
    
    private String paperTitle;
    
    private String conferenceName;
    
    private String firstAuthor;
    
    private String correspondingAuthor;
    
    private String conferenceType;
    
    private String isQingdaoFirstUnit; // 0-否, 1-是
    
    private String paperAffiliation;
    
    private Double points;
    
    private String remark;
}
