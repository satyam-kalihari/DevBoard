package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    boolean existsUserByKeycloakId(UUID keycloakId);
    Optional<User> findByKeycloakId(UUID keycloakId);
}
