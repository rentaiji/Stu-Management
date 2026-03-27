NEW_FILE_CODE
package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.entity.EduCourse;

@Mapper
public interface EduCourseMapper extends BaseMapper<EduCourse> {
}
