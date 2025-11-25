package com.software.engineering.taskmanager.repositories;

import com.software.engineering.taskmanager.domain.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID>
{
    List<Task> findByTaskListId(UUID taskListId);
    Optional<Task> findByTaskListIdAndId(UUID taskListId, UUID id);
    void deleteByTaskListIdAndId(UUID taskListId, UUID id);
    List<Task> findByCustomReminderDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Task> findByDueDateBetween(LocalDateTime start, LocalDateTime end);
}