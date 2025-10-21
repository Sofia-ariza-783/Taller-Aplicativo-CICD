package com.example.Taller_Aplicativo_CICD.controller.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.example.Taller_Aplicativo_CICD.controller.dtos.ChefDto;
import com.example.Taller_Aplicativo_CICD.model.models.Chef;
import com.example.Taller_Aplicativo_CICD.model.services.ChefService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ChefControllerTest {

  @Mock private ChefService chefService;

  @InjectMocks private chefController chefController;

  private Chef testChef;
  private ChefDto testChefDto;
  private String testId;

  @BeforeEach
  void setUp() {
    testId = "chef-123";
    testChef = new Chef(testId, "Test Chef");
    testChefDto = new ChefDto("Test Chef");
  }

  @Test
  void createChef_ShouldReturnCreated() {
    // Arrange
    when(chefService.createChef(any(ChefDto.class))).thenReturn(testChef);

    // Act
    ResponseEntity<Chef> response = chefController.createChef(testChefDto);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Test Chef", response.getBody().getFullName());
    verify(chefService, times(1)).createChef(any(ChefDto.class));
  }

  @Test
  void createChef_WhenServiceThrows_ShouldReturnBadRequest() {
    // Arrange
    when(chefService.createChef(any(ChefDto.class)))
        .thenThrow(new RuntimeException("Error creating chef"));

    // Act
    ResponseEntity<Chef> response = chefController.createChef(testChefDto);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  void getChefById_ShouldReturnChef() {
    // Arrange
    when(chefService.getChefById(anyString())).thenReturn(testChef);

    // Act
    ResponseEntity<Chef> response = chefController.getChefById(testId);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Test Chef", response.getBody().getFullName());
  }

  @Test
  void getChefById_WhenNotFound_ShouldReturnNotFound() {
    // Arrange
    when(chefService.getChefById(anyString())).thenThrow(new RuntimeException("Chef not found"));

    // Act
    ResponseEntity<Chef> response = chefController.getChefById("nonexistent");

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void deleteChef_ShouldReturnNoContent() {
    // Arrange
    doNothing().when(chefService).deleteChef(anyString());

    // Act
    ResponseEntity<Void> response = chefController.deleteChef(testId);

    // Assert
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(chefService, times(1)).deleteChef(testId);
  }

  @Test
  void deleteChef_WhenNotFound_ShouldReturnNotFound() {
    // Arrange
    doThrow(new RuntimeException("Chef not found")).when(chefService).deleteChef(anyString());

    // Act
    ResponseEntity<Void> response = chefController.deleteChef("nonexistent");

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}
