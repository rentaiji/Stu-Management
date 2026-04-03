package org.example.controller;

import org.example.common.Result;
import org.example.entity.SysUser;
import org.example.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService userService;

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
    public Result<Boolean> save(@RequestBody SysUser user) {
        // 新增时加密密码并检查学号唯一性
        if (user.getUserId() == null) {
            // 检查学号是否已存在
            SysUser existUser = userService.getUserByUsername(user.getUserName());
            if (existUser != null) {
                return Result.error("学号已存在");
            }
            String encodedPassword = cn.hutool.crypto.digest.BCrypt.hashpw(user.getPassword(), 
                cn.hutool.crypto.digest.BCrypt.gensalt());
            user.setPassword(encodedPassword);
        }
        return Result.success(userService.saveOrUpdate(user));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody SysUser user) {
        return Result.success(userService.updateById(user));
    }

    @DeleteMapping("/{userId}")
    public Result<Boolean> delete(@PathVariable Long userId) {
        return Result.success(userService.removeById(userId));
    }
}
