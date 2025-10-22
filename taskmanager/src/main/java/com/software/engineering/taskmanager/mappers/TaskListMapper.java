package com.software.engineering.taskmanager.mappers;

import com.software.engineering.taskmanager.domain.dto.TaskListDto;
import com.software.engineering.taskmanager.domain.entities.TaskList;

public interface TaskListMapper
{
    TaskList fromDto(TaskListDto taskListDto);

    TaskListDto toDto(TaskList taskList);
}
