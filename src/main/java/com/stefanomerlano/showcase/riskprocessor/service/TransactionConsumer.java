package com.stefanomerlano.showcase.riskprocessor.service;

import com.stefanomerlano.showcase.riskprocessor.dto.TransactionDto;
import com.stefanomerlano.showcase.riskprocessor.model.AnalysisResult;
import com.stefanomerlano.showcase.riskprocessor.persistence.entity.AnomalousTransactionEntity;
import com.stefanomerlano.showcase.riskprocessor.persistence.repository.AnomalousTransactionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionConsumer {

    private static final Logger logger = LoggerFactory.getLogger(TransactionConsumer.class);
    private final RiskAnalysisService riskAnalysisService;
    private final AnomalousTransactionRepository repository;

    @Autowired
    public TransactionConsumer(RiskAnalysisService riskAnalysisService, AnomalousTransactionRepository repository) {
        this.riskAnalysisService = riskAnalysisService;
        this.repository = repository;
    }

    @KafkaListener(topics = "transactions-topic", groupId = "risk-processor-group")
    public void consumeTransaction(TransactionDto transaction) {
        logger.info("--> Consumed transaction from Kafka: {}", transaction.transactionId());

        AnalysisResult result = riskAnalysisService.analyze(transaction);

        if (result.isAnomalous()) {
            logger.warn("--> ANOMALY DETECTED! Reason: [{}]. Transaction: {}", result.reason(), transaction);
            saveAnomalousTransaction(transaction, result.reason());
        } else {
            logger.info("--> Transaction {} analyzed: CLEAN", transaction.transactionId());
        }
    }

    private void saveAnomalousTransaction(TransactionDto dto, String reason) {
        AnomalousTransactionEntity entity = new AnomalousTransactionEntity();
        // Map fields from DTO to Entity (getters and setters)
        entity.setTransactionId(dto.transactionId());
        entity.setAmount(dto.amount());
        entity.setCurrency(dto.currency());
        entity.setTimestamp(dto.timestamp());
        entity.setOriginator(dto.originator());
        entity.setBeneficiary(dto.beneficiary());
        entity.setReason(reason);

        repository.save(entity);
        logger.info("--> Successfully saved anomalous transaction {}", entity.getTransactionId());
    }
}