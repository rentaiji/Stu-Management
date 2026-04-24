package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.common.Result;
import org.example.entity.EduStudentCourse;

public interface EduStudentCourseService extends IService<EduStudentCourse> {
    
    Result<Boolean> selectCourse(Long studentId, Long courseId);
    
    Result<Boolean> dropCourse(Long studentId, Long courseId);
    
    Result<?> getSelectedCourses(Long studentId);
    
    Result<?> getCourseStudents(Long courseId);
}
