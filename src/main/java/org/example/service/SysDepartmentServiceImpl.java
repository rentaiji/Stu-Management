package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.SysDepartment;
import org.example.mapper.SysDepartmentMapper;
import org.example.service.SysDepartmentService;
import org.springframework.stereotype.Service;

@Service
public class SysDepartmentServiceImpl extends ServiceImpl<SysDepartmentMapper, SysDepartment> implements SysDepartmentService {
}
