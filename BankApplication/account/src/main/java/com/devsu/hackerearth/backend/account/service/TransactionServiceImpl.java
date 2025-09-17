package com.devsu.hackerearth.backend.account.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsu.hackerearth.backend.account.exception.AccountNotFoundException;
import com.devsu.hackerearth.backend.account.exception.InsufficientBalanceException;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.Transaction;
import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;
import com.devsu.hackerearth.backend.account.model.dto.BankStatementReportDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;
import com.devsu.hackerearth.backend.account.repository.TransactionRepository;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final ClientService clientService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository,
            ClientService clientService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.clientService = clientService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDto> getAll() {
        // Get all transactions
        return transactionRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDto getById(Long id) {
        // Get transactions by id
        Optional<Transaction> transactionOpt = transactionRepository.findById(id);
        return transactionOpt.map(this::convertToDto).orElse(null);
    }

    @Override
    public TransactionDto create(TransactionDto transactionDto) {
        // Validate account exists
        Account account = accountRepository.findById(transactionDto.getAccountId())
                .orElseThrow(
                        () -> AccountNotFoundException.byAccountNumber("Account ID: " + transactionDto.getAccountId()));

        // Calculate new balance
        Double currentBalance = getCurrentBalance(transactionDto.getAccountId());
        Double newBalance = calculateNewBalance(currentBalance, transactionDto.getType(), transactionDto.getAmount());

        // Validate sufficient balance for withdrawals
        if ("RETIRO".equals(transactionDto.getType()) && newBalance < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Current: " + currentBalance + ", Requested: " + transactionDto.getAmount());
        }

        // Create transaction
        Transaction transaction = convertToEntity(transactionDto);
        transaction.setBalance(newBalance);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToDto(savedTransaction);
    }

    // Helper methods
    private Double getCurrentBalance(Long accountId) {
        Transaction lastTransaction = transactionRepository.findTopByAccountIdOrderByDateDesc(accountId)
                .orElse(null);

        if (lastTransaction != null) {
            return lastTransaction.getBalance();
        } else {
            // If no transactions, get initial amount from account
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new AccountNotFoundException(accountId));
            return account.getInitialAmount();
        }
    }

    private Double calculateNewBalance(Double currentBalance, String transactionType, Double amount) {
        if ("DEPOSITO".equals(transactionType)) {
            return currentBalance + amount;
        } else if ("RETIRO".equals(transactionType)) {
            return currentBalance - amount;
        } else {
            throw new IllegalArgumentException("Invalid transaction type: " + transactionType);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankStatementDto> getAllByAccountClientIdAndDateBetween(Long clientId, Date dateTransactionStart,
            Date dateTransactionEnd) {
        List<Transaction> transactions = transactionRepository.findByClientIdAndDateBetween(
                clientId, dateTransactionStart, dateTransactionEnd);

        return transactions.stream()
                .map(this::convertToBankStatementDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDto getLastByAccountId(Long accountId) {
        Transaction transaction = transactionRepository.findTopByAccountIdOrderByDateDesc(accountId)
                .orElse(null);
        return transaction != null ? convertToDto(transaction) : null;
    }

    @Override
    @Transactional(readOnly = true)
    public BankStatementReportDto getBankStatementReport(Long clientId, Date dateTransactionStart,
            Date dateTransactionEnd) {
        // Get client information
        BankStatementReportDto.ClientInfoDto clientInfo = clientService.getClientById(clientId);

        // Get all transactions for the client in the date range
        List<Transaction> transactions = transactionRepository.findByClientIdAndDateBetween(
                clientId, dateTransactionStart, dateTransactionEnd);

        // Group transactions by account
        Map<Long, List<Transaction>> transactionsByAccount = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getAccountId));

        // Create account statements
        List<BankStatementReportDto.AccountStatementDto> accountStatements = transactionsByAccount.entrySet().stream()
                .map(entry -> {
                    Long accountId = entry.getKey();
                    List<Transaction> accountTransactions = entry.getValue();

                    // Get account information
                    Account account = accountRepository.findById(accountId)
                            .orElseThrow(() -> new AccountNotFoundException(accountId));

                    // Convert transactions to DTOs
                    List<BankStatementReportDto.TransactionDetailDto> transactionDetails = accountTransactions.stream()
                            .map(transaction -> new BankStatementReportDto.TransactionDetailDto(
                                    transaction.getDate(),
                                    transaction.getType(),
                                    transaction.getAmount(),
                                    transaction.getBalance()))
                            .collect(Collectors.toList());

                    // Create account statement
                    return new BankStatementReportDto.AccountStatementDto(
                            account.getNumber(),
                            account.getType(),
                            account.getInitialAmount(),
                            getCurrentBalance(accountId),
                            account.isActive(),
                            transactionDetails);
                })
                .collect(Collectors.toList());

        return new BankStatementReportDto(clientInfo, accountStatements);
    }

    // Conversion methods
    private TransactionDto convertToDto(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getDate(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getBalance(),
                transaction.getAccountId());
    }

    private Transaction convertToEntity(TransactionDto transactionDto) {
        return new Transaction(
                transactionDto.getDate(),
                transactionDto.getType(),
                transactionDto.getAmount(),
                0.0,
                transactionDto.getAccountId());
    }

    private BankStatementDto convertToBankStatementDto(Transaction transaction) {
        // Get account information
        Account account = accountRepository.findById(transaction.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException(transaction.getAccountId()));

        BankStatementDto dto = new BankStatementDto();
        dto.setDate(transaction.getDate());
        dto.setTransactionType(transaction.getType());
        dto.setAmount(transaction.getAmount());
        dto.setBalance(transaction.getBalance());

        // Add account information
        dto.setAccountNumber(account.getNumber());
        dto.setAccountType(account.getType());
        dto.setInitialAmount(account.getInitialAmount());
        dto.setIsActive(account.isActive());

        // Get client
        dto.setClient("Client ID: " + account.getClientId());

        return dto;
    }

}
