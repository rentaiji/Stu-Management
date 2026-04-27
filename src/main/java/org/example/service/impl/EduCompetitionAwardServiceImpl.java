package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.EduCompetitionAward;
import org.example.mapper.EduCompetitionAwardMapper;
import org.example.service.EduCompetitionAwardService;
import org.springframework.stereotype.Service;

@Service
public class EduCompetitionAwardServiceImpl extends ServiceImpl<EduCompetitionAwardMapper, EduCompetitionAward> 
        implements EduCompetitionAwardService {
}
