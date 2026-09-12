package org.project.cloud;

import org.junit.jupiter.api.TestInstance;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public abstract class IntegrationTestBase {
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17");

    static {
        postgreSQLContainer.start();
    }
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl());
        registry.add("spring.liquibase.enabled", () -> "false");
    }
}
