package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.EduSemester;
import org.example.mapper.EduSemesterMapper;
import org.example.service.EduSemesterService;
import org.springframework.stereotype.Service;

@Service
public class EduSemesterServiceImpl extends ServiceImpl<EduSemesterMapper, EduSemester> implements EduSemesterService {
}
