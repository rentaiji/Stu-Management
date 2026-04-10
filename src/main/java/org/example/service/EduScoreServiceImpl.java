package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.EduScore;
import org.example.entity.EduStudent;
import org.example.mapper.EduScoreMapper;
import org.example.mapper.EduStudentMapper;
import org.example.service.EduScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EduScoreServiceImpl extends ServiceImpl<EduScoreMapper, EduScore> implements EduScoreService {

    @Autowired
    private EduStudentMapper studentMapper;

    @Override
    public List<Map<String, Object>> getScoresByCourse(Long courseId) {
        // 查询该课程的所有成绩记录
        LambdaQueryWrapper<EduScore> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduScore::getCourseId, courseId);
        List<EduScore> scores = this.list(wrapper);
        
        // 获取所有学生ID
        Set<Long> studentIds = scores.stream()
            .map(EduScore::getStudentId)
            .collect(Collectors.toSet());
        
        // 查询学生信息
        Map<Long, EduStudent> studentMap = new HashMap<>();
        if (!studentIds.isEmpty()) {
            List<EduStudent> students = studentMapper.selectBatchIds(studentIds);
            studentMap = students.stream()
                .collect(Collectors.toMap(EduStudent::getStudentId, s -> s));
        }
        
        // 组装返回数据
        List<Map<String, Object>> result = new ArrayList<>();
        for (EduScore score : scores) {
            Map<String, Object> item = new HashMap<>();
            EduStudent student = studentMap.get(score.getStudentId());
            
            item.put("scoreId", score.getScoreId());
            item.put("studentId", score.getStudentId());
            item.put("courseId", score.getCourseId());
            item.put("semesterId", score.getSemesterId());
            item.put("usualScore", score.getRegularScore());
            item.put("midtermScore", BigDecimal.ZERO); // 暂不支持期中
            item.put("finalScore", score.getFinalScore());
            item.put("totalScore", score.getTotalScore());
            item.put("status", score.getStatus());
            item.put("locked", "1".equals(score.getStatus())); // 已审核则锁定
            
            if (student != null) {
                item.put("studentNo", student.getStudentNo());
                // TODO: 需要从 sys_user 表关联查询 realName
                item.put("studentName", student.getRealName() != null ? student.getRealName() : "学生" + student.getStudentNo());
            }
            
            result.add(item);
        }
        
        return result;
    }

    @Override
    public boolean submitForReview(Long courseId) {
        // 将该课程下所有草稿状态的成绩改为待审核
        LambdaQueryWrapper<EduScore> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduScore::getCourseId, courseId)
               .eq(EduScore::getStatus, "0"); // 0=草稿
        
        EduScore updateEntity = new EduScore();
        updateEntity.setStatus("1"); // 1=待审核
        
        return this.update(updateEntity, wrapper);
    }
}
