package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.ResPaper;
import org.example.mapper.ResPaperMapper;
import org.example.service.ResPaperService;
import org.springframework.stereotype.Service;

@Service
public class ResPaperServiceImpl extends ServiceImpl<ResPaperMapper, ResPaper> implements ResPaperService {
}
