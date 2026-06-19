package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
