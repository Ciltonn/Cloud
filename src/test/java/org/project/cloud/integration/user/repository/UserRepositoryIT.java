package org.project.cloud.integration.user.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.project.cloud.user.model.RoleUser;
import org.project.cloud.user.model.User;
import org.project.cloud.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Testcontainers
class UserRepositoryIT {
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.liquibase.enabled", () -> "false");
    }

    @Autowired
    private EntityManager entityManager;

    @Test
    void saveUser() {
        User user = new User(null, "testEmail@test", "testPassword", RoleUser.USER);
        User saved = userRepository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("testEmail@test");
    }

    @Test
    void saveUserDuplicate() {
        User user = new User(null, "testEmail@test", "testPassword", RoleUser.USER);
        User userDuplicate = new User(null, "testEmail@test", "testPassword", RoleUser.USER);
        User saved = userRepository.save(user);

        assertThat(saved.getId()).isNotNull();
        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(userDuplicate));
    }

    @Test
    void findByEmail() {
        User user = new User(null, "testEmail@test", "testPassword", RoleUser.USER);
        User saved = userRepository.save(user);
        Optional<User> found = userRepository.findByEmail("testEmail@test");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo(saved.getEmail());
    }

    @Test
    void existsByEmail() {
        User user = new User(null, "testEmail@test", "testPassword", RoleUser.USER);
        userRepository.save(user);
        boolean exists = userRepository.existsByEmail("testEmail@test");
        assertThat(exists).isTrue();
    }
}