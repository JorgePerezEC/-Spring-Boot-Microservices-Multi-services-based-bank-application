package com.devsu.hackerearth.backend.account.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import lombok.Data;

import com.devsu.hackerearth.backend.account.model.dto.BankStatementReportDto.ClientInfoDto;

@Service
public class ClientServiceImpl implements ClientService {

    private final RestTemplate restTemplate;

    @Value("${client.service.url:http://localhost:8001}")
    private String clientServiceUrl;

    public ClientServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ClientInfoDto getClientById(Long clientId) {
        try {
            String url = clientServiceUrl + "/api/clients/" + clientId;
            ClientResponse response = restTemplate.getForObject(url, ClientResponse.class);

            if (response != null) {
                return new ClientInfoDto(
                        response.getId(),
                        response.getName(),
                        response.getDni(),
                        response.getPhone(),
                        response.getAddress());
            }
        } catch (RestClientException e) {
            // Log error and return fallback
            System.err.println("Error calling client service: " + e.getMessage());
        }

        // Fallback when client service is not available
        return new ClientInfoDto(
                clientId,
                "Cliente no disponible",
                "N/A",
                "N/A",
                "N/A");
    }

    // Internal DTO for client service response
    @Data
    private static class ClientResponse {
        private Long id;
        private String name;
        private String dni;
        private String phone;
        private String address;
    }
}
