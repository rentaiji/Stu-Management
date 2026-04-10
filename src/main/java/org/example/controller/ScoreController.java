package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.common.Result;
import org.example.entity.EduScore;
import org.example.service.EduScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/edu/score")
public class ScoreController {

    @Autowired
    private EduScoreService scoreService;

    @GetMapping("/student/{studentId}")
    public Result<List<EduScore>> getByStudentId(@PathVariable Long studentId) {
        LambdaQueryWrapper<EduScore> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduScore::getStudentId, studentId);
        List<EduScore> list = scoreService.list(wrapper);
        return Result.success(list);
    }

    @GetMapping("/course/{courseId}")
    public Result<List<Map<String, Object>>> getByCourseId(@PathVariable Long courseId) {
        List<Map<String, Object>> list = scoreService.getScoresByCourse(courseId);
        return Result.success(list);
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody EduScore score) {
        boolean success = scoreService.save(score);
        return success ? Result.success(true) : Result.error("录入失败");
    }

    @PostMapping("/batch")
    public Result<Boolean> batchSave(@RequestBody List<EduScore> scores) {
        boolean success = scoreService.saveBatch(scores);
        return success ? Result.success(true) : Result.error("批量录入失败");
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody EduScore score) {
        boolean success = scoreService.updateById(score);
        return success ? Result.success(true) : Result.error("更新失败");
    }

    @PostMapping("/submit/{courseId}")
    public Result<Boolean> submitForReview(@PathVariable Long courseId) {
        boolean success = scoreService.submitForReview(courseId);
        return success ? Result.success(true) : Result.error("提交失败");
    }
}
