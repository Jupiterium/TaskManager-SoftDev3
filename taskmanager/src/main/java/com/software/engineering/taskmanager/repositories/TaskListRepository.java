package com.software.engineering.taskmanager.repositories;

import com.software.engineering.taskmanager.domain.entities.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface TaskListRepository extends JpaRepository<TaskList, UUID> { }
