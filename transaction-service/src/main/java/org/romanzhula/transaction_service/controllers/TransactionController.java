package org.romanzhula.transaction_service.controllers;


import lombok.RequiredArgsConstructor;
import org.romanzhula.transaction_service.dto.TransactionRequest;
import org.romanzhula.transaction_service.dto.TransactionResponse;
import org.romanzhula.transaction_service.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @RequestBody TransactionRequest request
    ) {
        return transactionService.createTransaction(request);
    }

}
