package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.common.Result;
import org.example.entity.EduScore;
import org.example.service.EduScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    public Result<Boolean> save(@RequestBody EduScore score) {
        boolean success = scoreService.save(score);
        return success ? Result.success(true) : Result.error("录入失败");
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody EduScore score) {
        boolean success = scoreService.updateById(score);
        return success ? Result.success(true) : Result.error("更新失败");
    }
}
