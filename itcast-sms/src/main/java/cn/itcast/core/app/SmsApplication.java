package cn.itcast.core.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动器
 */
@SpringBootApplication
public class SmsApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SmsApplication.class);
        application.run(args);
    }
}
