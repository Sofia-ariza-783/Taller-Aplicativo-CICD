package com.example.Taller_Aplicativo_CICD.controller.controllers;

import com.example.Taller_Aplicativo_CICD.controller.dtos.RecipeDto;
import com.example.Taller_Aplicativo_CICD.controller.dtos.RecipeResponseDto;
import com.example.Taller_Aplicativo_CICD.model.models.Recipe;
import com.example.Taller_Aplicativo_CICD.model.services.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing Recipe entities. Provides endpoints for creating, retrieving, and
 * managing recipes.
 */
@RestController
@RequestMapping("/recipe")
@AllArgsConstructor
public class RecipeController {
  private final RecipeService recipeService;

  /**
   * Creates a new recipe with the provided details.
   *
   * @param recipeDto the data transfer object containing recipe information
   * @return ResponseEntity containing the created Recipe and HTTP status 201 if successful, or HTTP
   *     status 400 if the request is invalid
   */
  @PostMapping
  @Operation(
      summary = "Create a new recipe",
      description = "Creates a new recipe with the provided details")
  public ResponseEntity<Recipe> createRecipe(@RequestBody RecipeDto recipeDto) {
    try {
      Recipe newRecipe = recipeService.createRecipe(recipeDto);
      return new ResponseEntity<>(newRecipe, HttpStatus.CREATED);
    } catch (RuntimeException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Retrieves a recipe by participant name along with their season information.
   *
   * @param authorName the name of the participant/author
   * @return ResponseEntity containing the RecipeResponseDto if found (HTTP 200), or HTTP status 404
   *     if not found
   */
  @GetMapping("/participant/{authorName}")
  @Operation(
      summary = "Get recipe by participant name",
      description = "Retrieves a recipe along with the participant's season")
  public ResponseEntity<RecipeResponseDto> getRecipeByParticipant(@PathVariable String authorName) {
    try {
      RecipeResponseDto recipe = recipeService.getRecipeByParticipant(authorName);
      return ResponseEntity.ok(recipe);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Retrieves a recipe by the chef's name.
   *
   * @param chefName the name of the chef
   * @return ResponseEntity containing the Recipe if found (HTTP 200), or HTTP status 404 if not
   *     found
   */
  @GetMapping("/chef/{chefName}")
  @Operation(
      summary = "Get recipe by chef name",
      description = "Retrieves a recipe by the chef's name")
  public ResponseEntity<Recipe> getRecipeByChef(@PathVariable String chefName) {
    try {
      Recipe recipe = recipeService.getRecipeByAutor(chefName);
      return ResponseEntity.ok(recipe);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Retrieves a recipe by the viewer's name.
   *
   * @param viewerName the name of the viewer
   * @return ResponseEntity containing the Recipe if found (HTTP 200), or HTTP status 404 if not
   *     found
   */
  @GetMapping("/viewer/{viewerName}")
  @Operation(
      summary = "Get recipe by viewer name",
      description = "Retrieves a recipe by the viewer's name")
  public ResponseEntity<Recipe> getRecipeByViewer(@PathVariable String viewerName) {
    try {
      Recipe recipe = recipeService.getRecipeByAutor(viewerName);
      return ResponseEntity.ok(recipe);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Retrieves all recipes in the system.
   *
   * @return ResponseEntity containing a list of all Recipes (HTTP 200), or HTTP status 500 if
   *     there's a server error
   */
  @GetMapping
  @Operation(summary = "Get all recipes", description = "Retrieves a list of all recipes")
  public ResponseEntity<List<Recipe>> getAllRecipes() {
    try {
      List<Recipe> recipes = recipeService.getAllRecipes();
      return ResponseEntity.ok(recipes);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Retrieves a recipe by its unique number.
   *
   * @param number the recipe number to search for
   * @return ResponseEntity containing the Recipe if found (HTTP 200), or HTTP status 404 if not
   *     found
   */
  @GetMapping("/number/{number}")
  @Operation(
      summary = "Get recipe by number",
      description = "Retrieves a recipe by its unique number")
  public ResponseEntity<Recipe> getRecipeByNumber(@PathVariable int number) {
    try {
      Recipe recipe = recipeService.getRecipeByNumber(number);
      return ResponseEntity.ok(recipe);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Retrieves all recipes for a specific season.
   *
   * @param season the season number to filter by
   * @return ResponseEntity containing a list of RecipeResponseDto for the specified season (HTTP
   *     200), or HTTP status 404 if no recipes found for the season
   */
  @GetMapping("/season/{season}")
  @Operation(
      summary = "Get recipes by season",
      description = "Retrieves all recipes for a specific season")
  public ResponseEntity<List<RecipeResponseDto>> getRecipeBySeason(@PathVariable int season) {
    try {
      List<RecipeResponseDto> recipes = recipeService.getRecipesBySeason(season);
      return ResponseEntity.ok(recipes);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Retrieves all recipes that contain a specific ingredient.
   *
   * @param ingredient the ingredient to search for
   * @return ResponseEntity containing a list of Recipes containing the ingredient (HTTP 200), or
   *     HTTP status 404 if no recipes found with the ingredient
   */
  @GetMapping("/ingredient/{ingredient}")
  @Operation(
      summary = "Get recipes by ingredient",
      description = "Retrieves all recipes containing a specific ingredient")
  public ResponseEntity<List<Recipe>> getRecipeByIngredient(@PathVariable String ingredient) {
    try {
      List<Recipe> recipes = recipeService.getRecipesByIngredient(ingredient);
      return ResponseEntity.ok(recipes);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Updates an existing recipe with new information.
   *
   * @param id the ID of the recipe to update
   * @param recipeDto the data transfer object containing updated recipe information
   * @return ResponseEntity containing the updated Recipe (HTTP 200), or HTTP status 404 if the
   *     recipe doesn't exist
   */
  @PutMapping("/{id}")
  @Operation(
      summary = "Update a recipe",
      description = "Updates an existing recipe with new information")
  public ResponseEntity<Recipe> updateRecipe(
      @PathVariable String id, @RequestBody RecipeDto recipeDto) {
    try {
      Recipe updatedRecipe = recipeService.updateRecipe(id, recipeDto);
      return ResponseEntity.ok(updatedRecipe);
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Deletes a recipe by its ID.
   *
   * @param id the ID of the recipe to delete
   * @return ResponseEntity with HTTP status 204 (No Content) if successful, or HTTP status 500 if
   *     there's a server error
   */
  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a recipe", description = "Deletes a recipe by its ID")
  public ResponseEntity<Void> deleteRecipe(@PathVariable String id) {
    try {
      recipeService.deleteRecipe(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
