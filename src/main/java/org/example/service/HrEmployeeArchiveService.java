package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.HrEmployeeArchive;

public interface HrEmployeeArchiveService extends IService<HrEmployeeArchive> {
    
    /**
     * 根据用户ID获取档案
     */
    HrEmployeeArchive getByUserId(Long userId);
    
    /**
     * 保存或更新档案
     */
    boolean saveOrUpdateArchive(HrEmployeeArchive archive);
}
