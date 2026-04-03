package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.common.Result;
import org.example.entity.SysDepartment;
import org.example.service.SysDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/department")
public class SysDepartmentController {

    @Autowired
    private SysDepartmentService departmentService;

    @GetMapping("/list")
    public Result<List<SysDepartment>> list() {
        return Result.success(departmentService.list());
    }

    @GetMapping("/{deptId}")
    public Result<SysDepartment> getById(@PathVariable Long deptId) {
        return Result.success(departmentService.getById(deptId));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody SysDepartment department) {
        return Result.success(departmentService.saveOrUpdate(department));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody SysDepartment department) {
        return Result.success(departmentService.updateById(department));
    }

    @DeleteMapping("/{deptId}")
    public Result<Boolean> delete(@PathVariable Long deptId) {
        return Result.success(departmentService.removeById(deptId));
    }
}
