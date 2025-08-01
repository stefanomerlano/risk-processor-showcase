package com.stefanomerlano.showcase.riskprocessor.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.stefanomerlano.showcase.riskprocessor.model.Rule;

@Service
public class RulesServiceImpl implements RulesService {

    @Override
    public List<Rule> getActiveRules() {
        // For this showcase, we return a hardcoded list of rules.
        return List.of(
                new Rule("High Value Transaction", new BigDecimal("10000.00")));
    }

}
