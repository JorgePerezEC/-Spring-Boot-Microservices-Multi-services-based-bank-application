package com.devsu.hackerearth.backend.client.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsu.hackerearth.backend.client.exception.ClientNotFoundException;
import com.devsu.hackerearth.backend.client.exception.DuplicateDniException;
import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.repository.ClientRepository;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;

	public ClientServiceImpl(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ClientDto> getAll() {
		// Get all clients
		return clientRepository.findAll()
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public ClientDto getById(Long id) {
		Optional<Client> clientOpt = clientRepository.findById(id);
		return clientOpt.map(this::convertToDto).orElse(null);
	}

	@Override
	public ClientDto create(ClientDto clientDto) {
		// Validate DNI doesn't exist
		if (clientRepository.existsByDni(clientDto.getDni())) {
			throw new DuplicateDniException(clientDto.getDni());
		}

		Client client = convertToEntity(clientDto);
		client.setActive(true); // New clients are active by default
		Client savedClient = clientRepository.save(client);
		return convertToDto(savedClient);
	}

	@Override
	public ClientDto update(ClientDto clientDto) {
		Optional<Client> clientOpt = clientRepository.findById(clientDto.getId());
		if (!clientOpt.isPresent()) {
			return null;
		}

		Client existingClient = clientOpt.get();

		// Check if DNI is being changed and if it already exists
		if (!existingClient.getDni().equals(clientDto.getDni()) &&
				clientRepository.existsByDni(clientDto.getDni())) {
			throw new DuplicateDniException(clientDto.getDni());
		}

		// Update fields
		existingClient.setName(clientDto.getName());
		existingClient.setDni(clientDto.getDni());
		existingClient.setGender(clientDto.getGender());
		existingClient.setAge(clientDto.getAge());
		existingClient.setAddress(clientDto.getAddress());
		existingClient.setPhone(clientDto.getPhone());
		existingClient.setPassword(clientDto.getPassword());
		existingClient.setActive(clientDto.isActive());

		Client savedClient = clientRepository.save(existingClient);
		return convertToDto(savedClient);
	}

	@Override
	public ClientDto partialUpdate(Long id, PartialClientDto partialClientDto) {
		Optional<Client> clientOpt = clientRepository.findById(id);
		if (!clientOpt.isPresent()) {
			return null;
		}

		Client existingClient = clientOpt.get();

		// Update only provided fields
		if (partialClientDto.getIsActive() != null) {
			existingClient.setActive(partialClientDto.getIsActive());
		}
		if (partialClientDto.getPassword() != null) {
			existingClient.setPassword(partialClientDto.getPassword());
		}
		if (partialClientDto.getAddress() != null) {
			existingClient.setAddress(partialClientDto.getAddress());
		}
		if (partialClientDto.getPhone() != null) {
			existingClient.setPhone(partialClientDto.getPhone());
		}

		Client savedClient = clientRepository.save(existingClient);
		return convertToDto(savedClient);
	}

	@Override
	public void deleteById(Long id) {
		Optional<Client> clientOpt = clientRepository.findById(id);
		if (clientOpt.isPresent()) {
			Client client = clientOpt.get();
			// Soft delete: mark as inactive instead of physically deleting
			client.setActive(false);
			clientRepository.save(client);
		}
	}

	// Conversion methods
	private ClientDto convertToDto(Client client) {
		return new ClientDto(
				client.getId(),
				client.getDni(),
				client.getName(),
				client.getPassword(),
				client.getGender(),
				client.getAge(),
				client.getAddress(),
				client.getPhone(),
				client.isActive());
	}

	private Client convertToEntity(ClientDto clientDto) {
		return new Client(
				clientDto.getName(),
				clientDto.getDni(),
				clientDto.getGender(),
				clientDto.getAge(),
				clientDto.getAddress(),
				clientDto.getPhone(),
				clientDto.getPassword(),
				clientDto.isActive());
	}
}
