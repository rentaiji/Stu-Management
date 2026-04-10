package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.EduScore;

import java.util.List;
import java.util.Map;

public interface EduScoreService extends IService<EduScore> {
    List<Map<String, Object>> getScoresByCourse(Long courseId);
    boolean submitForReview(Long courseId);
}
