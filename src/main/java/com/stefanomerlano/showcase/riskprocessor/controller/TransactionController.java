package com.stefanomerlano.showcase.riskprocessor.controller;

import com.stefanomerlano.showcase.riskprocessor.dto.TransactionDto;
import com.stefanomerlano.showcase.riskprocessor.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    // Inject the service layer
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<String> ingestTransaction(@RequestBody TransactionDto transaction) {
        // Delegate the processing to the service layer
        transactionService.processTransaction(transaction);
        return ResponseEntity.accepted()
                .body("Transaction queued for processing with ID: " + transaction.transactionId());
    }
}