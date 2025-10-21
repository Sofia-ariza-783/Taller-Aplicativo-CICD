package com.example.Taller_Aplicativo_CICD.controller.controllers;

import com.example.Taller_Aplicativo_CICD.controller.dtos.ViewerDto;
import com.example.Taller_Aplicativo_CICD.model.models.Viewer;
import com.example.Taller_Aplicativo_CICD.model.services.ViewerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewerControllerTest {

    @Mock
    private ViewerService viewerService;

    @InjectMocks
    private ViewerController viewerController;

    private Viewer testViewer;
    private ViewerDto testViewerDto;
    private String testId;
    private String testName;

    @BeforeEach
    void setUp() {
        testId = "viewer-123";
        testName = "Test Viewer";
        testViewer = new Viewer(testId, testName);
        testViewerDto = new ViewerDto(testName);
    }

    @Test
    void createViewer_ShouldReturnCreated() {
        // Arrange
        when(viewerService.createViewer(any(ViewerDto.class))).thenReturn(testViewer);

        // Act
        ResponseEntity<Viewer> response = viewerController.createViewer(testViewerDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testName, response.getBody().getFullName());
        verify(viewerService, times(1)).createViewer(any(ViewerDto.class));
    }

    @Test
    void createViewer_WhenServiceThrows_ShouldReturnBadRequest() {
        // Arrange
        when(viewerService.createViewer(any(ViewerDto.class)))
            .thenThrow(new RuntimeException("Error creating viewer"));

        // Act
        ResponseEntity<Viewer> response = viewerController.createViewer(testViewerDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getViewerByName_ShouldReturnViewer() {
        // Arrange
        when(viewerService.getViewerByName(anyString())).thenReturn(testViewer);

        // Act
        ResponseEntity<Viewer> response = viewerController.getViewerByName(testName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testName, response.getBody().getFullName());
    }

    @Test
    void getViewerByName_WhenNotFound_ShouldReturnNotFound() {
        // Arrange
        when(viewerService.getViewerByName(anyString()))
            .thenThrow(new RuntimeException("Viewer not found"));

        // Act
        ResponseEntity<Viewer> response = viewerController.getViewerByName("nonexistent");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteViewer_ShouldReturnNoContent() {
        // Arrange
        doNothing().when(viewerService).deleteViewer(anyString());

        // Act
        ResponseEntity<Void> response = viewerController.deleteViewer(testId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(viewerService, times(1)).deleteViewer(testId);
    }

    @Test
    void deleteViewer_WhenNotFound_ShouldReturnNotFound() {
        // Arrange
        doThrow(new RuntimeException("Viewer not found")).when(viewerService).deleteViewer(anyString());

        // Act
        ResponseEntity<Void> response = viewerController.deleteViewer("nonexistent");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
