package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.common.Result;
import org.example.entity.EduCourse;
import org.example.entity.EduCourseRelease;
import org.example.entity.EduStudentCourse;
import org.example.service.EduCourseReleaseService;
import org.example.service.EduCourseService;
import org.example.service.EduStudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/edu/teacher")
public class EduTeacherController {

    @Autowired
    private EduCourseReleaseService courseReleaseService;
    
    @Autowired
    private EduCourseService courseService;
    
    @Autowired
    private EduStudentCourseService studentCourseService;

    /**
     * 获取教师的课程列表(用于课表)
     */
    @GetMapping("/courses")
    public Result<List<Map<String, Object>>> getTeacherCourses(@RequestParam Long teacherId) {
        LambdaQueryWrapper<EduCourseRelease> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduCourseRelease::getTeacherId, teacherId)
               .eq(EduCourseRelease::getReleaseStatus, "1");
        
        List<EduCourseRelease> releases = courseReleaseService.list(wrapper);
        
        List<Map<String, Object>> result = releases.stream().map(release -> {
            Map<String, Object> map = new HashMap<>();
            // 课程发布信息
            map.put("courseId", release.getCourseId());
            map.put("releaseId", release.getId());
            map.put("capacity", release.getCapacity());
            map.put("selectedCount", release.getSelectedCount());
            map.put("classTime", release.getClassTime());
            map.put("classLocation", release.getClassLocation());
            map.put("weeks", release.getStartWeek() + "-" + release.getEndWeek() + "周");
            
            // 关联查询课程详细信息
            EduCourse course = courseService.getById(release.getCourseId());
            if (course != null) {
                map.put("courseCode", course.getCourseCode());
                map.put("courseName", course.getCourseName());
                map.put("credits", course.getCredits());
                map.put("hours", course.getHours());
            } else {
                // 课程已被删除，标记为无效
                map.put("courseName", null);
            }
            
            return map;
        }).filter(map -> map.get("courseName") != null) // 过滤掉课程已删除的记录
         .collect(Collectors.toList());
        
        return Result.success(result);
    }

    /**
     * 获取某课程的选课学生
     */
    @GetMapping("/course/students/{courseId}")
    public Result<?> getCourseStudents(@PathVariable Long courseId) {
        return studentCourseService.getCourseStudents(courseId);
    }

    /**
     * 获取课程的学生名单（教师工作台用）
     */
    @GetMapping("/course-students/{courseId}")
    public Result<?> getCourseStudentList(@PathVariable Long courseId) {
        return studentCourseService.getCourseStudents(courseId);
    }
}
