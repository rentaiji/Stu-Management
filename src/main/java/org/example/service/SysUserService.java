package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.common.Result;
import org.example.entity.SysUser;

import java.util.Map;

public interface SysUserService extends IService<SysUser> {
    SysUser getUserByUsername(String username);
    boolean register(SysUser user);
    String login(String username, String password);
    Map<String, Object> getStudentProfile(Long userId);
    Result<Boolean> updateStudent(Map<String, Object> studentData);
    Map<String, Object> getUserPermissions(Long userId);
}
