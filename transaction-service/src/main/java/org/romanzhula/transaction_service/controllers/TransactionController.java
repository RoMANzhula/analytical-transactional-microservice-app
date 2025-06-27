package org.romanzhula.transaction_service.controllers;


import lombok.RequiredArgsConstructor;
import org.romanzhula.transaction_service.dto.TransactionRequest;
import org.romanzhula.transaction_service.dto.TransactionResponse;
import org.romanzhula.transaction_service.services.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping("/by-user-id")
    public Page<TransactionResponse> getByUserId(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return transactionService.getTransactionsByUserId(userId, PageRequest.of(page, size));
    }

    @GetMapping("/by-google-id")
    public Page<TransactionResponse> getByGoogleId(
            @RequestParam String googleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return transactionService.getTransactionsByGoogleId(googleId, PageRequest.of(page, size));
    }

    @GetMapping("/by-github-id")
    public Page<TransactionResponse> getByGithubId(
            @RequestParam String githubId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return transactionService.getTransactionsByGithubId(githubId, PageRequest.of(page, size));
    }

}
