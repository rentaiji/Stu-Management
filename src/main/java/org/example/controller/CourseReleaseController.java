package org.example.controller;

import org.example.common.Result;
import org.example.service.EduCourseReleaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/edu/course-release")
public class CourseReleaseController {

    @Autowired
    private EduCourseReleaseService courseReleaseService;

    @PostMapping("/release")
    public Result<Boolean> releaseCourse(HttpServletRequest request, @RequestBody Map<String, Object> params) {
        // 权限检查：只有教务老师可以发布课程
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        // 检查用户是否有发布课程的权限
        org.example.service.SysUserService userService = 
            org.example.config.ApplicationContextHolder.getBean(org.example.service.SysUserService.class);
        java.util.Map<String, Object> permissions = userService.getUserPermissions(userId);
        java.util.List<String> perms = (java.util.List<String>) permissions.get("perms");
        
        if (perms == null || !perms.contains("academic:course:release")) {
            return Result.error("无权发布课程，请联系教务老师");
        }
        
        Long courseId = Long.valueOf(params.get("courseId").toString());
        Long semesterId = params.get("semesterId") != null ? Long.valueOf(params.get("semesterId").toString()) : null;
        Long teacherId = params.get("teacherId") != null ? Long.valueOf(params.get("teacherId").toString()) : null;
        Integer capacity = params.get("capacity") != null ? Integer.valueOf(params.get("capacity").toString()) : null;
        
        return courseReleaseService.releaseCourse(courseId, semesterId, teacherId, capacity);
    }

    @PostMapping("/close/{releaseId}")
    public Result<Boolean> closeCourse(HttpServletRequest request, @PathVariable Long releaseId) {
        // 权限检查：只有教务老师可以关闭课程
        String userType = (String) request.getAttribute("userType");
        if (!"2".equals(userType)) {
            return Result.error("无权操作");
        }
        
        return courseReleaseService.closeCourse(releaseId);
    }

    @GetMapping("/list")
    public Result<?> getReleasedCourses(@RequestParam(required = false) Long semesterId) {
        return courseReleaseService.getReleasedCourses(semesterId);
    }

    @GetMapping("/available")
    public Result<?> getAvailableCourses() {
        return courseReleaseService.getAvailableCoursesForStudent();
    }

    @PostMapping
    public Result<Boolean> addMyCourse(HttpServletRequest request, @RequestBody Map<String, Object> params) {
        // 权限检查：只有教务老师可以添加课程
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        // 检查用户是否有发布课程的权限
        org.example.service.SysUserService userService = 
            org.example.config.ApplicationContextHolder.getBean(org.example.service.SysUserService.class);
        java.util.Map<String, Object> permissions = userService.getUserPermissions(userId);
        java.util.List<String> perms = (java.util.List<String>) permissions.get("perms");
        
        if (perms == null || !perms.contains("academic:course:release")) {
            return Result.error("无权添加课程，请联系教务老师");
        }
        
        Long courseId = Long.valueOf(params.get("courseId").toString());
        Long teacherId = params.get("teacherId") != null ? Long.valueOf(params.get("teacherId").toString()) : null;
        Long semesterId = params.get("semesterId") != null ? Long.valueOf(params.get("semesterId").toString()) : 1L;
        Integer capacity = params.get("capacity") != null ? Integer.valueOf(params.get("capacity").toString()) : null;
        
        return courseReleaseService.releaseCourse(courseId, semesterId, teacherId, capacity);
    }

    @DeleteMapping("/{releaseId}")
    public Result<Boolean> removeMyCourse(HttpServletRequest request, @PathVariable Long releaseId) {
        // 权限检查：只有教务老师可以移除课程
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        // 检查用户是否有发布课程的权限
        org.example.service.SysUserService userService = 
            org.example.config.ApplicationContextHolder.getBean(org.example.service.SysUserService.class);
        java.util.Map<String, Object> permissions = userService.getUserPermissions(userId);
        java.util.List<String> perms = (java.util.List<String>) permissions.get("perms");
        
        if (perms == null || !perms.contains("academic:course:release")) {
            return Result.error("无权移除课程");
        }
        
        return courseReleaseService.removeByReleaseId(releaseId);
    }

    @PutMapping
    public Result<Boolean> updateReleasedCourse(HttpServletRequest request, @RequestBody Map<String, Object> params) {
        // 权限检查：只有教务老师可以修改课程
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        // 检查用户是否有发布课程的权限
        org.example.service.SysUserService userService = 
            org.example.config.ApplicationContextHolder.getBean(org.example.service.SysUserService.class);
        java.util.Map<String, Object> permissions = userService.getUserPermissions(userId);
        java.util.List<String> perms = (java.util.List<String>) permissions.get("perms");
        
        if (perms == null || !perms.contains("academic:course:release")) {
            return Result.error("无权修改课程");
        }
        
        Long releaseId = Long.valueOf(params.get("releaseId").toString());
        Long teacherId = params.get("teacherId") != null ? Long.valueOf(params.get("teacherId").toString()) : null;
        Integer capacity = params.get("capacity") != null ? Integer.valueOf(params.get("capacity").toString()) : null;
        
        return courseReleaseService.updateReleasedCourse(releaseId, teacherId, capacity);
    }
}
