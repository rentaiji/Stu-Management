package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.common.Result;
import org.example.entity.EduResearchApplication;

import java.util.Map;

public interface EduResearchApplicationService extends IService<EduResearchApplication> {
    
    Result<Boolean> submitApplication(Long teacherId, Integer researchType, Map<String, Object> detailData);
    
    Result<?> getMyApplications(Long teacherId);
    
    Result<?> getPendingAudit();
    
    Result<Boolean> auditApplication(Long applicationId, String status, Long auditBy, String auditRemark);
    
    Result<?> getApplicationDetail(Long applicationId);
}
