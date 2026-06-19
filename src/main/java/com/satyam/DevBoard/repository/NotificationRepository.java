package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}
