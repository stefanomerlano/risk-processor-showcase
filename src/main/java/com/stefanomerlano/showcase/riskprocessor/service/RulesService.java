package com.stefanomerlano.showcase.riskprocessor.service;

import java.util.List;

import com.stefanomerlano.showcase.riskprocessor.model.Rule;

public interface RulesService {

    /**
     * Provides a static list of active rules.
     * 
     * @return A list of rules to be applied.
     */
    List<Rule> getActiveRules();
}
