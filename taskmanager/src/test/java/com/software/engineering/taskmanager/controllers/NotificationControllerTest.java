package com.software.engineering.taskmanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.engineering.taskmanager.domain.entities.Notification;
import com.software.engineering.taskmanager.domain.entities.NotificationType;
import com.software.engineering.taskmanager.mappers.NotificationMapper;
import com.software.engineering.taskmanager.services.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private NotificationMapper notificationMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllNotifications_ReturnsNotificationList() throws Exception {
        Notification notification = createTestNotification();
        when(notificationService.getAllNotifications()).thenReturn(List.of(notification));
        when(notificationMapper.toDto(any())).thenReturn(createTestNotificationDto());

        mockMvc.perform(get("/notifications"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getUnreadNotifications_ReturnsUnreadNotificationList() throws Exception {
        Notification notification = createTestNotification();
        when(notificationService.getUnreadNotifications()).thenReturn(List.of(notification));
        when(notificationMapper.toDto(any())).thenReturn(createTestNotificationDto());

        mockMvc.perform(get("/notifications/unread"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void markAsRead_ReturnsUpdatedNotification() throws Exception {
        UUID notificationId = UUID.randomUUID();
        Notification notification = createTestNotification();
        notification.setRead(true);
        
        when(notificationService.markAsRead(notificationId)).thenReturn(notification);
        when(notificationMapper.toDto(any())).thenReturn(createTestNotificationDto());

        mockMvc.perform(put("/notifications/{id}/read", notificationId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deleteNotification_ReturnsNoContent() throws Exception {
        UUID notificationId = UUID.randomUUID();

        mockMvc.perform(delete("/notifications/{id}", notificationId))
                .andExpect(status().isNoContent());
    }

    @Test
    void createTestNotification_ReturnsCreatedNotification() throws Exception {
        Notification notification = createTestNotification();
        when(notificationService.createNotification(any(), any(), any(), any())).thenReturn(notification);
        when(notificationMapper.toDto(any())).thenReturn(createTestNotificationDto());

        mockMvc.perform(post("/notifications/test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    private Notification createTestNotification() {
        return new Notification(
                UUID.randomUUID(),
                "Test Title",
                "Test Message",
                NotificationType.TASK_DUE_SOON,
                null,
                false,
                LocalDateTime.now()
        );
    }

    private com.software.engineering.taskmanager.domain.dto.NotificationDto createTestNotificationDto() {
        return new com.software.engineering.taskmanager.domain.dto.NotificationDto(
                UUID.randomUUID(),
                "Test Title",
                "Test Message",
                NotificationType.TASK_DUE_SOON,
                null,
                null,
                false,
                LocalDateTime.now()
        );
    }
}