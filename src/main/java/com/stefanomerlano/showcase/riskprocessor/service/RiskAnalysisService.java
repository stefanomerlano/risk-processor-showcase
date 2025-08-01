package com.stefanomerlano.showcase.riskprocessor.service;

import com.stefanomerlano.showcase.riskprocessor.dto.TransactionDto;
import com.stefanomerlano.showcase.riskprocessor.model.AnalysisResult;

public interface RiskAnalysisService {

    /**
     * Analyzes a single transaction against a set of rules.
     * 
     * @param transaction The transaction to analyze.
     * @return An AnalysisResult indicating if the transaction is anomalous and why.
     */
    AnalysisResult analyze(TransactionDto transaction);

}
