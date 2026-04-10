package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.EduCompetition;
import org.example.mapper.EduCompetitionMapper;
import org.example.service.EduCompetitionService;
import org.springframework.stereotype.Service;

@Service
public class EduCompetitionServiceImpl extends ServiceImpl<EduCompetitionMapper, EduCompetition> implements EduCompetitionService {
}
