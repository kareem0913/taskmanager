package com.taskmanager.mapper;

import com.taskmanager.model.dto.task.TaskCreate;
import com.taskmanager.model.dto.task.TaskResponse;
import com.taskmanager.model.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {
    public abstract TaskResponse toTaskResponse(Task task);

    public abstract Task toTask(TaskCreate taskCreate);
}
