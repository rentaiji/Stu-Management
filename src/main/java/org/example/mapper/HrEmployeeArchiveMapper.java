package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.entity.HrEmployeeArchive;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HrEmployeeArchiveMapper extends BaseMapper<HrEmployeeArchive> {
    
    /**
     * 查询用户档案（包括已逻辑删除的）
     */
    @Select("SELECT * FROM hr_employee_archive WHERE user_id = #{userId} LIMIT 1")
    HrEmployeeArchive selectByUserIdIncludingDeleted(Long userId);
    
    /**
     * 恢复已删除的档案
     */
    @Update("UPDATE hr_employee_archive SET deleted = 0 WHERE archive_id = #{archiveId}")
    int restoreDeleted(Long archiveId);
}
