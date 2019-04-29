package com.rbkmoney.mockapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class MockapterApplication extends SpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockapterApplication.class, args);
    }

}
