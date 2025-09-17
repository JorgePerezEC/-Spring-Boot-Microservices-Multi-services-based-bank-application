package com.devsu.hackerearth.backend.account.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsu.hackerearth.backend.account.exception.AccountNotFoundException;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> getAll() {
        return accountRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDto getById(Long id) {
        Optional<Account> accountOpt = accountRepository.findById(id);
        return accountOpt.map(this::convertToDto).orElse(null);
    }

    @Override
    public AccountDto create(AccountDto accountDto) {
        // Validate account number doesn't exist
        if (accountRepository.existsByNumber(accountDto.getNumber())) {
            throw new RuntimeException("Account number already exists: " + accountDto.getNumber());
        }

        Account account = convertToEntity(accountDto);
        account.setActive(true); // New accounts are active by default
        Account savedAccount = accountRepository.save(account);
        return convertToDto(savedAccount);
    }

    @Override
    public AccountDto update(AccountDto accountDto) {
        Optional<Account> accountOpt = accountRepository.findById(accountDto.getId());
        if (!accountOpt.isPresent()) {
            return null;
        }

        Account existingAccount = accountOpt.get();

        // Check if account number is being changed and if it already exists
        if (!existingAccount.getNumber().equals(accountDto.getNumber()) &&
                accountRepository.existsByNumber(accountDto.getNumber())) {
            throw new RuntimeException("Account number already exists: " + accountDto.getNumber());
        }

        // Update fields
        existingAccount.setNumber(accountDto.getNumber());
        existingAccount.setType(accountDto.getType());
        existingAccount.setInitialAmount(accountDto.getInitialAmount());
        existingAccount.setActive(accountDto.isActive());
        existingAccount.setClientId(accountDto.getClientId());

        Account savedAccount = accountRepository.save(existingAccount);
        return convertToDto(savedAccount);
    }

    @Override
    public AccountDto partialUpdate(Long id, PartialAccountDto partialAccountDto) {
        Optional<Account> accountOpt = accountRepository.findById(id);
        if (!accountOpt.isPresent()) {
            return null;
        }

        Account existingAccount = accountOpt.get();

        // Update only provided fields
        if (partialAccountDto.getIsActive() != null) {
            existingAccount.setActive(partialAccountDto.getIsActive());
        }
        if (partialAccountDto.getType() != null) {
            existingAccount.setType(partialAccountDto.getType());
        }

        Account savedAccount = accountRepository.save(existingAccount);
        return convertToDto(savedAccount);
    }

    @Override
    public void deleteById(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new AccountNotFoundException(id);
        }
        accountRepository.deleteById(id);
    }

    // Conversion methods
    private AccountDto convertToDto(Account account) {
        return new AccountDto(
                account.getId(),
                account.getNumber(),
                account.getType(),
                account.getInitialAmount(),
                account.isActive(),
                account.getClientId());
    }

    private Account convertToEntity(AccountDto accountDto) {
        return new Account(
                accountDto.getNumber(),
                accountDto.getType(),
                accountDto.getInitialAmount(),
                accountDto.isActive(),
                accountDto.getClientId());
    }
}