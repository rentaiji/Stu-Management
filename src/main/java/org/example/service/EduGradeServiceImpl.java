package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.EduGrade;
import org.example.mapper.EduGradeMapper;
import org.example.service.EduGradeService;
import org.springframework.stereotype.Service;

@Service
public class EduGradeServiceImpl extends ServiceImpl<EduGradeMapper, EduGrade> implements EduGradeService {
}
