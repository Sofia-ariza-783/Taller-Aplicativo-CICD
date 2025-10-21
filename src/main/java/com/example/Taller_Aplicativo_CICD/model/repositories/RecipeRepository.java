package com.example.Taller_Aplicativo_CICD.model.repositories;

import com.example.Taller_Aplicativo_CICD.model.models.Recipe;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
  @Query("{author: ?0}")
  Recipe findByAuthor(String authorName);

  Optional<Recipe> findByNum(int number);

  @Query("{'ingredients': {$in: [?0]}}")
  List<Recipe> findByIngredient(String ingredient);
}
