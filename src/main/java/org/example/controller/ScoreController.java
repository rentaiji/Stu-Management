package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.common.Result;
import org.example.entity.EduCourse;
import org.example.entity.EduCourseRelease;
import org.example.entity.EduScore;
import org.example.service.EduCourseReleaseService;
import org.example.service.EduCourseService;
import org.example.service.EduScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/edu/score")
public class ScoreController {

    @Autowired
    private EduScoreService scoreService;

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduCourseReleaseService courseReleaseService;

    @GetMapping("/my-courses")
    public Result<List<Map<String, Object>>> getMyCourses(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        LambdaQueryWrapper<EduCourseRelease> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduCourseRelease::getTeacherId, userId.intValue())
               .eq(EduCourseRelease::getReleaseStatus, "1")
               .orderByAsc(EduCourseRelease::getCreateTime);
        List<EduCourseRelease> releases = courseReleaseService.list(wrapper);
        
        List<Map<String, Object>> result = releases.stream().map(release -> {
            Map<String, Object> map = new HashMap<>();
            EduCourse course = courseService.getById(release.getCourseId());
            if (course != null) {
                map.put("courseId", course.getCourseId());
                map.put("courseCode", course.getCourseCode());
                map.put("courseName", course.getCourseName());
                map.put("credits", course.getCredits());
                map.put("hours", course.getHours());
                map.put("capacity", release.getCapacity());
                map.put("selectedCount", release.getSelectedCount());
            }
            return map;
        }).collect(Collectors.toList());
        
        return Result.success(result);
    }

    @GetMapping("/student/{studentId}")
    public Result<List<EduScore>> getByStudentId(@PathVariable Long studentId) {
        LambdaQueryWrapper<EduScore> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduScore::getStudentId, studentId);
        List<EduScore> list = scoreService.list(wrapper);
        return Result.success(list);
    }

    @GetMapping("/course/{courseId}")
    public Result<List<Map<String, Object>>> getByCourseId(HttpServletRequest request, @PathVariable Long courseId) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        LambdaQueryWrapper<EduCourseRelease> releaseWrapper = new LambdaQueryWrapper<>();
        releaseWrapper.eq(EduCourseRelease::getCourseId, courseId)
                     .eq(EduCourseRelease::getTeacherId, userId.intValue());
        long count = courseReleaseService.count(releaseWrapper);
        
        if (count == 0) {
            return Result.error("您不是该课程的授课教师，无权查看成绩");
        }
        
        List<Map<String, Object>> list = scoreService.getScoresByCourse(courseId);
        return Result.success(list);
    }

    @PostMapping
    public Result<Boolean> save(HttpServletRequest request, @RequestBody EduScore score) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        LambdaQueryWrapper<EduCourseRelease> releaseWrapper = new LambdaQueryWrapper<>();
        releaseWrapper.eq(EduCourseRelease::getCourseId, score.getCourseId())
                     .eq(EduCourseRelease::getTeacherId, userId.intValue());
        long count = courseReleaseService.count(releaseWrapper);
        
        if (count == 0) {
            return Result.error("您不是该课程的授课教师，无权录入成绩");
        }
        
        boolean success = scoreService.save(score);
        return success ? Result.success(true) : Result.error("录入失败");
    }

    @PostMapping("/batch")
    public Result<Boolean> batchSave(HttpServletRequest request, @RequestBody List<EduScore> scores) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        if (scores != null && !scores.isEmpty()) {
            Long courseId = scores.get(0).getCourseId();
            LambdaQueryWrapper<EduCourseRelease> releaseWrapper = new LambdaQueryWrapper<>();
            releaseWrapper.eq(EduCourseRelease::getCourseId, courseId)
                         .eq(EduCourseRelease::getTeacherId, userId.intValue());
            long count = courseReleaseService.count(releaseWrapper);
            
            if (count == 0) {
                return Result.error("您不是该课程的授课教师，无权录入成绩");
            }
        }
        
        boolean success = scoreService.saveBatch(scores);
        return success ? Result.success(true) : Result.error("批量录入失败");
    }

    @PutMapping
    public Result<Boolean> update(HttpServletRequest request, @RequestBody EduScore score) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        LambdaQueryWrapper<EduCourseRelease> releaseWrapper = new LambdaQueryWrapper<>();
        releaseWrapper.eq(EduCourseRelease::getCourseId, score.getCourseId())
                     .eq(EduCourseRelease::getTeacherId, userId.intValue());
        long count = courseReleaseService.count(releaseWrapper);
        
        if (count == 0) {
            return Result.error("您不是该课程的授课教师，无权修改成绩");
        }
        
        boolean success = scoreService.updateById(score);
        return success ? Result.success(true) : Result.error("更新失败");
    }

    @PostMapping("/submit/{courseId}")
    public Result<Boolean> submitForReview(HttpServletRequest request, @PathVariable Long courseId) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        LambdaQueryWrapper<EduCourseRelease> releaseWrapper = new LambdaQueryWrapper<>();
        releaseWrapper.eq(EduCourseRelease::getCourseId, courseId)
                     .eq(EduCourseRelease::getTeacherId, userId.intValue());
        long count = courseReleaseService.count(releaseWrapper);
        
        if (count == 0) {
            return Result.error("您不是该课程的授课教师，无权提交审核");
        }
        
        boolean success = scoreService.submitForReview(courseId);
        return success ? Result.success(true) : Result.error("提交失败");
    }

    @PostMapping("/audit")
    public Result<Boolean> auditScore(HttpServletRequest request, @RequestBody Map<String, Object> params) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        Long scoreId = Long.valueOf(params.get("scoreId").toString());
        String action = params.get("action").toString();
        String remark = params.get("remark") != null ? params.get("remark").toString() : "";
        
        return scoreService.auditScore(scoreId, action, userId, remark);
    }

    @GetMapping("/pending-audit")
    public Result<List<Map<String, Object>>> getPendingAuditScores(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.error("用户未登录");
        }
        
        return scoreService.getPendingAuditScores();
    }
}
