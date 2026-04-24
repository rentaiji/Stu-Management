package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.common.Result;
import org.example.entity.EduScore;

import java.util.List;
import java.util.Map;

public interface EduScoreService extends IService<EduScore> {
    List<Map<String, Object>> getScoresByCourse(Long courseId);
    boolean submitForReview(Long courseId);
    Result<Boolean> auditScore(Long scoreId, String action, Long auditorId, String remark);
    Result<List<Map<String, Object>>> getPendingAuditScores();
}
