package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.common.Result;
import org.example.entity.EduCourseRelease;
import org.example.entity.EduStudentCourse;
import org.example.service.EduCourseReleaseService;
import org.example.service.EduStudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/edu/course")
public class EduCourseEnrollController {

    @Autowired
    private EduStudentCourseService studentCourseService;
    
    @Autowired
    private EduCourseReleaseService courseReleaseService;

    @PostMapping("/enroll")
    public Result<Boolean> enroll(@RequestBody Map<String, Object> params) {
        Long studentId = Long.valueOf(params.get("studentId").toString());
        Long releaseId = params.get("releaseId") != null ? 
            Long.valueOf(params.get("releaseId").toString()) : null;
        Long courseId = params.get("courseId") != null ? 
            Long.valueOf(params.get("courseId").toString()) : null;
        Long semesterId = params.get("semesterId") != null ? 
            Long.valueOf(params.get("semesterId").toString()) : null;

        // 1. 获取课程发布记录
        EduCourseRelease release;
        if (releaseId != null) {
            // 优先使用 releaseId
            release = courseReleaseService.getById(releaseId);
        } else if (courseId != null && semesterId != null) {
            // 兼容旧接口，使用 courseId + semesterId
            LambdaQueryWrapper<EduCourseRelease> releaseWrapper = new LambdaQueryWrapper<>();
            releaseWrapper.eq(EduCourseRelease::getCourseId, courseId)
                         .eq(EduCourseRelease::getSemesterId, semesterId)
                         .eq(EduCourseRelease::getReleaseStatus, "1")
                         .last("LIMIT 1");
            release = courseReleaseService.getOne(releaseWrapper);
        } else {
            return Result.error("参数不完整");
        }
        
        if (release == null) {
            return Result.error("该课程未发布或不存在");
        }
        
        // 2. 检查容量
        if (release.getSelectedCount() >= release.getCapacity()) {
            return Result.error("该课程已满，无法选课");
        }

        // 3. 检查学生是否已选该课程
        LambdaQueryWrapper<EduStudentCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduStudentCourse::getStudentId, studentId)
               .eq(EduStudentCourse::getCourseId, release.getCourseId())
               .eq(EduStudentCourse::getSemesterId, release.getSemesterId());
        
        if (studentCourseService.count(wrapper) > 0) {
            return Result.error("您已选修该课程");
        }

        // 4. 创建选课记录
        EduStudentCourse studentCourse = new EduStudentCourse();
        studentCourse.setStudentId(studentId);
        studentCourse.setCourseId(release.getCourseId());
        studentCourse.setSemesterId(release.getSemesterId());
        studentCourse.setSelectTime(LocalDateTime.now());
        studentCourse.setSelectStatus("1"); // 1-已选
        studentCourse.setAuditStatus("1");  // 1-已通过

        boolean success = studentCourseService.save(studentCourse);
        
        if (success) {
            // 5. 更新已选人数
            release.setSelectedCount(release.getSelectedCount() + 1);
            courseReleaseService.updateById(release);
        }
        
        return Result.success(success);
    }

    @PostMapping("/drop")
    public Result<Boolean> drop(@RequestBody Map<String, Object> params) {
        Long studentId = Long.valueOf(params.get("studentId").toString());
        Long courseId = Long.valueOf(params.get("courseId").toString());

        // 1. 查找选课记录
        LambdaQueryWrapper<EduStudentCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduStudentCourse::getStudentId, studentId)
               .eq(EduStudentCourse::getCourseId, courseId);
        EduStudentCourse studentCourse = studentCourseService.getOne(wrapper);
        
        if (studentCourse == null) {
            return Result.error("未找到选课记录");
        }

        // 2. 删除选课记录
        boolean removed = studentCourseService.remove(wrapper);
        
        if (removed) {
            // 3. 更新已选人数
            LambdaQueryWrapper<EduCourseRelease> releaseWrapper = new LambdaQueryWrapper<>();
            releaseWrapper.eq(EduCourseRelease::getCourseId, courseId)
                         .eq(EduCourseRelease::getSemesterId, studentCourse.getSemesterId())
                         .last("LIMIT 1");
            EduCourseRelease release = courseReleaseService.getOne(releaseWrapper);
            
            if (release != null && release.getSelectedCount() > 0) {
                release.setSelectedCount(release.getSelectedCount() - 1);
                courseReleaseService.updateById(release);
            }
        }
        
        return Result.success(removed);
    }

    @DeleteMapping("/unenroll/{courseId}/{studentId}")
    public Result<Boolean> unenroll(@PathVariable Long courseId, @PathVariable Long studentId) {
        LambdaQueryWrapper<EduStudentCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduStudentCourse::getCourseId, courseId)
               .eq(EduStudentCourse::getStudentId, studentId);
        
        return Result.success(studentCourseService.remove(wrapper));
    }
}
