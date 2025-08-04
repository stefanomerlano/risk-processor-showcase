package com.stefanomerlano.showcase.riskprocessor.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stefanomerlano.showcase.riskprocessor.dto.TransactionDto;
import com.stefanomerlano.showcase.riskprocessor.model.AnalysisResult;
import com.stefanomerlano.showcase.riskprocessor.model.Rule;

/**
 * This service contains the core logic for analyzing transaction risks.
 */
@Service
public class RiskAnalysisServiceImpl implements RiskAnalysisService {

    private final RulesService rulesService;


    public RiskAnalysisServiceImpl(RulesService rulesService) {
        this.rulesService = rulesService;
    }

    /**
     * Analyzes a single transaction against a set of rules.
     * 
     * @param transaction The transaction to analyze.
     * @return An AnalysisResult indicating if the transaction is anomalous and why.
     */
    public AnalysisResult analyze(TransactionDto transaction) {
        List<Rule> activeRules = rulesService.getActiveRules();

        for (Rule rule : activeRules) {
            // Simple rule: check if the transaction amount exceeds the rule's threshold.
            if (transaction.amount().compareTo(rule.threshold()) > 0) {
                return new AnalysisResult(true, rule.ruleName());
            }
        }

        // If no rules were triggered, the transaction is considered clean.
        return AnalysisResult.clean();
    }
}