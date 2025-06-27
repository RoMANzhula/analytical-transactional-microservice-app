package org.romanzhula.transaction_service.repositories;


import org.romanzhula.transaction_service.models.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByUserId(Long userId, Pageable pageable);

    Page<Transaction> findByGoogleId(String googleId, Pageable pageable);

    Page<Transaction> findByGithubId(String githubId, Pageable pageable);

}
