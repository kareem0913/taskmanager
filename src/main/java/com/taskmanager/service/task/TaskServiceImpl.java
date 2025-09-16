package com.taskmanager.service.task;

import com.taskmanager.error.exception.AccessDeniedException;
import com.taskmanager.error.exception.ResourceNotFoundException;
import com.taskmanager.mapper.TaskMapper;
import com.taskmanager.model.dto.task.TaskCreate;
import com.taskmanager.model.dto.task.TaskResponse;
import com.taskmanager.model.entity.Task;
import com.taskmanager.model.entity.User;
import com.taskmanager.model.enums.TaskStatus;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Override
    public TaskResponse findTask(Long id, Long userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() ->{
                    log.error("Task not found or access denied with id: {}", id);
                     return new AccessDeniedException("access denied", "Task not found or access denied");
                });

        return taskMapper.toTaskResponse(task);
    }

    @Override
    public Page<TaskResponse> findAllTasks(Pageable pageable, Long userId) {
        Page<Task> tasks = taskRepository.findAllByUserId(userId, pageable);
        return tasks.map(taskMapper::toTaskResponse);
    }

    @Override
    public TaskResponse createTask(TaskCreate taskCreate, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->{
                    log.error("User not found with id: {}", userId);
                    return new ResourceNotFoundException("User not found" , "User not found with id: " + userId);
                });

        Task task = taskMapper.toTask(taskCreate);
        task.setUser(user);

        taskRepository.save(task);
        log.info("Task created successfully with id: {}", task.getId());
        return taskMapper.toTaskResponse(task);
    }

    @Override
    public TaskResponse updateTask(Long id, TaskCreate taskCreate, Long userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> {
                    log.error("Task not found or access denied with id: {}", id);
                    return new AccessDeniedException("access denied", "Task not found or access denied");
                });

        task.setTitle(taskCreate.getTitle());
        task.setDescription(taskCreate.getDescription());
        task.setStatus(taskCreate.getStatus());

        taskRepository.save(task);
        log.info("Task updated successfully with id: {}", task.getId());
        return taskMapper.toTaskResponse(task);
    }

    @Transactional
    @Override
    public void updateTaskStatus(Long id, TaskStatus taskStatus, Long userId) {
        taskRepository.updateStatusById(id, taskStatus, userId);
        log.info("Task status updated successfully with id: {}", id);
    }

    @Override
    public void deleteTask(Long id, Long userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() ->{
                    log.error("Task not found or access denied with id: {}", id);
                    return new AccessDeniedException("access denied", "Task not found or access denied");
                });
        taskRepository.delete(task);
        log.info("Task deleted successfully with id: {}", id);
    }
}
