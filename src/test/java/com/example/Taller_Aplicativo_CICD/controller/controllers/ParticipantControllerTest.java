package com.example.Taller_Aplicativo_CICD.controller.controllers;

import com.example.Taller_Aplicativo_CICD.controller.dtos.ParticipantDto;
import com.example.Taller_Aplicativo_CICD.model.models.Participant;
import com.example.Taller_Aplicativo_CICD.model.services.ParticipantService;
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
class ParticipantControllerTest {

    @Mock
    private ParticipantService participantService;

    @InjectMocks
    private ParticipantController participantController;

    private Participant testParticipant;
    private ParticipantDto testParticipantDto;
    private String testId;

    @BeforeEach
    void setUp() {
        testId = "test-id-123";
        testParticipant = new Participant(testId, "Test Participant", 1);
        testParticipantDto = new ParticipantDto("Test Participant", 1);
    }

    @Test
    void createParticipant_ShouldReturnCreated() {
        // Arrange
        when(participantService.createParticipant(any(ParticipantDto.class))).thenReturn(testParticipant);

        // Act
        ResponseEntity<Participant> response = participantController.createParticipant(testParticipantDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Participant", response.getBody().getFullName());
        verify(participantService, times(1)).createParticipant(any(ParticipantDto.class));
    }

    @Test
    void createParticipant_WhenServiceThrows_ShouldReturnBadRequest() {
        // Arrange
        when(participantService.createParticipant(any(ParticipantDto.class)))
            .thenThrow(new RuntimeException("Error creating participant"));

        // Act
        ResponseEntity<Participant> response = participantController.createParticipant(testParticipantDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getParticipantByName_ShouldReturnParticipant() {
        // Arrange
        when(participantService.getParticipantByName(anyString())).thenReturn(testParticipant);

        // Act
        ResponseEntity<Participant> response = participantController.getParticipantByName("Test Participant");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Participant", response.getBody().getFullName());
    }

    @Test
    void getParticipantByName_WhenNotFound_ShouldReturnNotFound() {
        // Arrange
        when(participantService.getParticipantByName(anyString()))
            .thenThrow(new RuntimeException("Participant not found"));

        // Act
        ResponseEntity<Participant> response = participantController.getParticipantByName("Nonexistent");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getParticipantById_ShouldReturnParticipant() {
        // Arrange
        when(participantService.getParticipantById(anyString())).thenReturn(testParticipant);

        // Act
        ResponseEntity<Participant> response = participantController.getParticipantById(testId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testId, response.getBody().getId());
    }

    @Test
    void deleteParticipant_ShouldReturnNoContent() {
        // Arrange
        doNothing().when(participantService).deleteParticipant(anyString());

        // Act
        ResponseEntity<Void> response = participantController.deleteParticipant(testId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(participantService, times(1)).deleteParticipant(testId);
    }

    @Test
    void deleteParticipant_WhenNotFound_ShouldReturnNotFound() {
        // Arrange
        doThrow(new RuntimeException("Participant not found")).when(participantService).deleteParticipant(anyString());

        // Act
        ResponseEntity<Void> response = participantController.deleteParticipant("nonexistent");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
