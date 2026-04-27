package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.common.Result;
import org.example.entity.SysLoginLog;
import org.example.service.SysLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/login-log")
public class SysLoginLogController {

    @Autowired
    private SysLoginLogService loginLogService;

    /**
     * 分页查询登录日志
     */
    @GetMapping("/list")
    public Result<Page<SysLoginLog>> getList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        Page<SysLoginLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysLoginLog> wrapper = new LambdaQueryWrapper<>();
        
        // 按用户名搜索
        if (username != null && !username.isEmpty()) {
            wrapper.like(SysLoginLog::getUsername, username)
                   .or()
                   .like(SysLoginLog::getRealName, username);
        }
        
        // 按日期范围筛选
        if (startDate != null && !startDate.isEmpty()) {
            wrapper.ge(SysLoginLog::getLoginTime, startDate + " 00:00:00");
        }
        if (endDate != null && !endDate.isEmpty()) {
            wrapper.le(SysLoginLog::getLoginTime, endDate + " 23:59:59");
        }
        
        // 按登录时间倒序
        wrapper.orderByDesc(SysLoginLog::getLoginTime);
        
        Page<SysLoginLog> result = loginLogService.page(page, wrapper);
        return Result.success(result);
    }

    /**
     * 记录登录日志
     */
    @PostMapping("/record")
    public Result<Boolean> recordLogin(@RequestBody SysLoginLog log) {
        boolean success = loginLogService.save(log);
        return success ? Result.success(true) : Result.error("记录失败");
    }

    /**
     * 删除日志
     */
    @DeleteMapping("/{logId}")
    public Result<Boolean> delete(@PathVariable Long logId) {
        boolean success = loginLogService.removeById(logId);
        return success ? Result.success(true) : Result.error("删除失败");
    }

    /**
     * 清空所有日志
     */
    @DeleteMapping("/clear")
    public Result<Boolean> clearAll() {
        boolean success = loginLogService.remove(null);
        return success ? Result.success(true) : Result.error("清空失败");
    }
}
