package com.stefanomerlano.showcase.riskprocessor.service;

import com.stefanomerlano.showcase.riskprocessor.dto.TransactionDto;
import com.stefanomerlano.showcase.riskprocessor.model.AnalysisResult;
import com.stefanomerlano.showcase.riskprocessor.model.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the RiskAnalysisServiceImpl.
 * These tests run in isolation without needing a Spring context.
 */
@ExtendWith(MockitoExtension.class) // Enables Mockito annotations
class RiskAnalysisServiceImplTest {

    @Mock // Creates a mock (fake) implementation of RulesService
    private RulesService rulesService;

    @InjectMocks // Creates an instance of RiskAnalysisServiceImpl and injects the mocks into it
    private RiskAnalysisServiceImpl riskAnalysisService;

    private TransactionDto cleanTransaction;
    private TransactionDto anomalousTransaction;

    @BeforeEach // This method runs before each test
    void setUp() {
        // Common setup for our tests
        cleanTransaction = new TransactionDto("tx-clean", new BigDecimal("500.00"), "EUR", Instant.now(), "orig", "bene");
        anomalousTransaction = new TransactionDto("tx-anomalous", new BigDecimal("15000.00"), "EUR", Instant.now(), "orig", "bene");
    }

    @Test
    void givenTransactionAmountBelowThreshold_whenAnalyze_thenResultIsClean() {
        // --- Arrange ---
        // Define the behavior of our mock: when getActiveRules() is called, return a standard rule.
        Rule highValueRule = new Rule("High Value Transaction", new BigDecimal("10000.00"));
        when(rulesService.getActiveRules()).thenReturn(List.of(highValueRule));

        // --- Act ---
        // Call the method we want to test
        AnalysisResult result = riskAnalysisService.analyze(cleanTransaction);

        // --- Assert ---
        // Verify that the outcome is what we expect
        assertFalse(result.isAnomalous(), "Transaction should be clean");
        assertNull(result.reason(), "Reason should be null for clean transactions");
    }

    @Test
    void givenTransactionAmountAboveThreshold_whenAnalyze_thenResultIsAnomalous() {
        // --- Arrange ---
        Rule highValueRule = new Rule("High Value Transaction", new BigDecimal("10000.00"));
        when(rulesService.getActiveRules()).thenReturn(List.of(highValueRule));

        // --- Act ---
        AnalysisResult result = riskAnalysisService.analyze(anomalousTransaction);

        // --- Assert ---
        assertTrue(result.isAnomalous(), "Transaction should be anomalous");
        assertEquals("High Value Transaction", result.reason(), "The reason should match the triggered rule's name");
    }
}