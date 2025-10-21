package com.example.Taller_Aplicativo_CICD.model.services;

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
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service class for managing Recipe entities. Handles business logic for recipe-related operations
 * including creation, retrieval, updating, and deletion.
 */
@Service
@AllArgsConstructor
public class RecipeService {
  private final RecipeRepository recipeRepository;
  private final ParticipantService participantService;
  private static final Logger logger = LoggerFactory.getLogger(RecipeService.class);
  private final ChefRepository chefRepository;
  private final ViewerRepository viewerRepository;

  /**
   * Creates a new recipe with the provided details.
   *
   * @param recipeDto the data transfer object containing recipe information
   * @return the created Recipe entity
   * @throws RuntimeException if there's an error during recipe creation
   */
  public Recipe createRecipe(RecipeDto recipeDto) {
    String id = UUID.randomUUID().toString();

    List<String> ingredients =
        Arrays.stream(recipeDto.ingredients().split(",")).map(String::trim).toList();
    List<String> instructions =
        Arrays.stream(recipeDto.instructions().split(";")).map(String::trim).toList();

    int num = 1;
    List<Recipe> allRecipes = recipeRepository.findAll();
    if (!allRecipes.isEmpty()) {
      Recipe lastRecipe = allRecipes.get(allRecipes.size() - 1);
      num = lastRecipe.getNum() + 1;
    }

    Recipe recipe = new Recipe(id, recipeDto.author(), num, ingredients, instructions);
    try {
      return recipeRepository.save(recipe);
    } catch (Exception e) {
      logger.error("Error creating recipe: {} ", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  /**
   * Retrieves a recipe response DTO for a participant by their name.
   *
   * @param authorName the name of the participant/author
   * @return RecipeResponseDto containing recipe details and season information
   * @throws RuntimeException if no recipe exists for the given author
   */
  public RecipeResponseDto getRecipeByParticipant(String authorName) {
    Recipe recipe = recipeRepository.findByAuthor(authorName);
    Participant participant = participantService.getParticipantByName(authorName);
    int season = participant.getSeason();

    if (recipe == null) {
      logger.error("Recipe with author name {} does not exists", authorName);
      throw new RuntimeException("Recipe with author name " + authorName + " does not exists");
    }

    return new RecipeResponseDto(
        recipe.getId(),
        recipe.getAuthor(),
        recipe.getIngredients(),
        recipe.getInstructions(),
        season);
  }

  /**
   * Retrieves a recipe by the author's name. The author can be either a Chef or a Viewer.
   *
   * @param autorName the name of the author (Chef or Viewer)
   * @return the found Recipe entity
   * @throws RuntimeException if the author doesn't exist or has no recipes
   */
  public Recipe getRecipeByAutor(String autorName) {
    Chef chef = chefRepository.findByFullName(autorName);
    Viewer viewer = viewerRepository.findByFullName(autorName);

    if (chef == null && viewer == null) {
      logger.error("Author with name {} does not exist in Chef or Viewer collections", autorName);
      throw new RuntimeException("Author with name " + autorName + " does not exist");
    }

    Recipe recipe = recipeRepository.findByAuthor(autorName);
    if (recipe == null) {
      logger.error("Recipe with author name {} does not exist", autorName);
      throw new RuntimeException("Recipe with author name " + autorName + " does not exist");
    }
    return recipe;
  }

  /**
   * Retrieves all recipes in the system.
   *
   * @return a list of all Recipe entities
   * @throws RuntimeException if there's an error retrieving the recipes
   */
  public List<Recipe> getAllRecipes() {
    try {
      return recipeRepository.findAll();
    } catch (Exception e) {
      logger.error("Error getting all recipes: {}", e.getMessage());
      throw new RuntimeException("Error getting all recipes", e);
    }
  }

  /**
   * Retrieves a recipe by its unique number.
   *
   * @param number the recipe number to search for
   * @return the found Recipe entity
   * @throws RuntimeException if no recipe exists with the given number
   */
  public Recipe getRecipeByNumber(int number) {
    return recipeRepository
        .findByNum(number)
        .orElseThrow(
            () -> {
              logger.error("Recipe with number {} does not exist", number);
              return new RuntimeException("Recipe with number " + number + " does not exist");
            });
  }

  /**
   * Retrieves all recipes for a specific season.
   *
   * @param number the season number
   * @return a list of RecipeResponseDto objects for the specified season
   */
  public List<RecipeResponseDto> getRecipesBySeason(int number) {
    List<Participant> participants = participantService.getParticipantsBySeason(number);
    List<RecipeResponseDto> recipesBySeason = new ArrayList<>();
    for (Participant participant : participants) {
      try {
        recipesBySeason.add(getRecipeByParticipant(participant.getFullName()));
      } catch (Exception e) {
        logger.warn("Participant with name {} does not hace recipes", participant.getFullName());
      }
    }
    return recipesBySeason;
  }

  /**
   * Finds all recipes that contain a specific ingredient.
   *
   * @param ingredient the ingredient to search for
   * @return a list of recipes containing the specified ingredient
   */
  public List<Recipe> getRecipesByIngredient(String ingredient) {
    List<Recipe> recipesByIngrediente = recipeRepository.findByIngredient(ingredient);
    return recipesByIngrediente;
  }

  /**
   * Updates an existing recipe with new information.
   *
   * @param id the ID of the recipe to update
   * @param recipeDto the data transfer object containing updated recipe information
   * @return the updated Recipe entity
   * @throws RuntimeException if the recipe doesn't exist or there's an error during update
   */
  public Recipe updateRecipe(String id, RecipeDto recipeDto) {
    Optional<Recipe> currentRecipe = recipeRepository.findById(id);
    if (currentRecipe.isEmpty()) {
      logger.error("Recipe with author name {} does not exists", recipeDto.author());
      throw new RuntimeException(
          "Recipe with author name " + recipeDto.author() + " does not exists");
    }
    Recipe recipe = currentRecipe.get();

    if (recipeDto.author() != null) recipe.setAuthor(recipeDto.author());

    if (recipeDto.ingredients() != null) {
      List<String> ingredients = Arrays.stream(recipeDto.ingredients().split(",")).toList();
      recipe.setIngredients(ingredients);
    }

    if (recipeDto.instructions() != null) {
      List<String> instructions = Arrays.stream(recipeDto.instructions().split(";")).toList();
      recipe.setInstructions(instructions);
    }

    try {
      return recipeRepository.save(recipe);
    } catch (Exception e) {
      logger.error("Error updating recipe with autor: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  /**
   * Deletes a recipe by its ID.
   *
   * @param id the ID of the recipe to delete
   * @throws RuntimeException if there's an error during deletion
   */
  public void deleteRecipe(String id) {
    try {
      recipeRepository.deleteById(id);
    } catch (Exception e) {
      logger.error("Error deleting recipe: {} ", e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
