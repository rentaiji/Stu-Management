package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.common.Result;
import org.example.entity.EduCompetition;
import org.example.service.EduCompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/edu/competition")
public class EduCompetitionController {

    @Autowired
    private EduCompetitionService competitionService;

    @GetMapping("/list")
    public Result<List<EduCompetition>> list(@RequestParam(required = false) Long studentId) {
        LambdaQueryWrapper<EduCompetition> wrapper = new LambdaQueryWrapper<>();
        if (studentId != null) {
            wrapper.eq(EduCompetition::getStudentId, studentId);
        }
        wrapper.orderByDesc(EduCompetition::getCreateTime);
        return Result.success(competitionService.list(wrapper));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody EduCompetition competition) {
        competition.setStatus("0");
        return Result.success(competitionService.save(competition));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody EduCompetition competition) {
        return Result.success(competitionService.updateById(competition));
    }

    @DeleteMapping("/{competitionId}")
    public Result<Boolean> delete(@PathVariable Long competitionId) {
        return Result.success(competitionService.removeById(competitionId));
    }
}
