package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.common.Result;
import org.example.entity.EduGrade;
import org.example.service.EduGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/edu/grade")
public class GradeController {

    @Autowired
    private EduGradeService gradeService;

    @GetMapping("/list")
    public Result<List<EduGrade>> list() {
        return Result.success(gradeService.list());
    }

    @GetMapping("/{gradeId}")
    public Result<EduGrade> getById(@PathVariable Long gradeId) {
        return Result.success(gradeService.getById(gradeId));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody EduGrade grade) {
        return Result.success(gradeService.saveOrUpdate(grade));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody EduGrade grade) {
        return Result.success(gradeService.updateById(grade));
    }

    @DeleteMapping("/{gradeId}")
    public Result<Boolean> delete(@PathVariable Long gradeId) {
        return Result.success(gradeService.removeById(gradeId));
    }
}
