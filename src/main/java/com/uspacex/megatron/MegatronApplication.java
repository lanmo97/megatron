package com.uspacex.megatron;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 服务计算课程网站/视频播放应用
 * 威震天（Megatron）
 */
@EnableJpaAuditing
@SpringBootApplication
public class MegatronApplication {
    public static void main(String[] args) {
        SpringApplication.run(MegatronApplication.class, args);
    }
}
