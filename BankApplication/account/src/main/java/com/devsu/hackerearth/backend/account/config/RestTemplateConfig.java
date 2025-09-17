package com.devsu.hackerearth.backend.account.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración de Spring para RestTemplate
 * 
 * Esta clase define la configuración necesaria para habilitar la comunicación
 * HTTP entre microservicios en la arquitectura del sistema bancario.
 * 
 * Propósito principal:
 * - Permite al microservicio Account comunicarse con el microservicio Client
 * - Facilita las llamadas HTTP para obtener información completa del cliente
 * - Utilizado específicamente en el reporte F4 (Estado de cuenta)
 * 
 * Uso:
 * - Se inyecta automáticamente en ClientServiceImpl
 * - Permite obtener datos del cliente desde Account microservice
 * - Facilita la generación de reportes consolidados
 * 
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
