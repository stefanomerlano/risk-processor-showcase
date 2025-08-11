package com.stefanomerlano.showcase.riskprocessor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stefanomerlano.showcase.riskprocessor.dto.TransactionDto;
import com.stefanomerlano.showcase.riskprocessor.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    // Inject the service layer
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Ingest a new transaction for asynchronous processing")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Transaction has been accepted and queued for processing."),
        @ApiResponse(responseCode = "400", description = "Invalid input data provided.")
    })
    @PostMapping
    public ResponseEntity<String> ingestTransaction(@RequestBody TransactionDto transaction) {
        // Delegate the processing to the service layer
        transactionService.processTransaction(transaction);
        return ResponseEntity.accepted()
                .body("Transaction queued for processing with ID: " + transaction.transactionId());
    }
}