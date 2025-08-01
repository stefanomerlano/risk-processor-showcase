package com.stefanomerlano.showcase.riskprocessor.service;

import com.stefanomerlano.showcase.riskprocessor.dto.TransactionDto;

public interface TransactionService {
    void processTransaction(TransactionDto transaction);
}