package com.demodd.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*@SpringBootApplication(scanBasePackages = {
    "com.demodd.infrastructure",
    "com.demodd.application",
    "com.demodd.domain"
})
@EnableJpaRepositories(basePackages = "com.demodd.infrastructure")
@EntityScan(basePackages = "com.demodd.infrastructure")*/
@SpringBootApplication(scanBasePackages = "com.demodd")
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
