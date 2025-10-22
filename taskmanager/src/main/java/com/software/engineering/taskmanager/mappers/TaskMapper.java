package com.software.engineering.taskmanager.mappers;

import com.software.engineering.taskmanager.domain.dto.TaskDto;
import com.software.engineering.taskmanager.domain.entities.Task;

public interface TaskMapper
{
    Task fromDto(TaskDto taskDto);

    TaskDto toDto(Task task);
}
