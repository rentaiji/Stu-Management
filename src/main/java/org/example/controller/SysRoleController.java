package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.common.Result;
import org.example.entity.SysRole;
import org.example.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService roleService;

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
        return Result.success(roleService.removeById(roleId));
    }

    @PostMapping("/assign")
    public Result<Boolean> assignRole(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        List<Long> roleIds = (List<Long>) params.get("roleIds");
        
        // TODO: 实现角色分配逻辑
        return Result.success(true);
    }

    @GetMapping("/user/{userId}")
    public Result<List<SysRole>> getUserRoles(@PathVariable Long userId) {
        // TODO: 查询用户已分配的角色
        return Result.success(roleService.list());
    }
}
