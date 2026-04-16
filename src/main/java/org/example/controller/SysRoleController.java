package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.common.Result;
import org.example.entity.SysRole;
import org.example.entity.SysUserRole;
import org.example.mapper.SysUserRoleMapper;
import org.example.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private org.example.service.SysUserService userService;

    @GetMapping("/list")
    public Result<List<SysRole>> list() {
        return Result.success(roleService.list());
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody SysRole role) {
        return Result.success(roleService.saveOrUpdate(role));
    }

    @DeleteMapping("/{roleId}")
    public Result<Boolean> delete(@PathVariable Long roleId) {
        // 禁止删除系统内置角色（ID 1-7）
        if (roleId <= 7) {
            return Result.error("系统内置角色不可删除");
        }
        return Result.success(roleService.removeById(roleId));
    }

    @Transactional
    @PostMapping("/assign")
    public Result<Boolean> assignRole(@RequestBody Map<String, Object> params) {
        // 兼容 Integer 和 Long 类型
        Object userIdObj = params.get("userId");
        Long userId = userIdObj instanceof Integer ? ((Integer) userIdObj).longValue() : (Long) userIdObj;
        
        List<Long> roleIds = new java.util.ArrayList<>();
        List<?> rawRoleIds = (List<?>) params.get("roleIds");
        if (rawRoleIds != null) {
            for (Object roleIdObj : rawRoleIds) {
                if (roleIdObj instanceof Integer) {
                    roleIds.add(((Integer) roleIdObj).longValue());
                } else {
                    roleIds.add((Long) roleIdObj);
                }
            }
        }
        
        // 删除用户原有角色
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        userRoleMapper.delete(wrapper);
        
        // 添加新角色
        if (!roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            }
        }
        
        return Result.success(true);
    }

    @GetMapping("/user/{userId}")
    public Result<List<SysRole>> getUserRoles(@PathVariable Long userId) {
        // 查询用户已分配的角色
        List<SysUserRole> userRoles = userRoleMapper.selectList(
            new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId)
        );
        
        List<Long> roleIds = userRoles.stream()
            .map(SysUserRole::getRoleId)
            .collect(Collectors.toList());
        
        if (roleIds.isEmpty()) {
            return Result.success(List.of());
        }
        
        List<SysRole> roles = roleService.listByIds(roleIds);
        return Result.success(roles);
    }

    @GetMapping("/users/{roleId}")
    public Result<List<org.example.entity.SysUser>> getRoleUsers(@PathVariable Long roleId) {
        // 查询拥有该角色的所有用户
        List<SysUserRole> userRoles = userRoleMapper.selectList(
            new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, roleId)
        );
        
        List<Long> userIds = userRoles.stream()
            .map(SysUserRole::getUserId)
            .collect(Collectors.toList());
        
        if (userIds.isEmpty()) {
            return Result.success(List.of());
        }
        
        List<org.example.entity.SysUser> users = userService.listByIds(userIds);
        return Result.success(users);
    }
}
