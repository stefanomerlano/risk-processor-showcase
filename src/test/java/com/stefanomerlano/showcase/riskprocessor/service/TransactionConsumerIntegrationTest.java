package com.stefanomerlano.showcase.riskprocessor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.stefanomerlano.showcase.riskprocessor.dto.TransactionDto;
import com.stefanomerlano.showcase.riskprocessor.persistence.entity.AnomalousTransactionEntity;
import com.stefanomerlano.showcase.riskprocessor.persistence.repository.AnomalousTransactionRepository;
import com.stefanomerlano.showcase.riskprocessor.search.repository.AnomalousTransactionSearchRepository;

/**
 * Integration test for the TransactionConsumer focusing on database persistence.
 * This test uses Testcontainers to spin up a real PostgreSQL database.
 */
@SpringBootTest
@Testcontainers // Enables Testcontainers support for JUnit 5
class TransactionConsumerIntegrationTest {

    // This will start a PostgreSQL Docker container before tests run
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @MockBean
    private AnomalousTransactionSearchRepository searchRepositoryMock;

    // This method dynamically overrides the application properties to point to the test database
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        // We can disable Kafka for this specific test as we are calling the consumer directly
        registry.add("spring.kafka.consumer.enabled", () -> "false"); 
    }

    @Autowired
    private TransactionConsumer transactionConsumer;

    @Autowired
    private AnomalousTransactionRepository repository;

    @Test
    void whenAnomalousTransactionIsConsumed_thenItIsPersistedInDatabase() {
        // --- Arrange ---
        TransactionDto anomalousTransaction = new TransactionDto(
            "tx-integration-test",
            new BigDecimal("25000.00"),
            "CHF",
            Instant.now(),
            "test-orig",
            "test-bene"
        );

        // --- Act ---
        // We call the consumer method directly, simulating a message being received from Kafka
        transactionConsumer.consumeTransaction(anomalousTransaction);

        // --- Assert ---
        // Verify directly against the database
        List<AnomalousTransactionEntity> results = repository.findAll();
        assertFalse(results.isEmpty(), "Database should not be empty after saving an anomaly");
        assertEquals(1, results.size(), "There should be exactly one anomaly in the database");

        AnomalousTransactionEntity savedEntity = results.get(0);
        assertEquals("tx-integration-test", savedEntity.getTransactionId());
        assertEquals("High Value Transaction", savedEntity.getReason());
    }
}