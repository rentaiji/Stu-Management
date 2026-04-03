package org.example.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.entity.SysUser;
import org.example.mapper.SysUserMapper;
import org.example.service.SysUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

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
        // 如果是明文密码（兼容旧数据），直接比较并更新为 BCrypt
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            if (!password.equals(user.getPassword())) {
                throw new RuntimeException("账号或密码错误");
            }
            // 更新为 BCrypt 加密密码
            String encodedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            update().set("password", encodedPassword).eq("user_id", user.getUserId()).update();
            user.setPassword(encodedPassword);
        } else {
            if (!BCrypt.checkpw(password, user.getPassword())) {
                throw new RuntimeException("账号或密码错误");
            }
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
}
