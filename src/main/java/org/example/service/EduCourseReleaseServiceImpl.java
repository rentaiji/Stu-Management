package org.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.common.Result;
import org.example.entity.EduCourse;
import org.example.entity.EduCourseRelease;
import org.example.entity.EduSemester;
import org.example.mapper.EduCourseReleaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EduCourseReleaseServiceImpl extends ServiceImpl<EduCourseReleaseMapper, EduCourseRelease> 
        implements EduCourseReleaseService {

    @Autowired
    private EduCourseReleaseMapper courseReleaseMapper;

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduSemesterService semesterService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> releaseCourse(Long courseId, Long semesterId, Long teacherId, Integer capacity) {
        if (courseId == null || semesterId == null) {
            return Result.error("参数不能为空");
        }

        EduCourse course = courseService.getById(courseId);
        if (course == null) {
            return Result.error("课程不存在");
        }

        EduSemester semester = semesterService.getById(semesterId);
        if (semester == null) {
            return Result.error("学期不存在");
        }

        // 检查该课程是否已经发布到这个学期
        LambdaQueryWrapper<EduCourseRelease> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(EduCourseRelease::getCourseId, courseId)
                   .eq(EduCourseRelease::getSemesterId, semesterId);
        EduCourseRelease existing = this.getOne(checkWrapper);

        if (existing != null) {
            return Result.error("该课程已发布过，如需修改请使用编辑功能");
        }

        // 创建新的发布记录
        EduCourseRelease release = new EduCourseRelease();
        release.setCourseId(courseId);
        release.setSemesterId(semesterId);
        release.setTeacherId(teacherId);
        release.setCapacity(capacity != null ? capacity : course.getCapacity());
        release.setSelectedCount(0);
        release.setReleaseStatus("1");
        release.setReleaseTime(LocalDateTime.now());
        release.setStartWeek(1);
        release.setEndWeek(18);

        boolean saved = this.save(release);
        return saved ? Result.success(true) : Result.error("发布失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> closeCourse(Long releaseId) {
        if (releaseId == null) {
            return Result.error("参数不能为空");
        }

        EduCourseRelease release = this.getById(releaseId);
        if (release == null) {
            return Result.error("发布记录不存在");
        }

        if (release.getSelectedCount() > 0) {
            return Result.error("已有学生选课，无法关闭");
        }

        release.setReleaseStatus("2");
        boolean updated = this.updateById(release);
        return updated ? Result.success(true) : Result.error("操作失败");
    }

    @Override
    public Result<?> getReleasedCourses(Long semesterId) {
        if (semesterId == null) {
            LambdaQueryWrapper<EduSemester> semesterWrapper = new LambdaQueryWrapper<>();
            semesterWrapper.eq(EduSemester::getIsCurrent, "1")
                          .last("LIMIT 1"); // 确保只返回一条记录
            EduSemester currentSemester = semesterService.getOne(semesterWrapper);
            if (currentSemester != null) {
                semesterId = currentSemester.getSemesterId();
            } else {
                return Result.error("当前学期未设置");
            }
        }

        List<EduCourseRelease> releases = this.list(new LambdaQueryWrapper<EduCourseRelease>()
                .eq(EduCourseRelease::getSemesterId, semesterId)
                .eq(EduCourseRelease::getReleaseStatus, "1")
                .orderByAsc(EduCourseRelease::getCreateTime));

        List<Map<String, Object>> result = releases.stream().map(release -> {
            Map<String, Object> map = new HashMap<>();
            map.put("releaseId", release.getId());
            map.put("courseId", release.getCourseId());
            map.put("semesterId", release.getSemesterId());
            map.put("teacherId", release.getTeacherId());
            map.put("capacity", release.getCapacity());
            map.put("selectedCount", release.getSelectedCount());
            map.put("releaseStatus", release.getReleaseStatus());
            map.put("classTime", release.getClassTime());
            map.put("classLocation", release.getClassLocation());
            map.put("startWeek", release.getStartWeek());
            map.put("endWeek", release.getEndWeek());

            EduCourse course = courseService.getById(release.getCourseId());
            if (course != null) {
                map.put("courseCode", course.getCourseCode());
                map.put("courseName", course.getCourseName());
                map.put("credits", course.getCredits());
                map.put("hours", course.getHours());
                map.put("courseType", course.getCourseType());
                map.put("description", course.getDescription());
            } else {
                // 课程已被删除，标记为无效
                map.put("courseName", null);
            }

            return map;
        }).filter(map -> map.get("courseName") != null) // 过滤掉课程已删除的记录
         .collect(Collectors.toList());

        return Result.success(result);
    }

    @Override
    public Result<?> getAvailableCoursesForStudent() {
        LambdaQueryWrapper<EduSemester> semesterWrapper = new LambdaQueryWrapper<>();
        semesterWrapper.eq(EduSemester::getIsCurrent, "1")
                      .last("LIMIT 1"); // 确保只返回一条记录
        EduSemester currentSemester = semesterService.getOne(semesterWrapper);
        
        if (currentSemester == null) {
            return Result.error("当前学期未设置");
        }

        return getReleasedCourses(currentSemester.getSemesterId());
    }

    @Override
    public Result<Boolean> removeByCourseAndTeacher(Long courseId, Long teacherId) {
        if (courseId == null || teacherId == null) {
            return Result.error("参数不能为空");
        }

        LambdaQueryWrapper<EduCourseRelease> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduCourseRelease::getCourseId, courseId)
               .eq(EduCourseRelease::getTeacherId, teacherId);
        
        List<EduCourseRelease> releases = this.list(wrapper);
        if (releases == null || releases.isEmpty()) {
            return Result.error("课程不存在");
        }

        // 检查是否已有学生选课
        for (EduCourseRelease release : releases) {
            if (release.getSelectedCount() > 0) {
                return Result.error("已有学生选课，无法移除");
            }
        }

        boolean removed = this.remove(wrapper);
        return removed ? Result.success(true) : Result.error("移除失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> removeByReleaseId(Long releaseId) {
        if (releaseId == null) {
            return Result.error("参数不能为空");
        }

        EduCourseRelease release = this.getById(releaseId);
        if (release == null) {
            return Result.error("课程发布记录不存在");
        }

        // 检查是否已有学生选课
        if (release.getSelectedCount() > 0) {
            return Result.error("已有学生选课，无法删除");
        }

        boolean removed = this.removeById(releaseId);
        return removed ? Result.success(true) : Result.error("删除失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateReleasedCourse(Long releaseId, Long teacherId, Integer capacity) {
        if (releaseId == null) {
            return Result.error("参数不能为空");
        }

        EduCourseRelease release = this.getById(releaseId);
        if (release == null) {
            return Result.error("课程发布记录不存在");
        }

        // 检查是否已有学生选课，如果有则不允许修改教师
        if (teacherId != null && !teacherId.equals(release.getTeacherId()) && release.getSelectedCount() > 0) {
            return Result.error("已有学生选课，无法修改授课教师");
        }

        // 更新教师
        if (teacherId != null) {
            release.setTeacherId(teacherId);
        }

        // 更新容量
        if (capacity != null) {
            if (capacity < release.getSelectedCount()) {
                return Result.error("容量不能小于已选人数（" + release.getSelectedCount() + "）");
            }
            release.setCapacity(capacity);
        }

        boolean updated = this.updateById(release);
        return updated ? Result.success(true) : Result.error("修改失败");
    }
}
