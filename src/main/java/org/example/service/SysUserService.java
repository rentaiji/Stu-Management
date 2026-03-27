package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.SysUser;

public interface SysUserService extends IService<SysUser> {
    SysUser getUserByUsername(String username);
    boolean register(SysUser user);
    String login(String username, String password);
}
