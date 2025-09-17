package com.devsu.hackerearth.backend.account.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankStatementReportDto {

    private ClientInfoDto client;
    private List<AccountStatementDto> accounts;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ClientInfoDto {
        private Long clientId;
        private String clientName;
        private String dni;
        private String phone;
        private String address;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AccountStatementDto {
        private String accountNumber;
        private String accountType;
        private Double initialAmount;
        private Double currentBalance;
        private Boolean isActive;
        private List<TransactionDetailDto> transactions;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TransactionDetailDto {
        private java.util.Date date;
        private String type;
        private Double amount;
        private Double balance;
    }
}
