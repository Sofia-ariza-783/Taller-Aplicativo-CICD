package com.example.Taller_Aplicativo_CICD.model.services;

import com.example.Taller_Aplicativo_CICD.controller.dtos.ViewerDto;
import com.example.Taller_Aplicativo_CICD.model.models.Viewer;
import com.example.Taller_Aplicativo_CICD.model.repositories.ViewerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewerServiceTest {

    @Mock
    private ViewerRepository viewerRepository;

    @InjectMocks
    private ViewerService viewerService;

    private Viewer testViewer;
    private ViewerDto testViewerDto;
    private String testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID().toString();
        testViewer = new Viewer(testId, "Test Viewer");
        testViewerDto = new ViewerDto("Test Viewer");
    }

    @Test
    void createViewer_ShouldCreateNewViewer() {
        // Arrange
        when(viewerRepository.findByFullName(anyString())).thenReturn(null);
        when(viewerRepository.save(any(Viewer.class))).thenReturn(testViewer);

        // Act
        Viewer result = viewerService.createViewer(testViewerDto);

        // Assert
        assertNotNull(result);
        assertEquals("Test Viewer", result.getFullName());
        verify(viewerRepository, times(1)).save(any(Viewer.class));
    }

    @Test
    void createViewer_WhenViewerExists_ShouldThrowException() {
        // Arrange
        when(viewerRepository.findByFullName(anyString())).thenReturn(testViewer);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> viewerService.createViewer(testViewerDto));
        verify(viewerRepository, never()).save(any(Viewer.class));
    }

    @Test
    void getViewerByName_ShouldReturnViewer() {
        // Arrange
        when(viewerRepository.findByFullName(anyString())).thenReturn(testViewer);

        // Act
        Viewer result = viewerService.getViewerByName("Test Viewer");

        // Assert
        assertNotNull(result);
        assertEquals("Test Viewer", result.getFullName());
    }

    @Test
    void getViewerByName_WhenNotFound_ShouldThrowException() {
        // Arrange
        when(viewerRepository.findByFullName(anyString())).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> viewerService.getViewerByName("nonexistent"));
    }

    @Test
    void deleteViewer_ShouldDeleteSuccessfully() {
        // Arrange
        doNothing().when(viewerRepository).deleteById(testId);

        // Act & Assert
        assertDoesNotThrow(() -> viewerService.deleteViewer(testId));
        verify(viewerRepository, times(1)).deleteById(testId);
    }

    @Test
    void deleteViewer_WhenErrorOccurs_ShouldThrowException() {
        // Arrange
        doThrow(new RuntimeException("Database error")).when(viewerRepository).deleteById(anyString());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> viewerService.deleteViewer("nonexistent"));
    }
}
