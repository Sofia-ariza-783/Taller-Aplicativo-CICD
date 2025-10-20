package com.example.Taller_Aplicativo_CICD.model.services;

import com.example.Taller_Aplicativo_CICD.controller.dtos.ParticipantDto;
import com.example.Taller_Aplicativo_CICD.model.models.Participant;
import com.example.Taller_Aplicativo_CICD.model.repositories.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceTest {

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private ParticipantService participantService;

    private Participant testParticipant;
    private ParticipantDto testParticipantDto;
    private String testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID().toString();
        testParticipant = new Participant(testId, "Test Participant", 1);
        testParticipantDto = new ParticipantDto("Test Participant", 1);
    }

    @Test
    void createParticipant_ShouldCreateNewParticipant() {
        // Arrange
        when(participantRepository.findByFullName(anyString())).thenReturn(null);
        when(participantRepository.save(any(Participant.class))).thenReturn(testParticipant);

        // Act
        Participant result = participantService.createParticipant(testParticipantDto);

        // Assert
        assertNotNull(result);
        assertEquals("Test Participant", result.getFullName());
        assertEquals(1, result.getSeason());
        verify(participantRepository, times(1)).save(any(Participant.class));
    }

    @Test
    void createParticipant_WhenParticipantExists_ShouldThrowException() {
        // Arrange
        when(participantRepository.findByFullName(anyString())).thenReturn(testParticipant);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> participantService.createParticipant(testParticipantDto));
        verify(participantRepository, never()).save(any(Participant.class));
    }

    @Test
    void getParticipantByName_ShouldReturnParticipant() {
        // Arrange
        when(participantRepository.findByFullName(anyString())).thenReturn(testParticipant);

        // Act
        Participant result = participantService.getParticipantByName("Test Participant");

        // Assert
        assertNotNull(result);
        assertEquals("Test Participant", result.getFullName());
    }

    @Test
    void getParticipantByName_WhenNotFound_ShouldThrowException() {
        // Arrange
        when(participantRepository.findByFullName(anyString())).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> participantService.getParticipantByName("nonexistent"));
    }

    @Test
    void getParticipantById_ShouldReturnParticipant() {
        // Arrange
        when(participantRepository.findById(testId)).thenReturn(Optional.of(testParticipant));

        // Act
        Participant result = participantService.getParticipantById(testId);

        // Assert
        assertNotNull(result);
        assertEquals("Test Participant", result.getFullName());
    }

    @Test
    void getParticipantById_WhenNotFound_ShouldThrowException() {
        // Arrange
        when(participantRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> participantService.getParticipantById("nonexistent"));
    }

    @Test
    void getParticipantsBySeason_ShouldReturnParticipants() {
        // Arrange
        List<Participant> participants = Arrays.asList(
            new Participant(UUID.randomUUID().toString(), "Participant 1", 1),
            new Participant(UUID.randomUUID().toString(), "Participant 2", 1)
        );
        when(participantRepository.findBySeason(1)).thenReturn(participants);

        // Act
        List<Participant> result = participantService.getParticipantsBySeason(1);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getSeason());
    }

    @Test
    void deleteParticipant_ShouldDeleteSuccessfully() {
        // Arrange
        doNothing().when(participantRepository).deleteById(testId);

        // Act & Assert
        assertDoesNotThrow(() -> participantService.deleteParticipant(testId));
        verify(participantRepository, times(1)).deleteById(testId);
    }

    @Test
    void deleteParticipant_WhenErrorOccurs_ShouldThrowException() {
        // Arrange
        doThrow(new RuntimeException("Database error")).when(participantRepository).deleteById(anyString());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> participantService.deleteParticipant("nonexistent"));
    }
}
