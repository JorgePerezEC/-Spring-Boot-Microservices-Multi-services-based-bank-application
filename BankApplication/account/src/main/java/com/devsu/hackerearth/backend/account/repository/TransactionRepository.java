package com.devsu.hackerearth.backend.account.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devsu.hackerearth.backend.account.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountId(Long accountId);

    List<Transaction> findByAccountIdOrderByDateDesc(Long accountId);

    Optional<Transaction> findTopByAccountIdOrderByDateDesc(Long accountId);

    List<Transaction> findByType(String type);

    List<Transaction> findByDateBetween(Date startDate, Date endDate);

    List<Transaction> findByAccountIdAndDateBetween(Long accountId, Date startDate, Date endDate);

    @Query("SELECT t FROM Transaction t WHERE t.accountId IN " +
            "(SELECT a.id FROM Account a WHERE a.clientId = :clientId) " +
            "AND t.date BETWEEN :startDate AND :endDate " +
            "ORDER BY t.date DESC")
    List<Transaction> findByClientIdAndDateBetween(@Param("clientId") Long clientId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    @Query("SELECT t FROM Transaction t " +
            "JOIN Account a ON t.accountId = a.id " +
            "WHERE a.clientId = :clientId " +
            "AND t.date BETWEEN :startDate AND :endDate " +
            "ORDER BY t.date DESC")
    List<Transaction> findTransactionsForBankStatement(@Param("clientId") Long clientId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
}
