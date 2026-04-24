package org.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;

@Data
@TableName("edu_research_award")
public class EduResearchAward {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long applicationId;
    
    private String awardName;
    
    private String achievementName;
    
    private String awardLevel;
    
    private LocalDate awardDate;
    
    private String allWinners;
    
    private String issuingAuthority;
    
    private String awardGrade;
    
    private Integer unitRanking;
    
    private String certificateNo;
    
    private String mainAchievement;
    
    private String remark;
}
