package com.example.Taller_Aplicativo_CICD.model.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.example.Taller_Aplicativo_CICD.controller.dtos.RecipeDto;
import com.example.Taller_Aplicativo_CICD.controller.dtos.RecipeResponseDto;
import com.example.Taller_Aplicativo_CICD.model.models.Chef;
import com.example.Taller_Aplicativo_CICD.model.models.Participant;
import com.example.Taller_Aplicativo_CICD.model.models.Recipe;
import com.example.Taller_Aplicativo_CICD.model.models.Viewer;
import com.example.Taller_Aplicativo_CICD.model.repositories.ChefRepository;
import com.example.Taller_Aplicativo_CICD.model.repositories.RecipeRepository;
import com.example.Taller_Aplicativo_CICD.model.repositories.ViewerRepository;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

  @Mock private RecipeRepository recipeRepository;

  @Mock private ParticipantService participantService;

  @Mock private ChefRepository chefRepository;

  @Mock private ViewerRepository viewerRepository;

  @InjectMocks private RecipeService recipeService;

  private Recipe testRecipe;
  private RecipeDto testRecipeDto;
  private Participant testParticipant;
  private Chef testChef;
  private Viewer testViewer;

  @BeforeEach
  void setUp() {
    testRecipe =
        new Recipe(
            "1",
            "title",
            "Chef John",
            1,
            Arrays.asList("ingredient1", "ingredient2"),
            Arrays.asList("Step 1", "Step 2"));

    testRecipeDto =
        new RecipeDto("title", "Chef John", "ingredient1, ingredient2", "Step 1; Step 2");

    testParticipant = new Participant("1", "Chef John", 1);
    testChef = new Chef("1", "Chef John");
    testViewer = new Viewer("1", "Viewer Jane");
  }

  @Test
  void createRecipe_ShouldCreateNewRecipe() {
    // Arrange
    when(recipeRepository.findAll()).thenReturn(new ArrayList<>());
    when(recipeRepository.save(any(Recipe.class))).thenReturn(testRecipe);

    // Act
    Recipe result = recipeService.createRecipe(testRecipeDto);

    // Assert
    assertNotNull(result);
    assertEquals("Chef John", result.getAuthor());
    assertEquals(1, result.getNum());
    verify(recipeRepository, times(1)).save(any(Recipe.class));
  }

  @Test
  void getRecipeByParticipant_ShouldReturnRecipeResponseDto() {
    // Arrange
    when(recipeRepository.findByAuthor(anyString())).thenReturn(testRecipe);
    when(participantService.getParticipantByName(anyString())).thenReturn(testParticipant);

    // Act
    RecipeResponseDto result = recipeService.getRecipeByParticipant("Chef John");

    // Assert
    assertNotNull(result);
    assertEquals("Chef John", result.author());
    assertEquals(1, result.season());
  }

  @Test
  void getRecipeByAutor_WithChef_ShouldReturnRecipe() {
    // Arrange
    when(chefRepository.findByFullName(anyString())).thenReturn(testChef);
    when(recipeRepository.findByAuthor(anyString())).thenReturn(testRecipe);

    // Act
    Recipe result = recipeService.getRecipeByAutor("Chef John");

    // Assert
    assertNotNull(result);
    assertEquals("Chef John", result.getAuthor());
  }

  @Test
  void getRecipeByAutor_WithViewer_ShouldReturnRecipe() {
    // Arrange
    when(chefRepository.findByFullName(anyString())).thenReturn(null);
    when(viewerRepository.findByFullName(anyString())).thenReturn(testViewer);
    when(recipeRepository.findByAuthor(anyString())).thenReturn(testRecipe);

    // Act
    Recipe result = recipeService.getRecipeByAutor("Viewer Jane");

    // Assert
    assertNotNull(result);
    assertEquals("Chef John", result.getAuthor());
  }

  @Test
  void getAllRecipes_ShouldReturnAllRecipes() {
    // Arrange
    List<Recipe> recipes = Arrays.asList(testRecipe);
    when(recipeRepository.findAll()).thenReturn(recipes);

    // Act
    List<Recipe> result = recipeService.getAllRecipes();

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Chef John", result.get(0).getAuthor());
  }

  @Test
  void getRecipeByNumber_ShouldReturnRecipe() {
    // Arrange
    when(recipeRepository.findByNum(anyInt())).thenReturn(Optional.of(testRecipe));

    // Act
    Recipe result = recipeService.getRecipeByNumber(1);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getNum());
  }

  @Test
  void getRecipesBySeason_ShouldReturnRecipes() {
    // Arrange
    List<Participant> participants = Arrays.asList(testParticipant);
    when(participantService.getParticipantsBySeason(anyInt())).thenReturn(participants);
    when(recipeRepository.findByAuthor(anyString())).thenReturn(testRecipe);
    when(participantService.getParticipantByName(anyString())).thenReturn(testParticipant);

    // Act
    List<RecipeResponseDto> result = recipeService.getRecipesBySeason(1);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals("Chef John", result.get(0).author());
  }

  @Test
  void getRecipesByIngredient_ShouldReturnMatchingRecipes() {
    // Arrange
    List<Recipe> recipes = Arrays.asList(testRecipe);
    when(recipeRepository.findByIngredient(anyString())).thenReturn(recipes);

    // Act
    List<Recipe> result = recipeService.getRecipesByIngredient("ingredient1");

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertTrue(result.get(0).getIngredients().contains("ingredient1"));
  }

  @Test
  void createRecipe_WhenRepositoryThrowsException_ShouldThrowRuntimeException() {
    // Arrange
    when(recipeRepository.findAll()).thenReturn(new ArrayList<>());
    when(recipeRepository.save(any(Recipe.class)))
        .thenThrow(new RuntimeException("Database error"));

    // Act & Assert
    assertThrows(RuntimeException.class, () -> recipeService.createRecipe(testRecipeDto));
    verify(recipeRepository, times(1)).save(any(Recipe.class));
  }

  @Test
  void getRecipeByParticipant_WhenParticipantNotFound_ShouldThrowException() {
    // Arrange
    when(participantService.getParticipantByName(anyString()))
        .thenThrow(new RuntimeException("Participant not found"));

    // Act & Assert
    assertThrows(RuntimeException.class, () -> recipeService.getRecipeByParticipant("Nonexistent"));
  }

  @Test
  void getRecipeByParticipant_WhenRecipeNotFound_ShouldThrowException() {
    // Arrange
    when(participantService.getParticipantByName(anyString())).thenReturn(testParticipant);
    when(recipeRepository.findByAuthor(anyString())).thenReturn(null);

    // Act & Assert
    assertThrows(RuntimeException.class, () -> recipeService.getRecipeByParticipant("Chef John"));
  }

  @Test
  void getRecipeByAutor_WhenAuthorNotFound_ShouldThrowException() {
    // Arrange
    when(chefRepository.findByFullName(anyString())).thenReturn(null);
    when(viewerRepository.findByFullName(anyString())).thenReturn(null);

    // Act & Assert
    assertThrows(RuntimeException.class, () -> recipeService.getRecipeByAutor("Nonexistent"));
  }

  @Test
  void getRecipeByNumber_WhenNotFound_ShouldThrowException() {
    // Arrange
    when(recipeRepository.findByNum(anyInt())).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(RuntimeException.class, () -> recipeService.getRecipeByNumber(999));
  }

  @Test
  void getRecipesBySeason_WhenNoParticipants_ShouldReturnEmptyList() {
    // Arrange
    when(participantService.getParticipantsBySeason(anyInt())).thenReturn(Collections.emptyList());

    // Act
    List<RecipeResponseDto> result = recipeService.getRecipesBySeason(999);

    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void updateRecipe_WhenRecipeNotFound_ShouldThrowException() {
    // Arrange
    when(recipeRepository.findById(anyString())).thenReturn(Optional.empty());
    RecipeDto updateDto =
        new RecipeDto("new_title", "Updated Chef", "new1, new2", "New Step 1; New Step 2");

    // Act & Assert
    assertThrows(
        RuntimeException.class, () -> recipeService.updateRecipe("nonexistent", updateDto));
  }

  @Test
  void deleteRecipe_WhenErrorOccurs_ShouldThrowException() {
    // Arrange
    doThrow(new RuntimeException("Database error")).when(recipeRepository).deleteById(anyString());

    // Act & Assert
    assertThrows(RuntimeException.class, () -> recipeService.deleteRecipe("1"));
  }

  @Test
  void updateRecipe_ShouldUpdateExistingRecipe() {
    // Arrange
    RecipeDto updateDto =
        new RecipeDto("title", "Updated Chef", "new1, new2", "New Step 1; New Step 2");
    when(recipeRepository.findById(anyString())).thenReturn(Optional.of(testRecipe));
    when(recipeRepository.save(any(Recipe.class))).thenReturn(testRecipe);

    // Act
    Recipe result = recipeService.updateRecipe("1", updateDto);

    // Assert
    assertNotNull(result);
    verify(recipeRepository, times(1)).save(any(Recipe.class));
  }

  @Test
  void deleteRecipe_ShouldDeleteRecipe() {
    // Arrange
    doNothing().when(recipeRepository).deleteById(anyString());

    // Act & Assert
    assertDoesNotThrow(() -> recipeService.deleteRecipe("1"));
    verify(recipeRepository, times(1)).deleteById("1");
  }
}
