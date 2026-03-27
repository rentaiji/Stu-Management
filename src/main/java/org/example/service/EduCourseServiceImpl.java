package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.EduCourse;
import org.example.mapper.EduCourseMapper;
import org.example.service.EduCourseService;
import org.springframework.stereotype.Service;

@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
}
