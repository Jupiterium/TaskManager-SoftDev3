package com.software.engineering.taskmanager.controllers;

import com.software.engineering.taskmanager.domain.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {
    
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    
    @Test
    void handleExceptions_IllegalArgumentException_ReturnsBadRequest() {
        IllegalArgumentException exception = new IllegalArgumentException("Test error");
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/test");
        
        ResponseEntity<ErrorResponse> response = handler.handleExceptions(exception, request);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(400, response.getBody().status());
        assertEquals("Test error", response.getBody().message());
        assertEquals("uri=/test", response.getBody().details());
    }
}