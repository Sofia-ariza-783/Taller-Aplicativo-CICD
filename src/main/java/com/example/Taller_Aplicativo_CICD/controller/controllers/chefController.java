package com.example.Taller_Aplicativo_CICD.controller.controllers;

import com.example.Taller_Aplicativo_CICD.controller.dtos.ChefDto;
import com.example.Taller_Aplicativo_CICD.model.models.Chef;
import com.example.Taller_Aplicativo_CICD.model.services.ChefService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing Chef entities. Provides endpoints for creating, retrieving, and
 * deleting chefs.
 */
@RestController
@RequestMapping("/chef")
@AllArgsConstructor
public class chefController {
  private final ChefService chefService;

  /**
   * Creates a new chef with the provided details.
   *
   * @param chefDto the chef data transfer object containing chef details
   * @return ResponseEntity containing the created chef and HTTP status 201 if successful, or HTTP
   *     status 400 if the request is invalid
   */
  @PostMapping
  public ResponseEntity<Chef> createChef(@RequestBody ChefDto chefDto) {
    try {
      Chef newChef = chefService.createChef(chefDto);
      return new ResponseEntity<>(newChef, HttpStatus.CREATED);
    } catch (RuntimeException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Retrieves a chef by their unique identifier.
   *
   * @param id the unique identifier of the chef to retrieve
   * @return ResponseEntity containing the chef if found (HTTP 200), or HTTP status 404 if no chef
   *     with the given ID exists
   */
  @GetMapping("/{id}")
  public ResponseEntity<Chef> getChefById(@PathVariable String id) {
    try {
      Chef chef = chefService.getChefById(id);
      return ResponseEntity.ok(chef);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Deletes a chef by their ID.
   *
   * @param id The ID of the chef to delete
   * @return ResponseEntity with HTTP status 204 (No Content) if successful, or HTTP status 404 (Not
   *     Found) if the chef doesn't exist
   */
  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a chef", description = "Deletes a chef by their ID")
  public ResponseEntity<Void> deleteChef(@PathVariable String id) {
    try {
      chefService.deleteChef(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
