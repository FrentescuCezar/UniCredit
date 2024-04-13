package com.pfm.category;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PfmCategoryServiceApplication{
    public static void main(String[] args) {
        SpringApplication.run(PfmCategoryServiceApplication.class, args);
    }
}
