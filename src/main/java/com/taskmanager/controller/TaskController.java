package com.taskmanager.controller;

import com.taskmanager.model.dto.task.TaskCreate;
import com.taskmanager.model.dto.task.TaskResponse;
import com.taskmanager.model.enums.TaskStatus;
import com.taskmanager.security.UserPrincipal;
import com.taskmanager.service.task.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Task Management", description = "APIs for managing user tasks")
@SecurityRequirement(name = "JWT")
public class TaskController {
    private final TaskService taskService;

    @Operation(summary = "Get task by ID", description = "Retrieve a specific task by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Task belongs to another user"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    public TaskResponse httpGetTask(@PathVariable Long id, @AuthenticationPrincipal final UserPrincipal currentUser) {
        return taskService.findTask(id, currentUser.getId());
    }

    @Operation(summary = "Get all user tasks", description = "Retrieve all tasks for the authenticated user with pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public Page<TaskResponse> httpGetAllTasks(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @AuthenticationPrincipal final UserPrincipal currentUser) {
        Pageable pageable = PageRequest.of(page, size);
        return taskService.findAllTasks(pageable, currentUser.getId());
    }

    @Operation(summary = "Create new task", description = "Create a new task for the authenticated user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public TaskResponse httpCreateTask(@Valid @RequestBody @NotNull TaskCreate taskCreate,
                                     @AuthenticationPrincipal final UserPrincipal currentUser) {
        return taskService.createTask(taskCreate, currentUser.getId());
    }

    @Operation(summary = "Update task", description = "Update an existing task completely")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Task belongs to another user"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}")
    public TaskResponse httpUpdateTask(@PathVariable final Long id,
                                    @Valid @RequestBody @NotNull TaskCreate taskCreate,
                                    @AuthenticationPrincipal final UserPrincipal currentUser) {
        return taskService.updateTask(id, taskCreate, currentUser.getId());
    }

    @Operation(summary = "Update task status", description = "Update only the status of a specific task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid status value"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Task belongs to another user"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PatchMapping("/{id}/status")
    public void httpUpdateTaskStatus(@PathVariable final Long id,
                                            @RequestParam TaskStatus taskStatus,
                                            @AuthenticationPrincipal final UserPrincipal currentUser) {
        taskService.updateTaskStatus(id, taskStatus, currentUser.getId());
    }

    @Operation(summary = "Delete task", description = "Delete a specific task")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Task belongs to another user"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{id}")
    public void httpDeleteTask(@PathVariable Long id,
                               @AuthenticationPrincipal final UserPrincipal currentUser) {
        taskService.deleteTask(id, currentUser.getId());
    }
}
