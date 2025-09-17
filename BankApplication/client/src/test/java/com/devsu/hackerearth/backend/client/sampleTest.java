package com.devsu.hackerearth.backend.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.devsu.hackerearth.backend.client.controller.ClientController;
import com.devsu.hackerearth.backend.client.exception.ClientNotFoundException;
import com.devsu.hackerearth.backend.client.exception.DuplicateDniException;
import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.repository.ClientRepository;
import com.devsu.hackerearth.backend.client.service.ClientService;
import com.devsu.hackerearth.backend.client.service.ClientServiceImpl;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class sampleTest {

    // Variables para unit tests
    private ClientService clientService = mock(ClientService.class);
    private ClientController clientController = new ClientController(clientService);
    private ClientRepository clientRepository = mock(ClientRepository.class);
    private ClientServiceImpl clientServiceImpl = new ClientServiceImpl(clientRepository);

    private ClientDto testClientDto;
    private Client testClient;

    // Variables para prueba de integracion
    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        testClientDto = new ClientDto();
        testClientDto.setId(1L);
        testClientDto.setName("George Perez");
        testClientDto.setDni("1802323823");
        testClientDto.setGender("M");
        testClientDto.setAge(22);
        testClientDto.setAddress("Quito");
        testClientDto.setPhone("092324223");
        testClientDto.setPassword("password123$");
        testClientDto.setActive(true);

        testClient = new Client();
        testClient.setId(1L);
        testClient.setName("George Perez");
        testClient.setDni("1802323823");
        testClient.setGender("M");
        testClient.setAge(22);
        testClient.setAddress("Quito");
        testClient.setPhone("092324223");
        testClient.setPassword("password123$");
        testClient.setActive(true);
    }

    // ========================================
    // PRUEBAS UNITARIAS (F5)
    // ========================================

    @Test
    void createClientTest() {
        // Arrange
        ClientDto newClient = new ClientDto(1L, "1802323823", "George Perez", "password123$", "M", 22, "Quito",
                "+51987654321", true);
        ClientDto createdClient = new ClientDto(1L, "1802323823", "George Perez", "password123$", "M", 22,
                "Quito", "092324223", true);
        when(clientService.create(newClient)).thenReturn(createdClient);

        // Act
        ResponseEntity<ClientDto> response = clientController.create(newClient);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createdClient, response.getBody());
        verify(clientService).create(newClient);
    }

    @Test
    void getAllClientsTest() {
        // Arrange
        List<ClientDto> clients = Arrays.asList(testClientDto);
        when(clientService.getAll()).thenReturn(clients);

        // Act
        ResponseEntity<List<ClientDto>> response = clientController.getAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<ClientDto> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.size());
        assertEquals(testClientDto.getName(), responseBody.get(0).getName());
    }

    @Test
    void getClientByIdTest() {
        // Arrange
        when(clientService.getById(1L)).thenReturn(testClientDto);

        // Act
        ResponseEntity<ClientDto> response = clientController.get(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ClientDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(testClientDto.getName(), responseBody.getName());
        assertEquals(testClientDto.getDni(), responseBody.getDni());
    }

    @Test
    void createClient_ShouldSucceed_WhenValidData() {
        // Arrange
        when(clientRepository.existsByDni(testClientDto.getDni())).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(testClient);

        // Act
        ClientDto result = clientServiceImpl.create(testClientDto);

        // Assert
        assertEquals(testClientDto.getName(), result.getName());
        assertEquals(testClientDto.getDni(), result.getDni());
        assertTrue(result.isActive());
        verify(clientRepository).existsByDni(testClientDto.getDni());
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void createClient_ShouldThrowException_WhenDniExists() {
        // Arrange
        when(clientRepository.existsByDni(testClientDto.getDni())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateDniException.class, () -> clientServiceImpl.create(testClientDto));
        verify(clientRepository).existsByDni(testClientDto.getDni());
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void getClientById_ShouldReturnClient_WhenExists() {
        // Arrange
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));

        // Act
        ClientDto result = clientServiceImpl.getById(1L);

        // Assert
        assertEquals(testClient.getName(), result.getName());
        assertEquals(testClient.getDni(), result.getDni());
        verify(clientRepository).findById(1L);
    }

    @Test
    void getClientById_ShouldThrowException_WhenNotExists() {
        // Arrange
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClientNotFoundException.class, () -> clientServiceImpl.getById(1L));
        verify(clientRepository).findById(1L);
    }

    @Test
    void deleteClient_ShouldSoftDelete_WhenExists() {
        // Arrange
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient));
        when(clientRepository.save(any(Client.class))).thenReturn(testClient);

        // Act
        clientServiceImpl.deleteById(1L);

        // Assert
        verify(clientRepository).findById(1L);
        verify(clientRepository).save(any(Client.class));
    }

    // ========================================
    // PRUEBA DE INTEGRACIÓN (F6)
    // ========================================
    @Test
    void integrationTest_CreateAndRetrieveClient_ShouldWork() {
        // Arrange - Preparamos un cliente para crear
        ClientDto newClient = new ClientDto();
        newClient.setName("María García");
        newClient.setDni("98765432101");
        newClient.setGender("F");
        newClient.setAge(25);
        newClient.setAddress("Av. Integración 456");
        newClient.setPhone("+51123456789");
        newClient.setPassword("integration123");
        newClient.setActive(true);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ClientDto> request = new HttpEntity<>(newClient, headers);

        // Act 1 - Crear cliente a través del endpoint REST
        ResponseEntity<ClientDto> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/clients",
                request,
                ClientDto.class);

        // Assert 1 - Verificar que se creó correctamente
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        ClientDto responseBody = createResponse.getBody();
        assertNotNull(responseBody);
        assertEquals("María García", responseBody.getName());
        assertEquals("98765432101", responseBody.getDni());
        assertTrue(responseBody.isActive());

        // Act 2 - Obtener el cliente creado por ID
        Long clientId = responseBody.getId();
        ResponseEntity<ClientDto> getResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/clients/" + clientId,
                ClientDto.class);

        // Assert 2 - Verificar que se recuperó correctamente
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals("María García", getResponse.getBody().getName());
        assertEquals("98765432101", getResponse.getBody().getDni());

        // Act 3 - Obtener todos los clientes
        ResponseEntity<ClientDto[]> getAllResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/clients",
                ClientDto[].class);

        // Assert 3 - Verificar que el cliente está en la lista
        assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());
        assertNotNull(getAllResponse.getBody());
        assertTrue(getAllResponse.getBody().length >= 1);

        // Verificar que nuestro cliente está en la respuesta
        boolean clientFound = false;
        for (ClientDto client : getAllResponse.getBody()) {
            if (client.getDni().equals("98765432101")) {
                clientFound = true;
                break;
            }
        }
        assertTrue(clientFound, "El cliente creado debe estar en la lista de todos los clientes");
    }
}
