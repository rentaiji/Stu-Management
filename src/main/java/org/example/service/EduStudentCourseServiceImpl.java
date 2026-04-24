package org.example.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.common.Result;
import org.example.entity.EduCourse;
import org.example.entity.EduCourseRelease;
import org.example.entity.EduSemester;
import org.example.entity.EduStudent;
import org.example.entity.EduStudentCourse;
import org.example.entity.EduStudentExt;
import org.example.entity.SysUser;
import org.example.mapper.EduStudentCourseMapper;
import org.example.mapper.EduStudentExtMapper;
import org.example.mapper.EduStudentMapper;
import org.example.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EduStudentCourseServiceImpl extends ServiceImpl<EduStudentCourseMapper, EduStudentCourse> 
        implements EduStudentCourseService {

    @Autowired
    private EduStudentCourseMapper studentCourseMapper;

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduSemesterService semesterService;

    @Autowired
    private EduCourseReleaseService courseReleaseService;

    @Autowired
    private EduStudentMapper studentMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private EduStudentExtMapper studentExtMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> selectCourse(Long studentId, Long courseId) {
        if (studentId == null || courseId == null) {
            return Result.error("参数不能为空");
        }

        LambdaQueryWrapper<EduSemester> semesterWrapper = new LambdaQueryWrapper<>();
        semesterWrapper.eq(EduSemester::getIsCurrent, "1")
                      .last("LIMIT 1"); // 确保只返回一条记录
        EduSemester currentSemester = semesterService.getOne(semesterWrapper);
        
        if (currentSemester == null) {
            return Result.error("当前学期未设置");
        }

        LambdaQueryWrapper<EduCourseRelease> releaseWrapper = new LambdaQueryWrapper<>();
        releaseWrapper.eq(EduCourseRelease::getCourseId, courseId)
                     .eq(EduCourseRelease::getSemesterId, currentSemester.getSemesterId())
                     .eq(EduCourseRelease::getReleaseStatus, "1")
                     .last("LIMIT 1"); // 确保只返回一条记录
        EduCourseRelease release = courseReleaseService.getOne(releaseWrapper);
        
        if (release == null) {
            return Result.error("该课程当前学期未发布或已关闭");
        }

        if (release.getSelectedCount() >= release.getCapacity()) {
            return Result.error("课程容量已满");
        }

        LambdaQueryWrapper<EduStudentCourse> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(EduStudentCourse::getStudentId, studentId)
                   .eq(EduStudentCourse::getCourseId, courseId)
                   .eq(EduStudentCourse::getSemesterId, currentSemester.getSemesterId());
        long count = studentCourseMapper.selectCount(checkWrapper);
        
        if (count > 0) {
            return Result.error("您已选修该课程");
        }

        EduStudentCourse studentCourse = new EduStudentCourse();
        studentCourse.setStudentId(studentId);
        studentCourse.setCourseId(courseId);
        studentCourse.setSemesterId(currentSemester.getSemesterId());
        studentCourse.setSelectTime(LocalDateTime.now());
        studentCourse.setSelectStatus("0");
        studentCourse.setAuditStatus("0");

        boolean saved = this.save(studentCourse);
        
        if (saved) {
            release.setSelectedCount(release.getSelectedCount() + 1);
            courseReleaseService.updateById(release);
            return Result.success(true);
        } else {
            return Result.error("选课失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> dropCourse(Long studentId, Long courseId) {
        if (studentId == null || courseId == null) {
            return Result.error("参数不能为空");
        }

        LambdaQueryWrapper<EduSemester> semesterWrapper = new LambdaQueryWrapper<>();
        semesterWrapper.eq(EduSemester::getIsCurrent, "1")
                      .last("LIMIT 1"); // 确保只返回一条记录
        EduSemester currentSemester = semesterService.getOne(semesterWrapper);
        
        if (currentSemester == null) {
            return Result.error("当前学期未设置");
        }

        LambdaQueryWrapper<EduStudentCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduStudentCourse::getStudentId, studentId)
               .eq(EduStudentCourse::getCourseId, courseId)
               .eq(EduStudentCourse::getSemesterId, currentSemester.getSemesterId());
        
        EduStudentCourse studentCourse = this.getOne(wrapper);
        
        if (studentCourse == null) {
            return Result.error("未找到选课记录");
        }

        boolean removed = this.remove(wrapper);
        
        if (removed) {
            LambdaQueryWrapper<EduCourseRelease> releaseWrapper = new LambdaQueryWrapper<>();
            releaseWrapper.eq(EduCourseRelease::getCourseId, courseId)
                         .eq(EduCourseRelease::getSemesterId, currentSemester.getSemesterId())
                         .last("LIMIT 1"); // 确保只返回一条记录
            EduCourseRelease release = courseReleaseService.getOne(releaseWrapper);
            
            if (release != null && release.getSelectedCount() > 0) {
                release.setSelectedCount(release.getSelectedCount() - 1);
                courseReleaseService.updateById(release);
            }
            return Result.success(true);
        } else {
            return Result.error("退课失败");
        }
    }

    @Override
    public Result<?> getSelectedCourses(Long studentId) {
        if (studentId == null) {
            return Result.error("参数不能为空");
        }

        LambdaQueryWrapper<EduSemester> semesterWrapper = new LambdaQueryWrapper<>();
        semesterWrapper.eq(EduSemester::getIsCurrent, "1")
                      .last("LIMIT 1"); // 确保只返回一条记录
        EduSemester currentSemester = semesterService.getOne(semesterWrapper);
        
        if (currentSemester == null) {
            return Result.error("当前学期未设置");
        }

        List<EduStudentCourse> studentCourses = this.list(new LambdaQueryWrapper<EduStudentCourse>()
                .eq(EduStudentCourse::getStudentId, studentId)
                .eq(EduStudentCourse::getSemesterId, currentSemester.getSemesterId()));

        List<Map<String, Object>> result = new java.util.ArrayList<>();
        for (EduStudentCourse sc : studentCourses) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sc.getId());
            map.put("courseId", sc.getCourseId());
            map.put("semesterId", sc.getSemesterId());
            map.put("selectTime", sc.getSelectTime());
            map.put("selectStatus", sc.getSelectStatus());
            map.put("auditStatus", sc.getAuditStatus());
            
            EduCourse course = courseService.getById(sc.getCourseId());
            if (course != null) {
                map.put("courseCode", course.getCourseCode());
                map.put("courseName", course.getCourseName());
                map.put("credits", course.getCredits());
                map.put("hours", course.getHours());
                map.put("teacherId", course.getTeacherId());
                
                // 获取课程发布记录，获取教师信息和上课信息
                LambdaQueryWrapper<EduCourseRelease> releaseWrapper = new LambdaQueryWrapper<>();
                releaseWrapper.eq(EduCourseRelease::getCourseId, sc.getCourseId())
                             .eq(EduCourseRelease::getSemesterId, sc.getSemesterId())
                             .last("LIMIT 1");
                EduCourseRelease release = courseReleaseService.getOne(releaseWrapper);
                
                if (release != null) {
                    map.put("releaseId", release.getId());
                    map.put("classTime", release.getClassTime());
                    map.put("classLocation", release.getClassLocation());
                    map.put("teacherId", release.getTeacherId());
                }
            }
            
            result.add(map);
        }

        return Result.success(result);
    }

    @Override
    public Result<?> getCourseStudents(Long courseId) {
        if (courseId == null) {
            return Result.error("参数不能为空");
        }

        System.out.println("=== 开始查询课程学生名单, courseId: " + courseId + " ===");

        LambdaQueryWrapper<EduSemester> semesterWrapper = new LambdaQueryWrapper<>();
        semesterWrapper.eq(EduSemester::getIsCurrent, "1")
                      .last("LIMIT 1"); // 确保只返回一条记录
        EduSemester currentSemester = semesterService.getOne(semesterWrapper);
        
        if (currentSemester == null) {
            System.out.println("错误：当前学期未设置");
            return Result.error("当前学期未设置");
        }
        
        System.out.println("当前学期ID: " + currentSemester.getSemesterId());

        List<EduStudentCourse> studentCourses = this.list(new LambdaQueryWrapper<EduStudentCourse>()
                .eq(EduStudentCourse::getCourseId, courseId)
                .eq(EduStudentCourse::getSemesterId, currentSemester.getSemesterId()));
        
        System.out.println("找到 " + studentCourses.size() + " 条选课记录");

        List<Map<String, Object>> result = new java.util.ArrayList<>();
        for (EduStudentCourse sc : studentCourses) {
            Map<String, Object> map = new HashMap<>();
            map.put("studentId", sc.getStudentId());
            map.put("selectTime", sc.getSelectTime());
            
            System.out.println("处理学生ID: " + sc.getStudentId());
            
            // 查询学生基本信息
            LambdaQueryWrapper<EduStudent> studentWrapper = new LambdaQueryWrapper<>();
            studentWrapper.eq(EduStudent::getStudentId, sc.getStudentId());
            EduStudent eduStudent = studentMapper.selectOne(studentWrapper);
            
            if (eduStudent != null) {
                System.out.println("找到学生信息: " + eduStudent.getRealName());
                map.put("studentNo", eduStudent.getStudentNo());
                map.put("realName", eduStudent.getRealName());
                map.put("idCard", eduStudent.getIdCard());
                map.put("birthDate", eduStudent.getBirthDate());
                map.put("nation", eduStudent.getNation());
                map.put("politicalStatus", eduStudent.getPoliticalStatus());
                map.put("enrollmentYear", eduStudent.getEnrollmentYear());
                
                // 从 sys_user 获取联系方式
                if (eduStudent.getUserId() != null) {
                    System.out.println("学生关联的userId: " + eduStudent.getUserId());
                    SysUser sysUser = sysUserMapper.selectById(eduStudent.getUserId());
                    if (sysUser != null) {
                        map.put("phone", sysUser.getPhone());
                        map.put("email", sysUser.getEmail());
                        System.out.println("手机号: " + sysUser.getPhone() + ", 邮箱: " + sysUser.getEmail());
                    } else {
                        System.out.println("警告：未找到sys_user记录");
                        map.put("phone", "");
                        map.put("email", "");
                    }
                } else {
                    System.out.println("警告：学生没有关联userId");
                    map.put("phone", "");
                    map.put("email", "");
                }
                
                // 从 edu_student_ext 获取扩展信息
                LambdaQueryWrapper<EduStudentExt> extWrapper = new LambdaQueryWrapper<>();
                extWrapper.eq(EduStudentExt::getStudentId, sc.getStudentId());
                EduStudentExt studentExt = studentExtMapper.selectOne(extWrapper);
                if (studentExt != null) {
                    map.put("emergencyContact", studentExt.getEmergencyContact());
                    map.put("emergencyPhone", studentExt.getEmergencyPhone());
                } else {
                    map.put("emergencyContact", "");
                    map.put("emergencyPhone", "");
                }
            } else {
                System.out.println("错误：未找到edu_student记录, studentId: " + sc.getStudentId());
                // 即使没有找到学生信息，也设置默认值
                map.put("studentNo", "未知");
                map.put("realName", "未知");
                map.put("phone", "");
                map.put("email", "");
            }
            
            result.add(map);
        }
        
        System.out.println("最终返回结果数量: " + result.size());

        return Result.success(result);
    }
}
