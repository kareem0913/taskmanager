package com.taskmanager.service.task;

import com.taskmanager.model.dto.task.TaskCreate;
import com.taskmanager.model.dto.task.TaskResponse;
import com.taskmanager.model.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskResponse findTask(Long id, Long userId);

    Page<TaskResponse> findAllTasks(Pageable pageable, Long userId);

    TaskResponse createTask(TaskCreate taskCreate, Long userId);

    TaskResponse updateTask(Long id, TaskCreate taskCreate, Long userId);

    void updateTaskStatus(Long id, TaskStatus taskStatus, Long userId);

    void deleteTask(Long id, Long userId);
}
