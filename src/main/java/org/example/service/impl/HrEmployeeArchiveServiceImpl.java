package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.HrEmployeeArchive;
import org.example.mapper.HrEmployeeArchiveMapper;
import org.example.service.HrEmployeeArchiveService;
import org.springframework.stereotype.Service;

@Service
public class HrEmployeeArchiveServiceImpl extends ServiceImpl<HrEmployeeArchiveMapper, HrEmployeeArchive> 
        implements HrEmployeeArchiveService {

    @Override
    public HrEmployeeArchive getByUserId(Long userId) {
        // 使用自定义查询方法，忽略逻辑删除条件
        return this.baseMapper.selectByUserIdIncludingDeleted(userId);
    }

    @Override
    public boolean saveOrUpdateArchive(HrEmployeeArchive archive) {
        // 使用自定义查询方法，忽略逻辑删除条件
        HrEmployeeArchive existingArchive = this.baseMapper.selectByUserIdIncludingDeleted(archive.getUserId());
        
        if (existingArchive != null) {
            // 档案已存在，执行更新
            archive.setArchiveId(existingArchive.getArchiveId());
            System.out.println("更新档案，ID: " + archive.getArchiveId() + ", userId: " + archive.getUserId() + ", deleted: " + existingArchive.getDeleted());
            
            // 如果之前被逻辑删除，先恢复
            if (existingArchive.getDeleted() == 1) {
                this.baseMapper.restoreDeleted(existingArchive.getArchiveId());
                System.out.println("恢复已删除的档案记录");
            }
            
            boolean result = this.updateById(archive);
            System.out.println("更新结果: " + result);
            return result;
        } else {
            // 档案不存在，执行新增
            archive.setArchiveStatus("已建档");
            System.out.println("创建新档案，userId: " + archive.getUserId() + ", 设置状态为: " + archive.getArchiveStatus());
            boolean result = this.save(archive);
            System.out.println("保存结果: " + result + ", 档案ID: " + archive.getArchiveId());
            return result;
        }
    }
}
