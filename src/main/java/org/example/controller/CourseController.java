package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.common.Result;
import org.example.entity.EduCourse;
import org.example.entity.EduCourseRelease;
import org.example.service.EduCourseReleaseService;
import org.example.service.EduCourseService;
import org.example.service.EduStudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/edu/course")
public class CourseController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduStudentCourseService studentCourseService;

    @Autowired
    private EduCourseReleaseService courseReleaseService;

    @GetMapping("/list")
    public Result<List<EduCourse>> list() {
        LambdaQueryWrapper<EduCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduCourse::getStatus, "0");
        List<EduCourse> list = courseService.list(wrapper);
        return Result.success(list);
    }

    @GetMapping("/available")
    public Result<?> getAvailableCourses() {
        return courseReleaseService.getAvailableCoursesForStudent();
    }

    @GetMapping("/{courseId}")
    public Result<EduCourse> getById(@PathVariable Long courseId) {
        EduCourse course = courseService.getById(courseId);
        return Result.success(course);
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody EduCourse course) {
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
        // 1. 先查询该课程的所有排课记录
        LambdaQueryWrapper<EduCourseRelease> releaseWrapper = new LambdaQueryWrapper<>();
        releaseWrapper.eq(EduCourseRelease::getCourseId, courseId);
        List<EduCourseRelease> releases = courseReleaseService.list(releaseWrapper);
        
        // 2. 对每个排课记录，删除对应的学生选课记录
        for (EduCourseRelease release : releases) {
            LambdaQueryWrapper<org.example.entity.EduStudentCourse> studentCourseWrapper = new LambdaQueryWrapper<>();
            studentCourseWrapper.eq(org.example.entity.EduStudentCourse::getCourseId, courseId)
                               .eq(org.example.entity.EduStudentCourse::getSemesterId, release.getSemesterId());
            studentCourseService.remove(studentCourseWrapper);
        }
        
        // 3. 删除排课记录
        courseReleaseService.remove(releaseWrapper);
        
        // 4. 最后删除课程
        boolean success = courseService.removeById(courseId);
        return success ? Result.success(true) : Result.error("删除失败");
    }

    @PostMapping("/select/{courseId}")
    public Result<Boolean> selectCourse(HttpServletRequest request, @PathVariable Long courseId) {
        Long studentId = (Long) request.getAttribute("userId");
        if (studentId == null) {
            return Result.error("用户未登录");
        }
        return studentCourseService.selectCourse(studentId, courseId);
    }

    @PostMapping("/drop/{courseId}")
    public Result<Boolean> dropCourse(HttpServletRequest request, @PathVariable Long courseId) {
        Long studentId = (Long) request.getAttribute("userId");
        if (studentId == null) {
            return Result.error("用户未登录");
        }
        return studentCourseService.dropCourse(studentId, courseId);
    }

    @GetMapping("/my-courses")
    public Result<?> getMyCourses(HttpServletRequest request) {
        Long studentId = (Long) request.getAttribute("userId");
        if (studentId == null) {
            return Result.error("用户未登录");
        }
        return studentCourseService.getSelectedCourses(studentId);
    }

    @GetMapping("/students/{courseId}")
    public Result<?> getCourseStudents(HttpServletRequest request, @PathVariable Long courseId) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        LambdaQueryWrapper<EduCourseRelease> releaseWrapper = new LambdaQueryWrapper<>();
        releaseWrapper.eq(EduCourseRelease::getCourseId, courseId)
                     .eq(EduCourseRelease::getTeacherId, userId.intValue());
        long count = courseReleaseService.count(releaseWrapper);
        
        if (count == 0) {
            return Result.error("您不是该课程的授课教师");
        }
        
        return studentCourseService.getCourseStudents(courseId);
    }
}
