package org.romanzhula.transaction_service.services.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.romanzhula.transaction_service.configurations.UserServiceClient;
import org.romanzhula.transaction_service.dto.TransactionRequest;
import org.romanzhula.transaction_service.dto.TransactionResponse;
import org.romanzhula.transaction_service.dto.events.NotificationRequestDTO;
import org.romanzhula.transaction_service.dto.events.TransactionEventDto;
import org.romanzhula.transaction_service.models.Transaction;
import org.romanzhula.transaction_service.repositories.TransactionRepository;
import org.romanzhula.transaction_service.services.TransactionService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Value("${spring.rabbitmq.exchange.analytics.name}")
    private String analyticsExchange;

    @Value("${spring.rabbitmq.routing-key.analytics}")
    private String analyticsRoutingKey;


    private final UserServiceClient userServiceClient;
    private final TransactionRepository transactionRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ModelMapper modelMapper;

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
                .googleId(googleId)
                .githubId(githubId)
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


        TransactionEventDto transactionEventDto = TransactionEventDto.builder()
                .userId(String.valueOf(transaction.getUserId()))
                .eventType("transaction")
                .eventTime(transaction.getCreatedAt())
                .build();

        executorService.submit(() -> {
            rabbitTemplate.convertAndSend(
                    analyticsExchange,
                    analyticsRoutingKey,
                    transactionEventDto
            );
        });


        return ResponseEntity.ok(
                TransactionResponse.builder()
                        .id(transaction.getId())
                        .userId(transaction.getUserId().toString())
                        .amount(transaction.getAmount())
                        .currency(transaction.getCurrency())
                        .googleId(googleId)
                        .githubId(githubId)
                        .createdAt(transaction.getCreatedAt())
                        .build()
        );
    }


    @Override
    @Transactional(readOnly = true)
    public Page<TransactionResponse> getTransactionsByUserId(Long userId, Pageable pageable) {
        return transactionRepository.findByUserId(userId, pageable)
                .map(transaction -> modelMapper.map(transaction, TransactionResponse.class))
        ;
    }


    @Override
    public Page<TransactionResponse> getTransactionsByGoogleId(String googleId, Pageable pageable) {
        return transactionRepository.findByGoogleId(googleId, pageable)
                .map(transaction -> modelMapper.map(transaction, TransactionResponse.class))
        ;
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
