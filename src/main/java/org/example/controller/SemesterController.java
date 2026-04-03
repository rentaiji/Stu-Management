package org.example.controller;

import org.example.common.Result;
import org.example.entity.EduSemester;
import org.example.service.EduSemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/edu/semester")
public class SemesterController {

    @Autowired
    private EduSemesterService semesterService;

    @GetMapping("/list")
    public Result<List<EduSemester>> list() {
        return Result.success(semesterService.list());
    }

    @GetMapping("/{semesterId}")
    public Result<EduSemester> getById(@PathVariable Long semesterId) {
        return Result.success(semesterService.getById(semesterId));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody EduSemester semester) {
        return Result.success(semesterService.saveOrUpdate(semester));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody EduSemester semester) {
        return Result.success(semesterService.updateById(semester));
    }

    @DeleteMapping("/{semesterId}")
    public Result<Boolean> delete(@PathVariable Long semesterId) {
        return Result.success(semesterService.removeById(semesterId));
    }
}
