package com.stefanomerlano.showcase.riskprocessor.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Represents a financial transaction data transfer object.
 * This record is used to carry data between processes.
 *
 * @param transactionId A unique identifier for the transaction.
 * @param amount        The monetary value of the transaction.
 * @param currency      The ISO 4217 currency code (e.g., "EUR", "USD").
 * @param timestamp     The exact moment when the transaction occurred.
 * @param originator    The account identifier of the party initiating the
 *                      transaction.
 * @param beneficiary   The account identifier of the recipient party.
 */
public record TransactionDto(
        String transactionId,
        BigDecimal amount,
        String currency,
        Instant timestamp,
        String originator,
        String beneficiary) {
}