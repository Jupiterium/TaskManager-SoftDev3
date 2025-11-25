package com.software.engineering.taskmanager.repositories;

import com.software.engineering.taskmanager.domain.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByIsReadFalseOrderByCreatedDesc();
    List<Notification> findAllByOrderByCreatedDesc();
    List<Notification> findByTaskIdOrderByCreatedDesc(UUID taskId);
}