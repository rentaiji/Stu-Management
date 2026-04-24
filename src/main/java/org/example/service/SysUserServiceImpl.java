package org.example.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.common.Result;
import org.example.entity.SysUser;
import org.example.mapper.SysUserMapper;
import org.example.mapper.EduStudentMapper;
import org.example.mapper.EduStudentExtMapper;
import org.example.mapper.SysUserRoleMapper;
import org.example.mapper.SysRoleMenuMapper;
import org.example.mapper.SysMenuMapper;
import org.example.entity.EduStudent;
import org.example.entity.EduStudentExt;
import org.example.entity.SysUserRole;
import org.example.entity.SysRoleMenu;
import org.example.entity.SysMenu;
import org.example.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @Autowired(required = false)
    private EduStudentMapper eduStudentMapper;

    @Autowired(required = false)
    private EduStudentExtMapper eduStudentExtMapper;

    @Autowired(required = false)
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired(required = false)
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired(required = false)
    private SysMenuMapper sysMenuMapper;

    @Override
    public SysUser getUserByUsername(String account) {
        // 使用用户名（学号/工号）登录
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserName, account);
        return getOne(wrapper);
    }

    @Override
    public boolean register(SysUser user) {
        SysUser existUser = getUserByUsername(user.getUserName());
        if (existUser != null) {
            return false;
        }
        String encodedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(encodedPassword);
        user.setStatus("0");
        user.setUserType("0");
        return save(user);
    }

    @Override
    public String login(String account, String password) {
        SysUser user = getUserByUsername(account);
        if (user == null) {
            throw new RuntimeException("账号或密码错误");
        }
        // 明文密码比较
        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("账号或密码错误");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUserName());
        claims.put("userType", user.getUserType());
        claims.put("realName", user.getRealName());
        claims.put("phone", user.getPhone());
        claims.put("email", user.getEmail());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(account)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    @Override
    public Map<String, Object> getStudentProfile(Long userId) {
        // 获取用户基本信息
        SysUser user = getById(userId);
        if (user == null) {
            return null;
        }

        Map<String, Object> profile = new HashMap<>();
        // 用户基本信息（来自sys_user表）
        profile.put("userId", user.getUserId());
        profile.put("userName", user.getUserName());
        profile.put("realName", user.getRealName());
        profile.put("gender", user.getGender());
        profile.put("phone", user.getPhone());
        profile.put("email", user.getEmail());
        profile.put("avatar", user.getAvatar());
        profile.put("deptId", user.getDeptId());
        // sys_user表中的学生字段
        profile.put("birthDate", user.getBirthDate());
        profile.put("idCard", user.getIdCard());
        profile.put("nation", user.getNation());
        profile.put("politicalStatus", user.getPoliticalStatus());
        profile.put("nativePlace", user.getNativePlace());
        profile.put("homeAddress", user.getHomeAddress());

        // 获取学生扩展信息（来自edu_student表）
        if (eduStudentMapper != null) {
            LambdaQueryWrapper<EduStudent> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(EduStudent::getUserId, userId);
            EduStudent student = eduStudentMapper.selectOne(wrapper);
            
            if (student != null) {
                // edu_student表字段
                profile.put("studentNo", student.getStudentNo());
                profile.put("birthPlace", student.getBirthPlace());
                profile.put("enrollmentYear", student.getEnrollmentYear());
                profile.put("studyMode", student.getStudyMode());
                profile.put("entryDate", student.getEntryDate());
                profile.put("graduateDate", student.getGraduateDate());
            }
        }

        // 获取学生扩展表信息（edu_student_ext）
        if (eduStudentExtMapper != null) {
            EduStudentExt ext = eduStudentExtMapper.selectById(userId);
            if (ext != null) {
                profile.put("gradeId", ext.getGradeId());
                profile.put("classId", ext.getClassId());
                profile.put("majorId", ext.getMajorId());
                profile.put("deptId", ext.getDeptId());
                profile.put("educationLevel", ext.getEducationLevel());
                profile.put("studyLength", ext.getStudyLength());
                profile.put("entranceYear", ext.getEntranceYear());
                profile.put("studentStatus", ext.getStudentStatus());
                profile.put("emergencyContact", ext.getEmergencyContact());
                profile.put("emergencyPhone", ext.getEmergencyPhone());
            }
        }

        return profile;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateStudent(Map<String, Object> studentData) {
        Long userId = Long.valueOf(studentData.get("userId").toString());
        
        // 更新 sys_user 表
        SysUser user = new SysUser();
        user.setUserId(userId);
        if (studentData.containsKey("phone")) user.setPhone((String) studentData.get("phone"));
        if (studentData.containsKey("email")) user.setEmail((String) studentData.get("email"));
        if (studentData.containsKey("homeAddress")) user.setHomeAddress((String) studentData.get("homeAddress"));
        boolean userUpdated = this.updateById(user);
        
        // 更新 edu_student_ext 表
        if (eduStudentExtMapper != null) {
            EduStudentExt ext = eduStudentExtMapper.selectById(userId);
            if (ext != null) {
                if (studentData.containsKey("emergencyContact")) {
                    ext.setEmergencyContact((String) studentData.get("emergencyContact"));
                }
                if (studentData.containsKey("emergencyPhone")) {
                    ext.setEmergencyPhone((String) studentData.get("emergencyPhone"));
                }
                eduStudentExtMapper.updateById(ext);
            }
        }
        
        return userUpdated ? Result.success(true) : Result.error("更新失败");
    }

    @Override
    public Map<String, Object> getUserPermissions(Long userId) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取用户角色
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(
            new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId)
        );
        
        List<Long> roleIds = userRoles.stream()
            .map(SysUserRole::getRoleId)
            .collect(Collectors.toList());
        
        result.put("roleIds", roleIds);
        
        // 获取角色对应的菜单权限
        if (!roleIds.isEmpty()) {
            List<SysRoleMenu> roleMenus = sysRoleMenuMapper.selectList(
                new LambdaQueryWrapper<SysRoleMenu>().in(SysRoleMenu::getRoleId, roleIds)
            );
            
            List<Long> menuIds = roleMenus.stream()
                .map(SysRoleMenu::getMenuId)
                .distinct()
                .collect(Collectors.toList());
            
            result.put("menuIds", menuIds);
            
            // 获取菜单详情
            if (!menuIds.isEmpty()) {
                List<SysMenu> menus = sysMenuMapper.selectBatchIds(menuIds);
                result.put("menus", menus);
                
                // 提取权限标识
                List<String> perms = menus.stream()
                    .map(SysMenu::getPerms)
                    .filter(p -> p != null && !p.isEmpty())
                    .collect(Collectors.toList());
                result.put("perms", perms);
            }
        } else {
            result.put("menuIds", List.of());
            result.put("menus", List.of());
            result.put("perms", List.of());
        }
        
        return result;
    }
}
