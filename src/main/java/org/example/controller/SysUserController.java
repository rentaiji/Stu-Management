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
        String username = params.get("username");
        String password = params.get("password");
        String token = userService.login(username, password);
        SysUser user = userService.getUserByUsername(username);
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
}
