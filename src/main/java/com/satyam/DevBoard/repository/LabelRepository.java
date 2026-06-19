package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LabelRepository extends JpaRepository<Label, UUID> {
}
