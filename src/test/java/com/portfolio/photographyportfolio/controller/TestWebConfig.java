package com.portfolio.photographyportfolio.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * Test configuration to exclude MongoDB and Security autoconfiguration for controller tests
 */
@Configuration
@EnableAutoConfiguration(exclude = {
    MongoAutoConfiguration.class, 
    MongoDataAutoConfiguration.class,
    SecurityAutoConfiguration.class
})
public class TestWebConfig {
}
