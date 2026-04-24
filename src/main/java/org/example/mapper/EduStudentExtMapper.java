package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.entity.EduStudentExt;

import java.util.List;

@Mapper
public interface EduStudentExtMapper extends BaseMapper<EduStudentExt> {
    
    /**
     * 根据年级ID查询学生扩展信息
     */
    @Select("SELECT * FROM edu_student_ext WHERE grade_id = #{gradeId}")
    List<EduStudentExt> selectByGradeId(@Param("gradeId") Long gradeId);
    
    /**
     * 根据班级ID查询学生扩展信息
     */
    @Select("SELECT * FROM edu_student_ext WHERE class_id = #{classId}")
    List<EduStudentExt> selectByClassId(@Param("classId") Long classId);
    
    /**
     * 根据专业ID查询学生扩展信息
     */
    @Select("SELECT * FROM edu_student_ext WHERE major_id = #{majorId}")
    List<EduStudentExt> selectByMajorId(@Param("majorId") Long majorId);
    
    /**
     * 根据院系ID查询学生扩展信息
     */
    @Select("SELECT * FROM edu_student_ext WHERE dept_id = #{deptId}")
    List<EduStudentExt> selectByDeptId(@Param("deptId") Long deptId);
}
