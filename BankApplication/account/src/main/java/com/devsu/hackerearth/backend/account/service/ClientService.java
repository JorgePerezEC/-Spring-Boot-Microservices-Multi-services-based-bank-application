package com.devsu.hackerearth.backend.account.service;

import com.devsu.hackerearth.backend.account.model.dto.BankStatementReportDto.ClientInfoDto;

public interface ClientService {
    ClientInfoDto getClientById(Long clientId);
}
