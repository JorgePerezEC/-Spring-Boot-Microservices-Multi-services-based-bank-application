package com.devsu.hackerearth.backend.client.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devsu.hackerearth.backend.client.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByDni(String dni);

    List<Client> findByIsActive(boolean isActive);

    @Query("SELECT c FROM Client c WHERE c.isActive = true")
    List<Client> findAllActiveClients();
    // List<Client> findByIsActiveTrue();

    @Query("SELECT c FROM Client c WHERE c.name LIKE %:name%")
    List<Client> findByNameContaining(@Param("name") String name);

    // List<Client> findByNameContaining(String name);

    boolean existsByDni(String dni);
}
