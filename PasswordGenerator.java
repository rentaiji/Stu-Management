import cn.hutool.crypto.digest.BCrypt;

public class PasswordGenerator {
    public static void main(String[] args) {
        String password = "123456";
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("原始密码: " + password);
        System.out.println("BCrypt哈希: " + hashed);
        System.out.println("\n验证结果: " + BCrypt.checkpw(password, hashed));
        
        // 测试旧哈希
        String oldHash = "$2a$10$N.zxrWvOlqMDvpQfMzKWyO3Yp4JKVLPqN9sVN8bEJxLZLKxLKxLKx";
        System.out.println("\n旧哈希验证: " + BCrypt.checkpw(password, oldHash));
    }
}
