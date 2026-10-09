package com.portfolio.photographyportfolio.config;

import de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Configuration;

/**
 * Test configuration to enable embedded MongoDB for tests
 */
@Configuration
@ImportAutoConfiguration(EmbeddedMongoAutoConfiguration.class)
public class TestMongoConfig {
    // This configuration class enables embedded MongoDB for all tests
}
