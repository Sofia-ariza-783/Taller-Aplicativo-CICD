package com.example.Taller_Aplicativo_CICD.model.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a Recipe entity in the system. Contains information about a recipe including its
 * author, ingredients, and preparation instructions.
 */
@Data
@AllArgsConstructor
@Document(collection = "Recipes")
public class Recipe {
  @Id private String id;
  private String title;
  private String author;
  private int num;
  private List<String> ingredients;
  private List<String> instructions;
}
