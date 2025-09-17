package com.devsu.hackerearth.backend.account.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devsu.hackerearth.backend.account.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByNumber(String number);

    List<Account> findByClientId(Long clientId);

    List<Account> findByClientIdAndIsActive(Long clientId, boolean isActive);

    List<Account> findByIsActive(boolean isActive);

    @Query("SELECT a FROM Account a WHERE a.clientId = :clientId AND a.isActive = true")
    List<Account> findActiveAccountsByClientId(@Param("clientId") Long clientId);

    @Query("SELECT a FROM Account a WHERE a.type = :type")
    List<Account> findByType(@Param("type") String type);

    boolean existsByNumber(String number);
}
