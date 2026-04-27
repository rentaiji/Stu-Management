package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.common.Result;
import org.example.entity.EduCourse;
import org.example.entity.EduScore;
import org.example.entity.EduStudent;
import org.example.entity.EduStudentCourse;
import org.example.mapper.EduCourseMapper;
import org.example.mapper.EduScoreMapper;
import org.example.mapper.EduStudentCourseMapper;
import org.example.mapper.EduStudentMapper;
import org.example.mapper.SysUserMapper;
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

    @Autowired
    private EduCourseMapper courseMapper;
    
    @Autowired
    private EduStudentCourseMapper studentCourseMapper;
    
    @Autowired
    private SysUserMapper userMapper;

    @Override
    public List<Map<String, Object>> getScoresByCourse(Long courseId) {
        // 先查询该课程的所有选课学生
        LambdaQueryWrapper<EduStudentCourse> scWrapper = new LambdaQueryWrapper<>();
        scWrapper.eq(EduStudentCourse::getCourseId, courseId)
                 .eq(EduStudentCourse::getSelectStatus, "1"); // 只查已选课的
        List<EduStudentCourse> studentCourses = studentCourseMapper.selectList(scWrapper);
        
        // 获取所有学生ID和userId
        Set<Long> studentIds = studentCourses.stream()
            .map(EduStudentCourse::getStudentId)
            .collect(Collectors.toSet());
        Set<Long> userIds = new HashSet<>();
        
        // 查询学生信息
        Map<Long, EduStudent> studentMap = new HashMap<>();
        if (!studentIds.isEmpty()) {
            List<EduStudent> students = studentMapper.selectBatchIds(studentIds);
            studentMap = students.stream()
                .collect(Collectors.toMap(EduStudent::getStudentId, s -> s));
            // 收集所有userId
            userIds = students.stream()
                .map(EduStudent::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        }
        
        // 查询用户信息获取姓名
        Map<Long, String> userNameMap = new HashMap<>();
        if (!userIds.isEmpty()) {
            LambdaQueryWrapper<org.example.entity.SysUser> userWrapper = new LambdaQueryWrapper<>();
            userWrapper.in(org.example.entity.SysUser::getUserId, userIds);
            List<org.example.entity.SysUser> users = userMapper.selectList(userWrapper);
            userNameMap = users.stream()
                .collect(Collectors.toMap(
                    org.example.entity.SysUser::getUserId,
                    u -> u.getRealName() != null ? u.getRealName() : u.getUserName(),
                    (u1, u2) -> u1
                ));
        }
        
        // 查询已有的成绩记录
        LambdaQueryWrapper<EduScore> scoreWrapper = new LambdaQueryWrapper<>();
        scoreWrapper.eq(EduScore::getCourseId, courseId);
        List<EduScore> scores = this.list(scoreWrapper);
        Map<Long, EduScore> scoreMap = scores.stream()
            .collect(Collectors.toMap(EduScore::getStudentId, s -> s, (s1, s2) -> s1));
        
        // 组装返回数据(包含所有选课学生,即使没有成绩)
        List<Map<String, Object>> result = new ArrayList<>();
        for (EduStudentCourse sc : studentCourses) {
            Map<String, Object> item = new HashMap<>();
            EduStudent student = studentMap.get(sc.getStudentId());
            EduScore score = scoreMap.get(sc.getStudentId());
            
            if (score != null) {
                item.put("scoreId", score.getScoreId());
                item.put("usualScore", score.getRegularScore());
                item.put("midtermScore", BigDecimal.ZERO);
                item.put("finalScore", score.getFinalScore());
                item.put("totalScore", score.getTotalScore());
                item.put("status", score.getStatus());
                item.put("locked", "1".equals(score.getStatus()));
            } else {
                // 没有成绩记录,初始化空值
                item.put("scoreId", null);
                item.put("usualScore", null);
                item.put("midtermScore", null);
                item.put("finalScore", null);
                item.put("totalScore", null);
                item.put("status", "0");
                item.put("locked", false);
            }
            
            item.put("studentId", sc.getStudentId());
            item.put("courseId", courseId);
            item.put("semesterId", sc.getSemesterId());
            
            if (student != null) {
                item.put("studentNo", student.getStudentNo());
                // 从sys_user表获取姓名
                String studentName = userNameMap.get(student.getUserId());
                item.put("studentName", studentName != null ? studentName : "学生" + student.getStudentNo());
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

    @Override
    public Result<Boolean> auditScore(Long scoreId, String action, Long auditorId, String remark) {
        EduScore score = this.getById(scoreId);
        if (score == null) {
            return Result.error("成绩记录不存在");
        }
        
        if (!"1".equals(score.getStatus())) {
            return Result.error("该成绩不是待审核状态");
        }
        
        if ("approve".equals(action)) {
            score.setStatus("2");
        } else if ("reject".equals(action)) {
            score.setStatus("0");
            score.setRemark(remark != null ? remark : "审核不通过");
        } else {
            return Result.error("无效的操作");
        }
        
        boolean updated = this.updateById(score);
        return updated ? Result.success(true) : Result.error("操作失败");
    }

    @Override
    public Result<List<Map<String, Object>>> getPendingAuditScores() {
        LambdaQueryWrapper<EduScore> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduScore::getStatus, "1");
        List<EduScore> scores = this.list(wrapper);
        
        Set<Long> studentIds = scores.stream().map(EduScore::getStudentId).collect(Collectors.toSet());
        Set<Long> courseIds = scores.stream().map(EduScore::getCourseId).collect(Collectors.toSet());
        
        Map<Long, EduStudent> studentMap = new HashMap<>();
        Map<Long, EduCourse> courseMap = new HashMap<>();
        
        if (!studentIds.isEmpty()) {
            List<EduStudent> students = studentMapper.selectBatchIds(studentIds);
            studentMap = students.stream().collect(Collectors.toMap(EduStudent::getStudentId, s -> s));
        }
        
        if (!courseIds.isEmpty()) {
            List<EduCourse> courses = courseMapper.selectBatchIds(courseIds);
            courseMap = courses.stream().collect(Collectors.toMap(EduCourse::getCourseId, c -> c));
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (EduScore score : scores) {
            Map<String, Object> item = new HashMap<>();
            EduStudent student = studentMap.get(score.getStudentId());
            EduCourse course = courseMap.get(score.getCourseId());
            
            item.put("scoreId", score.getScoreId());
            item.put("studentId", score.getStudentId());
            item.put("courseId", score.getCourseId());
            item.put("totalScore", score.getTotalScore());
            item.put("status", score.getStatus());
            item.put("inputTime", score.getInputTime());
            
            if (student != null) {
                item.put("studentNo", student.getStudentNo());
                item.put("studentName", student.getRealName());
            }
            
            if (course != null) {
                item.put("courseName", course.getCourseName());
                item.put("courseCode", course.getCourseCode());
            }
            
            result.add(item);
        }
        
        return Result.success(result);
    }
}
