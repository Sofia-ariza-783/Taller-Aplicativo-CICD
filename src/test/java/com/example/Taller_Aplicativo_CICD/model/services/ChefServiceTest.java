package com.example.Taller_Aplicativo_CICD.model.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.example.Taller_Aplicativo_CICD.controller.dtos.ChefDto;
import com.example.Taller_Aplicativo_CICD.model.models.Chef;
import com.example.Taller_Aplicativo_CICD.model.repositories.ChefRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChefServiceTest {

  @Mock private ChefRepository chefRepository;

  @InjectMocks private ChefService chefService;

  private Chef testChef;
  private ChefDto testChefDto;
  private String testId;

  @BeforeEach
  void setUp() {
    testId = UUID.randomUUID().toString();
    testChef = new Chef(testId, "Test Chef");
    testChefDto = new ChefDto("Test Chef");
  }

  @Test
  void createChef_ShouldCreateNewChef() {
    // Arrange
    when(chefRepository.findByFullName(anyString())).thenReturn(null);
    when(chefRepository.save(any(Chef.class))).thenReturn(testChef);

    // Act
    Chef result = chefService.createChef(testChefDto);

    // Assert
    assertNotNull(result);
    assertEquals("Test Chef", result.getFullName());
    verify(chefRepository, times(1)).save(any(Chef.class));
  }

  @Test
  void createChef_WhenChefExists_ShouldThrowException() {
    // Arrange
    when(chefRepository.findByFullName(anyString())).thenReturn(testChef);

    // Act & Assert
    assertThrows(RuntimeException.class, () -> chefService.createChef(testChefDto));
    verify(chefRepository, never()).save(any(Chef.class));
  }

  @Test
  void getChefById_ShouldReturnChef() {
    // Arrange
    when(chefRepository.findById(testId)).thenReturn(Optional.of(testChef));

    // Act
    Chef result = chefService.getChefById(testId);

    // Assert
    assertNotNull(result);
    assertEquals("Test Chef", result.getFullName());
  }

  @Test
  void getChefById_WhenNotFound_ShouldThrowException() {
    // Arrange
    when(chefRepository.findById(anyString())).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(RuntimeException.class, () -> chefService.getChefById("nonexistent"));
  }

  @Test
  void deleteChef_ShouldDeleteSuccessfully() {
    // Arrange
    doNothing().when(chefRepository).deleteById(testId);

    // Act & Assert
    assertDoesNotThrow(() -> chefService.deleteChef(testId));
    verify(chefRepository, times(1)).deleteById(testId);
  }

  @Test
  void deleteChef_WhenErrorOccurs_ShouldThrowException() {
    // Arrange
    doThrow(new RuntimeException("Database error")).when(chefRepository).deleteById(anyString());

    // Act & Assert
    assertThrows(RuntimeException.class, () -> chefService.deleteChef("nonexistent"));
  }
}
