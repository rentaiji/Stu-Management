package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.common.Result;
import org.example.entity.EduCourseRelease;

import java.util.List;

public interface EduCourseReleaseService extends IService<EduCourseRelease> {
    
    Result<Boolean> releaseCourse(Long courseId, Long semesterId, Long teacherId, Integer capacity);
    
    Result<Boolean> closeCourse(Long releaseId);
    
    Result<?> getReleasedCourses(Long semesterId);
    
    Result<?> getAvailableCoursesForStudent();
    
    Result<Boolean> removeByCourseAndTeacher(Long courseId, Long teacherId);
    
    Result<Boolean> removeByReleaseId(Long releaseId);
    
    Result<Boolean> updateReleasedCourse(Long releaseId, Long teacherId, Integer capacity);
}
