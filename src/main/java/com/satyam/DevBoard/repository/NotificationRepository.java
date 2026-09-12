package com.satyam.DevBoard.repository;

import com.satyam.DevBoard.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    @Query("select n from Notification n " +
            "join fetch n.user " +
            "join fetch n.organization " +
            "where n.id = :id")
    Optional<Notification> findByIdWithDetails(@Param("id") UUID id);

    @Query("SELECT n FROM Notification n " +
            "JOIN FETCH n.user " +
            "JOIN FETCH n.organization " +
            "WHERE n.user.id = :userId " +
            "ORDER BY n.createdAt DESC")
    List<Notification> findAllByUserIdWithDetails(@Param("userId") UUID userId);

    @Query("SELECT n FROM Notification n " +
            "JOIN FETCH n.user " +
            "JOIN FETCH n.organization " +
            "WHERE n.user.id = :userId " +
            "AND n.isRead = false " +
            "ORDER BY n.createdAt DESC")
    List<Notification> findUnreadByUserId(@Param("userId") UUID userId);

    @Query("select count(n) from Notification n " +
            "where n.user.id = :userId and n.isRead = false")
    long countUnreadByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query("update Notification n set n.isRead = true " +
            "where n.user.id = :userd and n.isRead = false")
    void markAllAsReadByUserId(@Param("userId") UUID userId);
}
