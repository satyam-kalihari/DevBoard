package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
