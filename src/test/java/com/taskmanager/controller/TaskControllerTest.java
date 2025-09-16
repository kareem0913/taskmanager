package com.taskmanager.controller;

import com.taskmanager.model.dto.task.TaskResponse;
import com.taskmanager.security.UserPrincipal;
import com.taskmanager.service.task.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Test
    void httpGetTask_ValidRequest_ReturnsTaskResponse() {
        // Arrange
        Long taskId = 1L;
        Long userId = 1L;
        UserPrincipal userPrincipal = new UserPrincipal(userId, "testUser", "test@email.com", "password", null);
        TaskResponse expectedResponse = new TaskResponse(/* populate fields */);

        when(taskService.findTask(taskId, userId)).thenReturn(expectedResponse);

        // Act
        TaskResponse actualResponse = taskController.httpGetTask(taskId, userPrincipal);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(taskService, times(1)).findTask(taskId, userId);
    }
}