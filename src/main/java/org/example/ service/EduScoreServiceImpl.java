NEW_FILE_CODE
package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.EduScore;
import org.example.mapper.EduScoreMapper;
import org.example.service.EduScoreService;
import org.springframework.stereotype.Service;

@Service
public class EduScoreServiceImpl extends ServiceImpl<EduScoreMapper, EduScore> implements EduScoreService {
}
