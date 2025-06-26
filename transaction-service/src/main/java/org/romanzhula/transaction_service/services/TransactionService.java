package org.romanzhula.transaction_service.services;

import org.romanzhula.transaction_service.dto.TransactionRequest;
import org.romanzhula.transaction_service.dto.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface TransactionService {

    ResponseEntity<TransactionResponse> createTransaction(TransactionRequest request);

    Page<TransactionResponse> getTransactionsByUserId(Long userId, Pageable pageable);

}
