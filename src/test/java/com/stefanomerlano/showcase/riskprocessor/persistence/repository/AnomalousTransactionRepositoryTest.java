package com.stefanomerlano.showcase.riskprocessor.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.stefanomerlano.showcase.riskprocessor.persistence.entity.AnomalousTransactionEntity;

/**
 * Integration test for the persistence layer (JPA Repository) using @DataJpaTest.
 * This test slice loads only the JPA components and connects to a real PostgreSQL
 * database managed by Testcontainers.
 */
@DataJpaTest // Focuses the test only on JPA components
@Testcontainers
class AnomalousTransactionRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired // Spring will inject the real repository bean
    private AnomalousTransactionRepository repository;

    @Test
    void whenSaveAndFindById_thenEntityIsPersistedCorrectly() {
        // --- Arrange ---
        AnomalousTransactionEntity newEntity = new AnomalousTransactionEntity();
        newEntity.setTransactionId("tx-jpa-test");
        newEntity.setAmount(new BigDecimal("12345.67"));
        newEntity.setCurrency("CHF");
        newEntity.setTimestamp(Instant.now());
        newEntity.setOriginator("test-orig-jpa");
        newEntity.setBeneficiary("test-bene-jpa");
        newEntity.setReason("High Value Transaction");

        // --- Act ---
        AnomalousTransactionEntity savedEntity = repository.save(newEntity);
        Optional<AnomalousTransactionEntity> foundEntity = repository.findById(savedEntity.getId());

        // --- Assert ---
        assertNotNull(savedEntity.getId(), "Saved entity should have a non-null ID");
        assertTrue(foundEntity.isPresent(), "Entity should be found in the database by its ID");
        assertEquals("tx-jpa-test", foundEntity.get().getTransactionId());
    }
}