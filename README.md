# [Spring Boot Microservices] Multi-services based bank application

Aplicación bancaria desarrollada con arquitectura de microservicios usando Spring Boot.

## Estructura del Proyecto

```
BankApplication/
├── client/          # Microservicio de Clientes
├── account/         # Microservicio de Cuentas y Transacciones
└── api-collections/ # Colecciones Postman para pruebas
```

## Funcionalidades Implementadas

### F1: Operaciones CRUD

- **Clientes**: `/api/clients` - Crear, consultar, actualizar y eliminar
- **Cuentas**: `/api/accounts` - Gestión completa de cuentas bancarias
- **Transacciones**: `/api/transactions` - Registro de movimientos

### F2: Gestión de Transacciones

- Depósitos y retiros con actualización automática de saldos
- Validación de fondos disponibles
- Historial completo de movimientos

### F3: Control de Saldos

- Validación "Saldo no disponible" para retiros sin fondos
- Manejo de errores personalizado

### F4: Reportes de Estado de Cuenta

- Endpoint: `/api/transactions/clients/{clientId}/report`
- Filtrado por rango de fechas
- Información completa del cliente y movimientos por cuenta

### F5: Pruebas Unitarias

- Tests para lógica de negocio con mocks
- Cobertura de servicios principales

### F6: Pruebas de Integración

- Tests end-to-end con contexto Spring completo
- Validación de comunicación entre capas

## Tecnologías

- **Backend**: Spring Boot 2.4.2, Spring Data JPA
- **Base de Datos**: H2 (en memoria para desarrollo)
- **Testing**: JUnit 5, Mockito
- **Documentación**: Javadoc, comentarios técnicos

## Ejecución

### Requisitos

- Java 11
- Maven 3.6+

### Comandos

```bash
# Microservicio Cliente (Puerto 8001)
cd client
mvn spring-boot:run

# Microservicio Account (Puerto 8000)
cd account
mvn spring-boot:run

# Ejecutar tests
mvn test
```

## Pruebas con Postman

Importar la colección `collection_bank_postman.json` que incluye:

- Todos los endpoints configurados
- Variables de entorno
- Tests automatizados
- Casos de error

## Autor

**George Perez**  
Desarrollador Backend - Sistema Bancario  
Septiembre 2025
