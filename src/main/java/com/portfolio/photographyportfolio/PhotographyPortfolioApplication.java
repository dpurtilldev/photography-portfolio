package com.portfolio.photographyportfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.portfolio.photographyportfolio.repository")
public class PhotographyPortfolioApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotographyPortfolioApplication.class, args);
    }
}
