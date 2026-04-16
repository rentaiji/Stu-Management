package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.common.Result;
import org.example.entity.EduCourse;
import org.example.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/edu/course")
public class CourseController {

    @Autowired
    private EduCourseService courseService;

    @GetMapping("/list")
    public Result<List<EduCourse>> list() {
        LambdaQueryWrapper<EduCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduCourse::getStatus, "0");
        List<EduCourse> list = courseService.list(wrapper);
        return Result.success(list);
    }

    @GetMapping("/{courseId}")
    public Result<EduCourse> getById(@PathVariable Long courseId) {
        EduCourse course = courseService.getById(courseId);
        return Result.success(course);
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody EduCourse course) {
        // 检查课程代码是否已存在
        LambdaQueryWrapper<EduCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduCourse::getCourseCode, course.getCourseCode());
        if (courseService.count(wrapper) > 0) {
            return Result.error("课程代码已存在");
        }
        boolean success = courseService.save(course);
        return success ? Result.success(true) : Result.error("添加失败");
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody EduCourse course) {
        boolean success = courseService.updateById(course);
        return success ? Result.success(true) : Result.error("更新失败");
    }

    @DeleteMapping("/{courseId}")
    public Result<Boolean> delete(@PathVariable Long courseId) {
        boolean success = courseService.removeById(courseId);
        return success ? Result.success(true) : Result.error("删除失败");
    }
}
