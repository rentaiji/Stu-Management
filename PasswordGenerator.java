package org.example;

import cn.hutool.crypto.digest.BCrypt;

public class PasswordGenerator {
    public static void main(String[] args) {
        String password = "admin123";
        String salt = BCrypt.gensalt();
        String hashed = BCrypt.hashpw(password, salt);
        
        System.out.println("原始密码：" + password);
        System.out.println("BCrypt 密码：" + hashed);
        
        // 验证
        boolean matches = BCrypt.checkpw(password, hashed);
        System.out.println("验证结果：" + matches);
    }
}
