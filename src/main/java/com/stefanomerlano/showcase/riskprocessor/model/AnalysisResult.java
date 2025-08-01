package com.stefanomerlano.showcase.riskprocessor.model;

/**
 * Represents the outcome of a risk analysis.
 * 
 * @param isAnomalous True if the transaction is considered anomalous, false
 *                    otherwise.
 * @param reason      A description of why the transaction is anomalous (e.g.,
 *                    the name of the rule triggered).
 */
public record AnalysisResult(boolean isAnomalous, String reason) {
    // A handy factory method for clean results
    public static AnalysisResult clean() {
        return new AnalysisResult(false, null);
    }
}