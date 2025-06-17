package org.romanzhula.transaction_service.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.romanzhula.transaction_service.configurations.UserServiceClient;
import org.romanzhula.transaction_service.dto.TransactionRequest;
import org.romanzhula.transaction_service.dto.TransactionResponse;
import org.romanzhula.transaction_service.dto.events.NotificationRequestDTO;
import org.romanzhula.transaction_service.models.Transaction;
import org.romanzhula.transaction_service.repositories.TransactionRepository;
import org.romanzhula.transaction_service.services.TransactionService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    @Value("${spring.rabbitmq.exchange.notification.name}")
    private String notificationExchange;

    @Value("${spring.rabbitmq.routing-key.notification}")
    private String notificationRoutingKey;


    private final UserServiceClient userServiceClient;
    private final TransactionRepository transactionRepository;
    private final RabbitTemplate rabbitTemplate;

    // Virtual Threads Executor for rabbitmq messages
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();


    @Override
    @Transactional
    public ResponseEntity<TransactionResponse> createTransaction(TransactionRequest request) {
        String googleId = request.getGoogleId();
        String githubId = request.getGithubId();

        boolean isPassphraseValid = userServiceClient.verifyPassphrase(googleId, githubId, request.getPassphrase());

        if (!isPassphraseValid) {
            return ResponseEntity.status(403).body(
                    TransactionResponse.builder()
                            .userId(null)
                            .amount(null)
                            .currency(null)
                            .createdAt(null)
                            .build()
                    )
            ;
        }

        Long userId = getUserIdFromUserService(googleId, githubId);

        Transaction transaction = Transaction.builder()
                .userId(userId)
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .build()
        ;

        transactionRepository.save(transaction);


        NotificationRequestDTO notificationRequestDTO = NotificationRequestDTO.builder()
                .userId(String.valueOf(transaction.getUserId()))
                .message("Your transaction amount is: " + transaction.getAmount() +
                        " " + transaction.getCurrency() + " done successfully!"
                )
                .build()
        ;

        executorService.submit(() -> {
            rabbitTemplate.convertAndSend(
                    notificationExchange,
                    notificationRoutingKey,
                    notificationRequestDTO
            );
        });


        return ResponseEntity.ok(
                TransactionResponse.builder()
                        .id(transaction.getId())
                        .userId(transaction.getUserId().toString())
                        .amount(transaction.getAmount())
                        .currency(transaction.getCurrency())
                        .createdAt(transaction.getCreatedAt())
                        .build()
        );
    }

    private Long getUserIdFromUserService(String googleId, String githubId) {
        try {
            if (googleId != null) {
                return userServiceClient.getUserIdByGoogleId(googleId);
            } else if (githubId != null) {
                return userServiceClient.getUserIdByGithubId(githubId);
            }
        } catch (Exception exception) {
            throw new EntityNotFoundException("User not found in user-service");
        }

        throw new IllegalArgumentException("Unable to fetch user from user-service");
    }

}
