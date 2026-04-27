package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.common.Result;
import org.example.entity.*;
import org.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

@RestController
@RequestMapping("/sys/statistics")
public class StatisticsController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduCompetitionAwardService competitionService;

    @Autowired(required = false)
    private HrEmployeeArchiveService archiveService;

    @Autowired(required = false)
    private SysLoginLogService loginLogService;

    @Autowired(required = false)
    private SysDepartmentService departmentService;

    /**
     * 获取学生统计信息
     */
    @GetMapping("/students")
    public Result<Map<String, Object>> getStudentStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 学生总数
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserType, "0");
        long totalStudents = userService.count(wrapper);
        
        stats.put("totalStudents", totalStudents);
        
        return Result.success(stats);
    }

    /**
     * 获取教师统计信息
     */
    @GetMapping("/teachers")
    public Result<Map<String, Object>> getTeacherStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 教师总数
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserType, "1");
        long totalTeachers = userService.count(wrapper);
        
        stats.put("totalTeachers", totalTeachers);
        
        return Result.success(stats);
    }

    /**
     * 获取课程统计信息
     */
    @GetMapping("/courses")
    public Result<Map<String, Object>> getCourseStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 课程总数
        long totalCourses = courseService.count();
        
        stats.put("totalCourses", totalCourses);
        
        return Result.success(stats);
    }

    /**
     * 获取竞赛申报统计信息
     */
    @GetMapping("/competitions")
    public Result<Map<String, Object>> getCompetitionStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 总申报数
        long totalApplications = competitionService.count();
        
        // 待审核数
        LambdaQueryWrapper<EduCompetitionAward> pendingWrapper = new LambdaQueryWrapper<>();
        pendingWrapper.eq(EduCompetitionAward::getStatus, "待审核");
        long pendingCount = competitionService.count(pendingWrapper);
        
        // 已通过数
        LambdaQueryWrapper<EduCompetitionAward> approvedWrapper = new LambdaQueryWrapper<>();
        approvedWrapper.eq(EduCompetitionAward::getStatus, "已通过");
        long approvedCount = competitionService.count(approvedWrapper);
        
        // 已驳回数
        LambdaQueryWrapper<EduCompetitionAward> rejectedWrapper = new LambdaQueryWrapper<>();
        rejectedWrapper.eq(EduCompetitionAward::getStatus, "已驳回");
        long rejectedCount = competitionService.count(rejectedWrapper);
        
        stats.put("totalApplications", totalApplications);
        stats.put("pendingCount", pendingCount);
        stats.put("approvedCount", approvedCount);
        stats.put("rejectedCount", rejectedCount);
        
        return Result.success(stats);
    }

    /**
     * 获取部门统计信息
     */
    @GetMapping("/departments")
    public Result<Map<String, Object>> getDepartmentStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        if (departmentService != null) {
            long totalDepartments = departmentService.count();
            stats.put("totalDepartments", totalDepartments);
        } else {
            stats.put("totalDepartments", 0);
        }
        
        return Result.success(stats);
    }

    /**
     * 获取人事档案统计信息
     */
    @GetMapping("/hr-archives")
    public Result<Map<String, Object>> getHrArchiveStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        if (archiveService != null) {
            // 已建档人数（档案状态为"已建档"）
            LambdaQueryWrapper<HrEmployeeArchive> archivedWrapper = new LambdaQueryWrapper<>();
            archivedWrapper.eq(HrEmployeeArchive::getArchiveStatus, "已建档");
            long archivedCount = archiveService.count(archivedWrapper);
            
            // 教师总数
            LambdaQueryWrapper<SysUser> teacherWrapper = new LambdaQueryWrapper<>();
            teacherWrapper.eq(SysUser::getUserType, "1");
            long totalTeachers = userService.count(teacherWrapper);
            
            // 未建档人数 = 教师总数 - 已建档人数
            long notArchivedCount = totalTeachers - archivedCount;
            
            stats.put("archivedCount", archivedCount);
            stats.put("notArchivedCount", notArchivedCount > 0 ? notArchivedCount : 0);
        } else {
            stats.put("archivedCount", 0);
            stats.put("notArchivedCount", 0);
        }
        
        return Result.success(stats);
    }

    /**
     * 获取登录统计信息
     */
    @GetMapping("/login")
    public Result<Map<String, Object>> getLoginStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        if (loginLogService != null) {
            // 今日登录次数
            String today = LocalDate.now().toString() + " 00:00:00";
            LambdaQueryWrapper<SysLoginLog> todayWrapper = new LambdaQueryWrapper<>();
            todayWrapper.ge(SysLoginLog::getLoginTime, today);
            long todayCount = loginLogService.count(todayWrapper);
            
            // 今日登录人数（去重）
            List<SysLoginLog> todayLogs = loginLogService.list(todayWrapper);
            Set<Long> todayUserIds = new HashSet<>();
            for (SysLoginLog log : todayLogs) {
                if (log.getUserId() != null) {
                    todayUserIds.add(log.getUserId());
                }
            }
            long todayUsers = todayUserIds.size();
            
            // 总登录次数
            long totalCount = loginLogService.count();
            
            // 总登录人数（去重）
            List<SysLoginLog> allLogs = loginLogService.list();
            Set<Long> allUserIds = new HashSet<>();
            for (SysLoginLog log : allLogs) {
                if (log.getUserId() != null) {
                    allUserIds.add(log.getUserId());
                }
            }
            long totalUsers = allUserIds.size();
            
            stats.put("todayCount", todayCount);
            stats.put("todayUsers", todayUsers);
            stats.put("totalCount", totalCount);
            stats.put("totalUsers", totalUsers);
        } else {
            stats.put("todayCount", 0);
            stats.put("todayUsers", 0);
            stats.put("totalCount", 0);
            stats.put("totalUsers", 0);
        }
        
        return Result.success(stats);
    }
}
