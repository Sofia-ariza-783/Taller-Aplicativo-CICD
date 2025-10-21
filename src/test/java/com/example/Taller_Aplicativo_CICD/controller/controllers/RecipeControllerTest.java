package com.example.Taller_Aplicativo_CICD.controller.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.example.Taller_Aplicativo_CICD.controller.dtos.RecipeDto;
import com.example.Taller_Aplicativo_CICD.controller.dtos.RecipeResponseDto;
import com.example.Taller_Aplicativo_CICD.model.models.Recipe;
import com.example.Taller_Aplicativo_CICD.model.services.RecipeService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class RecipeControllerTest {

  @Mock private RecipeService recipeService;

  @InjectMocks private RecipeController recipeController;

  private Recipe testRecipe;
  private RecipeDto testRecipeDto;
  private RecipeResponseDto testResponseDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    testRecipe =
        new Recipe(
            "1",
            "Chef John",
            1,
            Arrays.asList("ingredient1", "ingredient2"),
            Arrays.asList("Step 1", "Step 2"));

    testRecipeDto = new RecipeDto("Chef John", "ingredient1, ingredient2", "Step 1; Step 2");

    testResponseDto =
        new RecipeResponseDto(
            "1",
            "Chef John",
            Arrays.asList("ingredient1", "ingredient2"),
            Arrays.asList("Step 1", "Step 2"),
            1);
  }

  @Test
  void createRecipe_ShouldReturnCreatedRecipe() {
    when(recipeService.createRecipe(any(RecipeDto.class))).thenReturn(testRecipe);

    ResponseEntity<Recipe> response = recipeController.createRecipe(testRecipeDto);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Chef John", response.getBody().getAuthor());
  }

  @Test
  void getRecipeByParticipant_ShouldReturnRecipe() {
    when(recipeService.getRecipeByParticipant(anyString())).thenReturn(testResponseDto);

    ResponseEntity<RecipeResponseDto> response = recipeController.getRecipeByParticipant("John");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Chef John", response.getBody().author());
  }

  @Test
  void getRecipeByChef_ShouldReturnRecipe() {
    when(recipeService.getRecipeByAutor(anyString())).thenReturn(testRecipe);

    ResponseEntity<Recipe> response = recipeController.getRecipeByChef("Chef John");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Chef John", response.getBody().getAuthor());
  }

  @Test
  void getAllRecipes_ShouldReturnAllRecipes() {
    List<Recipe> recipes = Arrays.asList(testRecipe);
    when(recipeService.getAllRecipes()).thenReturn(recipes);

    ResponseEntity<List<Recipe>> response = recipeController.getAllRecipes();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void getAllRecipes_WhenNoRecipes_ShouldReturnEmptyList() {
    when(recipeService.getAllRecipes()).thenReturn(Collections.emptyList());

    ResponseEntity<List<Recipe>> response = recipeController.getAllRecipes();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isEmpty());
  }

  @Test
  void getRecipeByNumber_ShouldReturnRecipe() {
    when(recipeService.getRecipeByNumber(anyInt())).thenReturn(testRecipe);

    ResponseEntity<Recipe> response = recipeController.getRecipeByNumber(1);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().getNum());
  }

  @Test
  void getRecipeBySeason_ShouldReturnRecipes() {
    List<RecipeResponseDto> recipes = Arrays.asList(testResponseDto);
    when(recipeService.getRecipesBySeason(anyInt())).thenReturn(recipes);

    ResponseEntity<List<RecipeResponseDto>> response = recipeController.getRecipeBySeason(1);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void getRecipeBySeason_WhenNoRecipes_ShouldReturnEmptyList() {
    when(recipeService.getRecipesBySeason(anyInt())).thenReturn(Collections.emptyList());

    ResponseEntity<List<RecipeResponseDto>> response = recipeController.getRecipeBySeason(999);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isEmpty());
  }

  @Test
  void getRecipeByIngredient_ShouldReturnRecipes() {
    List<Recipe> recipes = Arrays.asList(testRecipe);
    when(recipeService.getRecipesByIngredient(anyString())).thenReturn(recipes);

    ResponseEntity<List<Recipe>> response = recipeController.getRecipeByIngredient("ingredient1");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
  }

  @Test
  void getRecipeByIngredient_WhenNoRecipes_ShouldReturnEmptyList() {
    when(recipeService.getRecipesByIngredient(anyString())).thenReturn(Collections.emptyList());

    ResponseEntity<List<Recipe>> response = recipeController.getRecipeByIngredient("nonexistent");

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isEmpty());
  }

  @Test
  void updateRecipe_ShouldReturnUpdatedRecipe() {
    when(recipeService.updateRecipe(anyString(), any(RecipeDto.class))).thenReturn(testRecipe);

    ResponseEntity<Recipe> response = recipeController.updateRecipe("1", testRecipeDto);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Chef John", response.getBody().getAuthor());
  }

  @Test
  void searchByIngredient_WithExistingIngredient_ShouldReturnMatchingRecipes() {
    // Arrange
    Recipe recipe1 =
        new Recipe(
            "1",
            "Chef John",
            1,
            Arrays.asList("ingredient1", "ingredient2"),
            Arrays.asList("Step 1", "Step 2"));

    Recipe recipe2 =
        new Recipe(
            "2",
            "Chef Maria",
            2,
            Arrays.asList("ingredient1", "ingredient3"),
            Arrays.asList("Step A", "Step B"));

    List<Recipe> expectedRecipes = Arrays.asList(recipe1, recipe2);

    when(recipeService.getRecipesByIngredient("ingredient1")).thenReturn(expectedRecipes);

    // Act
    ResponseEntity<List<Recipe>> response = recipeController.getRecipeByIngredient("ingredient1");

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(2, response.getBody().size());
    assertTrue(
        response.getBody().stream()
            .allMatch(recipe -> recipe.getIngredients().contains("ingredient1")));
    verify(recipeService, times(1)).getRecipesByIngredient("ingredient1");
  }

  @Test
  void getRecipeByNumber_WhenRecipeDoesNotExist_ShouldReturnNotFound() {
    // Arrange
    when(recipeService.getRecipeByNumber(999))
        .thenThrow(new RuntimeException("Recipe with number 999 does not exist"));

    // Act
    ResponseEntity<Recipe> response = recipeController.getRecipeByNumber(999);

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
    verify(recipeService, times(1)).getRecipeByNumber(999);
  }
}
