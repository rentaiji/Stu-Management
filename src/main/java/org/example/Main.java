package org.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.example.mapper")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("====================================");
        System.out.println("学生综合服务与教学协同平台启动成功!");
        System.out.println("访问地址：http://localhost:8080/api");
        System.out.println("====================================");
    }
}
