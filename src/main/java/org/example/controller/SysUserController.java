package org.example.controller;

import org.example.common.Result;
import org.example.entity.SysUser;
import org.example.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService userService;
    
    @Autowired(required = false)
    private org.example.mapper.EduStudentExtMapper eduStudentExtMapper;
    
    /**
     * 解析日期字符串，支持多种格式
     */
    private java.time.LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            // ISO 8601 格式: 2011-04-04T16:00:00.000Z
            if (dateStr.contains("T")) {
                return java.time.LocalDateTime.parse(dateStr).toLocalDate();
            }
            // 简单日期格式: 2011-04-04
            return java.time.LocalDate.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String account = params.get("account");
        String password = params.get("password");
        String token = userService.login(account, password);
        SysUser user = userService.getUserByUsername(account);
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);
        return Result.success(data);
    }

    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody SysUser user) {
        boolean success = userService.register(user);
        return success ? Result.success(true) : Result.error("注册失败，用户名已存在");
    }

    @GetMapping("/{userId}")
    public Result<SysUser> getUserById(@PathVariable Long userId) {
        SysUser user = userService.getById(userId);
        return Result.success(user);
    }

    @GetMapping("/list")
    public Result<java.util.List<SysUser>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String userType) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysUser> wrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(SysUser::getUserName, keyword)
                    .or().like(SysUser::getRealName, keyword)
                    .or().like(SysUser::getPhone, keyword));
        }
        if (userType != null && !userType.isEmpty()) {
            wrapper.eq(SysUser::getUserType, userType);
        }
        return Result.success(userService.list(wrapper));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody Map<String, Object> userData) {
        // 提取 SysUser 字段
        SysUser user = new SysUser();
        user.setUserId(userData.get("userId") != null ? Long.valueOf(userData.get("userId").toString()) : null);
        user.setUserName((String) userData.get("userName"));
        user.setRealName((String) userData.get("realName"));
        user.setGender((String) userData.get("gender"));
        user.setPhone((String) userData.get("phone"));
        user.setEmail((String) userData.get("email"));
        user.setDeptId(userData.get("deptId") != null ? Long.valueOf(userData.get("deptId").toString()) : null);
        user.setBirthDate(parseDate((String) userData.get("birthDate")));
        user.setIdCard((String) userData.get("idCard"));
        user.setNation((String) userData.get("nation"));
        user.setPoliticalStatus(userData.get("politicalStatus") != null ? Integer.valueOf(userData.get("politicalStatus").toString()) : null);
        user.setNativePlace((String) userData.get("nativePlace"));
        user.setHomeAddress((String) userData.get("homeAddress"));
        // 从前端获取 userType，默认为学生
        user.setUserType(userData.get("userType") != null ? (String) userData.get("userType") : "0");
        
        // 判断是新增还是更新
        if (user.getUserId() == null) {
            // 检查学号是否已存在
            SysUser existUser = userService.getUserByUsername(user.getUserName());
            if (existUser != null) {
                return Result.error("学号/工号已存在");
            }
            // 明文密码
            user.setPassword((String) userData.get("password"));
            
            // 保存用户
            boolean saved = userService.save(user);
            if (!saved) {
                return Result.error("保存失败");
            }
            
            // 创建扩展表记录
            if (userData.containsKey("gradeId") || userData.containsKey("classId") || 
                userData.containsKey("majorId") || userData.containsKey("emergencyContact")) {
                org.example.entity.EduStudentExt ext = new org.example.entity.EduStudentExt();
                ext.setStudentId(user.getUserId());
                if (userData.containsKey("gradeId") && userData.get("gradeId") != null) 
                    ext.setGradeId(Long.valueOf(userData.get("gradeId").toString()));
                if (userData.containsKey("classId") && userData.get("classId") != null) 
                    ext.setClassId(Long.valueOf(userData.get("classId").toString()));
                if (userData.containsKey("majorId") && userData.get("majorId") != null) 
                    ext.setMajorId(Long.valueOf(userData.get("majorId").toString()));
                if (userData.containsKey("deptId")) ext.setDeptId(user.getDeptId());
                if (userData.containsKey("educationLevel")) ext.setEducationLevel((String) userData.get("educationLevel"));
                if (userData.containsKey("studyLength") && userData.get("studyLength") != null) 
                    ext.setStudyLength(Integer.valueOf(userData.get("studyLength").toString()));
                if (userData.containsKey("entranceYear") && userData.get("entranceYear") != null) 
                    ext.setEntranceYear(Integer.valueOf(userData.get("entranceYear").toString()));
                if (userData.containsKey("studentStatus")) ext.setStudentStatus((String) userData.get("studentStatus"));
                if (userData.containsKey("emergencyContact")) ext.setEmergencyContact((String) userData.get("emergencyContact"));
                if (userData.containsKey("emergencyPhone")) ext.setEmergencyPhone((String) userData.get("emergencyPhone"));
                
                if (eduStudentExtMapper != null) {
                    eduStudentExtMapper.insert(ext);
                }
            }
            
            return Result.success(true);
        } else {
            // 更新时如果不传密码，保留原密码
            SysUser oldUser = userService.getById(user.getUserId());
            if (oldUser != null && (user.getPassword() == null || user.getPassword().isEmpty())) {
                user.setPassword(oldUser.getPassword());
            }
            
            // 更新用户
            userService.updateById(user);
            
            // 更新扩展表
            if (userData.containsKey("gradeId") || userData.containsKey("emergencyContact")) {
                Map<String, Object> extData = new java.util.HashMap<>();
                extData.put("userId", user.getUserId());
                if (userData.containsKey("phone")) extData.put("phone", userData.get("phone"));
                if (userData.containsKey("email")) extData.put("email", userData.get("email"));
                if (userData.containsKey("homeAddress")) extData.put("homeAddress", userData.get("homeAddress"));
                if (userData.containsKey("emergencyContact")) extData.put("emergencyContact", userData.get("emergencyContact"));
                if (userData.containsKey("emergencyPhone")) extData.put("emergencyPhone", userData.get("emergencyPhone"));
                userService.updateStudent(extData);
            }
            
            return Result.success(true);
        }
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody Map<String, Object> userData) {
        // 提取 SysUser 字段
        SysUser user = new SysUser();
        user.setUserId(userData.get("userId") != null ? Long.valueOf(userData.get("userId").toString()) : null);
        user.setUserName((String) userData.get("userName"));
        user.setRealName((String) userData.get("realName"));
        user.setGender((String) userData.get("gender"));
        user.setPhone((String) userData.get("phone"));
        user.setEmail((String) userData.get("email"));
        user.setDeptId(userData.get("deptId") != null ? Long.valueOf(userData.get("deptId").toString()) : null);
        user.setBirthDate(parseDate((String) userData.get("birthDate")));
        user.setIdCard((String) userData.get("idCard"));
        user.setNation((String) userData.get("nation"));
        user.setPoliticalStatus(userData.get("politicalStatus") != null ? Integer.valueOf(userData.get("politicalStatus").toString()) : null);
        user.setNativePlace((String) userData.get("nativePlace"));
        user.setHomeAddress((String) userData.get("homeAddress"));
        
        // 如果不传密码，保留原密码
        if (userData.get("password") == null || ((String) userData.get("password")).isEmpty()) {
            SysUser oldUser = userService.getById(user.getUserId());
            if (oldUser != null) {
                user.setPassword(oldUser.getPassword());
            }
        } else {
            // 明文密码
            user.setPassword((String) userData.get("password"));
        }
        
        // 更新用户
        userService.updateById(user);
        
        // 更新扩展表
        if (eduStudentExtMapper != null) {
            org.example.entity.EduStudentExt ext = eduStudentExtMapper.selectById(user.getUserId());
            if (ext == null) {
                // 创建新记录
                ext = new org.example.entity.EduStudentExt();
                ext.setStudentId(user.getUserId());
            }
            
            if (userData.containsKey("gradeId") && userData.get("gradeId") != null) 
                ext.setGradeId(Long.valueOf(userData.get("gradeId").toString()));
            if (userData.containsKey("classId") && userData.get("classId") != null) 
                ext.setClassId(Long.valueOf(userData.get("classId").toString()));
            if (userData.containsKey("majorId") && userData.get("majorId") != null) 
                ext.setMajorId(Long.valueOf(userData.get("majorId").toString()));
            if (userData.containsKey("deptId")) ext.setDeptId(user.getDeptId());
            if (userData.containsKey("educationLevel")) ext.setEducationLevel((String) userData.get("educationLevel"));
            if (userData.containsKey("studyLength") && userData.get("studyLength") != null) 
                ext.setStudyLength(Integer.valueOf(userData.get("studyLength").toString()));
            if (userData.containsKey("entranceYear") && userData.get("entranceYear") != null) 
                ext.setEntranceYear(Integer.valueOf(userData.get("entranceYear").toString()));
            if (userData.containsKey("studentStatus")) ext.setStudentStatus((String) userData.get("studentStatus"));
            if (userData.containsKey("emergencyContact")) ext.setEmergencyContact((String) userData.get("emergencyContact"));
            if (userData.containsKey("emergencyPhone")) ext.setEmergencyPhone((String) userData.get("emergencyPhone"));
            
            if (ext.getStudentId() != null) {
                if (eduStudentExtMapper.selectById(ext.getStudentId()) == null) {
                    eduStudentExtMapper.insert(ext);
                } else {
                    eduStudentExtMapper.updateById(ext);
                }
            }
        }
        
        return Result.success(true);
    }

    @PutMapping("/student")
    public Result<Boolean> updateStudent(@RequestBody Map<String, Object> studentData) {
        return userService.updateStudent(studentData);
    }

    @PutMapping("/password")
    public Result<Boolean> changePassword(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        String oldPassword = (String) params.get("oldPassword");
        String newPassword = (String) params.get("newPassword");
        
        SysUser user = userService.getById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        // 验证旧密码
        if (!oldPassword.equals(user.getPassword())) {
            return Result.error("原密码错误");
        }
        
        // 更新新密码
        user.setPassword(newPassword);
        boolean success = userService.updateById(user);
        
        return success ? Result.success(true) : Result.error("修改失败");
    }

    @DeleteMapping("/{userId}")
    public Result<Boolean> delete(@PathVariable Long userId) {
        return Result.success(userService.removeById(userId));
    }

    @GetMapping("/student/profile/{userId}")
    public Result<Map<String, Object>> getStudentProfile(@PathVariable Long userId) {
        Map<String, Object> profile = userService.getStudentProfile(userId);
        return profile != null ? Result.success(profile) : Result.error("未找到学生信息");
    }

    @GetMapping("/permissions/{userId}")
    public Result<Map<String, Object>> getUserPermissions(@PathVariable Long userId) {
        Map<String, Object> permissions = userService.getUserPermissions(userId);
        return Result.success(permissions);
    }
}
