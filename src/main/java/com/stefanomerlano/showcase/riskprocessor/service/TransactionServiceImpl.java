package com.stefanomerlano.showcase.riskprocessor.service;

import com.stefanomerlano.showcase.riskprocessor.dto.TransactionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service // Marks this class as a Spring service bean
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    private static final String TOPIC_NAME = "transactions-topic";

    @Autowired // Spring will inject the configured KafkaTemplate
    private KafkaTemplate<String, TransactionDto> kafkaTemplate;

    @Override
    public void processTransaction(TransactionDto transaction) {
        logger.info("Publishing transaction to Kafka topic: {}", TOPIC_NAME);
        // The send method is asynchronous. It returns a CompletableFuture.
        kafkaTemplate.send(TOPIC_NAME, transaction.transactionId(), transaction);
    }
}