package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.common.Result;
import org.example.entity.EduTeacherResearch;
import org.example.service.EduTeacherResearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/edu/research")
public class EduTeacherResearchController {

    @Autowired
    private EduTeacherResearchService researchService;

    @GetMapping("/list")
    public Result<List<EduTeacherResearch>> list(@RequestParam(required = false) Long teacherId) {
        LambdaQueryWrapper<EduTeacherResearch> wrapper = new LambdaQueryWrapper<>();
        if (teacherId != null) {
            wrapper.eq(EduTeacherResearch::getTeacherId, teacherId);
        }
        return Result.success(researchService.list(wrapper));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody EduTeacherResearch research) {
        return Result.success(researchService.saveOrUpdate(research));
    }

    @DeleteMapping("/{researchId}")
    public Result<Boolean> delete(@PathVariable Long researchId) {
        return Result.success(researchService.removeById(researchId));
    }
}
