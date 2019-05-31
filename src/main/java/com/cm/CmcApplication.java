package com.cm;

import com.cm.inventory.thread.RequestProcessThreadPool;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@MapperScan("com.cm.inventory.mapper")
@SpringBootApplication
public class CmcApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CmcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        RequestProcessThreadPool.init();
    }
}
