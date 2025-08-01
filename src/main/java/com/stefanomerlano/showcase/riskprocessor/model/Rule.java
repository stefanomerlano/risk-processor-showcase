package com.stefanomerlano.showcase.riskprocessor.model;

import java.math.BigDecimal;

/**
 * Represents a single validation rule.
 * 
 * @param ruleName  The name of the rule (e.g., "High Value Transaction").
 * @param threshold The value to compare against.
 */
public record Rule(String ruleName, BigDecimal threshold) {
}