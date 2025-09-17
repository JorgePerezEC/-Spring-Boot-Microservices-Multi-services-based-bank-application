package com.devsu.hackerearth.backend.account.controller;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;
import com.devsu.hackerearth.backend.account.model.dto.BankStatementReportDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

	private final TransactionService transactionService;

	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@GetMapping
	public ResponseEntity<List<TransactionDto>> getAll() {
		// api/transactions
		// Get all transactions
		List<TransactionDto> transactions = transactionService.getAll();
		return ResponseEntity.ok(transactions);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TransactionDto> get(@PathVariable Long id) {
		// api/transactions/{id}
		// Get transactions by id
		TransactionDto transaction = transactionService.getById(id);
		return transaction != null ? ResponseEntity.ok(transaction) : ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<TransactionDto> create(@RequestBody TransactionDto transactionDto) {
		// api/transactions
		// Create transactions
		TransactionDto createdTransaction = transactionService.create(transactionDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
	}

	@GetMapping("/clients/{clientId}/report")
	public ResponseEntity<List<BankStatementDto>> report(
			@PathVariable Long clientId,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTransactionStart,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTransactionEnd) {
		// api/transactions/clients/{clientId}/report?dateTransactionStart=2025-01-01&dateTransactionEnd=2025-12-31
		// Get bank statement report (legacy format for compatibility)
		List<BankStatementDto> bankStatement = transactionService.getAllByAccountClientIdAndDateBetween(
				clientId, dateTransactionStart, dateTransactionEnd);
		return ResponseEntity.ok(bankStatement);
	}

	// @GetMapping("/clients/{clientId}/report-structured")
	// public ResponseEntity<BankStatementReportDto> report(
	// @PathVariable Long clientId,
	// @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date
	// dateTransactionStart,
	// @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date
	// dateTransactionEnd) {
	// //
	// api/transactions/clients/{clientId}/report?dateTransactionStart=2025-01-01&dateTransactionEnd=2025-12-31
	// // Get structured bank statement report
	// BankStatementReportDto bankStatement =
	// transactionService.getBankStatementReport(
	// clientId, dateTransactionStart, dateTransactionEnd);
	// return ResponseEntity.ok(bankStatement);
	// }
}
