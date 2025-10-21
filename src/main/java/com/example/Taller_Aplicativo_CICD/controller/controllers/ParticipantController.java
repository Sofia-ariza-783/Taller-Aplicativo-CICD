package com.example.Taller_Aplicativo_CICD.controller.controllers;

import com.example.Taller_Aplicativo_CICD.controller.dtos.ParticipantDto;
import com.example.Taller_Aplicativo_CICD.model.models.Participant;
import com.example.Taller_Aplicativo_CICD.model.services.ParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing Participant entities. Provides endpoints for creating, retrieving,
 * and deleting participants.
 */
@RestController
@RequestMapping("/participant")
@AllArgsConstructor
/** Controller class for handling participant-related HTTP requests. */
public class ParticipantController {
  private final ParticipantService participantService;

  /**
   * Creates a new participant with the provided details.
   *
   * @param participantDto the participant data transfer object containing participant details
   * @return ResponseEntity containing the created participant and HTTP status 201 if successful, or
   *     HTTP status 400 if the request is invalid
   */
  @PostMapping
  public ResponseEntity<Participant> createParticipant(@RequestBody ParticipantDto participantDto) {
    try {
      Participant newParticipant = participantService.createParticipant(participantDto);
      return new ResponseEntity<>(newParticipant, HttpStatus.CREATED);
    } catch (RuntimeException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Retrieves a participant by their full name.
   *
   * @param fullName the full name of the participant to retrieve
   * @return ResponseEntity containing the participant if found (HTTP 200), or HTTP status 404 if no
   *     participant with the given name exists
   */
  @GetMapping("/name/{fullName}")
  public ResponseEntity<Participant> getParticipantByName(@PathVariable String fullName) {
    try {
      Participant participant = participantService.getParticipantByName(fullName);
      return ResponseEntity.ok(participant);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Retrieves a participant by their unique identifier.
   *
   * @param id the unique identifier of the participant to retrieve
   * @return ResponseEntity containing the participant if found (HTTP 200), or HTTP status 404 if no
   *     participant with the given ID exists
   */
  @GetMapping("/id/{id}")
  public ResponseEntity<Participant> getParticipantById(@PathVariable String id) {
    try {
      Participant participant = participantService.getParticipantById(id);
      return ResponseEntity.ok(participant);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Deletes a participant by their ID.
   *
   * @param id the ID of the participant to delete
   * @return ResponseEntity with HTTP status 204 (No Content) if successful, or HTTP status 404 (Not
   *     Found) if the participant doesn't exist
   */
  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a participant", description = "Deletes a participant by their ID")
  public ResponseEntity<Void> deleteParticipant(@PathVariable String id) {
    try {
      participantService.deleteParticipant(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
