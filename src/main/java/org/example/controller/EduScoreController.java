package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.common.Result;
import org.example.entity.EduScore;
import org.example.entity.SysUser;
import org.example.service.EduScoreService;
import org.example.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/edu/score")
public class EduScoreController {

    @Autowired
    private EduScoreService scoreService;
    
    @Autowired
    private SysUserService userService;

    /**
     * 学生查询我的成绩
     */
    @GetMapping("/my-scores")
    public Result<List<Map<String, Object>>> getMyScores(@RequestParam Long studentId) {
        LambdaQueryWrapper<EduScore> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduScore::getStudentId, studentId);
        List<EduScore> scores = scoreService.list(wrapper);
        
        List<Map<String, Object>> result = scores.stream().map(score -> {
            Map<String, Object> map = new HashMap<>();
            map.put("scoreId", score.getScoreId());
            map.put("courseId", score.getCourseId());
            map.put("semesterId", score.getSemesterId());
            map.put("regularScore", score.getRegularScore());
            map.put("finalScore", score.getFinalScore());
            map.put("totalScore", score.getTotalScore());
            map.put("gradePoint", score.getGradePoint());
            map.put("gradeLevel", score.getGradeLevel());
            map.put("status", score.getStatus());
            return map;
        }).collect(java.util.stream.Collectors.toList());
        
        return Result.success(result);
    }

    /**
     * 教师录入成绩
     */
    @PostMapping("/input")
    public Result<Boolean> inputScore(@RequestBody Map<String, Object> params) {
        EduScore score = new EduScore();
        score.setStudentId(Long.valueOf(params.get("studentId").toString()));
        score.setCourseId(Long.valueOf(params.get("courseId").toString()));
        score.setSemesterId(Long.valueOf(params.get("semesterId").toString()));
        
        if (params.containsKey("regularScore")) {
            score.setRegularScore(new java.math.BigDecimal(params.get("regularScore").toString()));
        }
        if (params.containsKey("finalScore")) {
            score.setFinalScore(new java.math.BigDecimal(params.get("finalScore").toString()));
        }
        if (params.containsKey("totalScore")) {
            score.setTotalScore(new java.math.BigDecimal(params.get("totalScore").toString()));
        }
        
        score.setInputBy(Long.valueOf(params.get("inputBy").toString()));
        score.setInputTime(java.time.LocalDateTime.now());
        score.setStatus("0");
        
        // 检查是否已存在
        LambdaQueryWrapper<EduScore> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduScore::getStudentId, score.getStudentId())
               .eq(EduScore::getCourseId, score.getCourseId())
               .eq(EduScore::getSemesterId, score.getSemesterId());
        
        EduScore existScore = scoreService.getOne(wrapper);
        boolean success;
        if (existScore != null) {
            score.setScoreId(existScore.getScoreId());
            success = scoreService.updateById(score);
        } else {
            success = scoreService.save(score);
        }
        
        return success ? Result.success(true) : Result.error("录入失败");
    }
}
