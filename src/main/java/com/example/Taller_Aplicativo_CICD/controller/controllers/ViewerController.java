package com.example.Taller_Aplicativo_CICD.controller.controllers;

import com.example.Taller_Aplicativo_CICD.controller.dtos.ViewerDto;
import com.example.Taller_Aplicativo_CICD.model.models.Viewer;
import com.example.Taller_Aplicativo_CICD.model.services.ViewerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing Viewer entities.
 * Provides endpoints for creating, retrieving, and deleting viewers.
 */

@RestController
@RequestMapping("/viewer")
@AllArgsConstructor
/**
 * Controller class for handling viewer-related HTTP requests.
 */
public class ViewerController {
    private final ViewerService viewerService;

    /**
     * Creates a new viewer with the provided details.
     *
     * @param viewerDto the viewer data transfer object containing viewer details
     * @return ResponseEntity containing the created viewer and HTTP status 201 if successful,
     *         or HTTP status 400 if the request is invalid
     */
    @PostMapping
    public ResponseEntity<Viewer> createViewer(@RequestBody ViewerDto viewerDto) {
        try {
            Viewer newViewer = viewerService.createViewer(viewerDto);
            return new ResponseEntity<>(newViewer, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieves a viewer by their full name.
     *
     * @param fullName the full name of the viewer to retrieve
     * @return ResponseEntity containing the viewer if found (HTTP 200),
     *         or HTTP status 404 if no viewer with the given name exists
     */
    @GetMapping("/{fullName}")
    public ResponseEntity<Viewer> getViewerByName(@PathVariable String fullName) {
        try {
            Viewer viewer = viewerService.getViewerByName(fullName);
            return ResponseEntity.ok(viewer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a viewer by their ID.
     *
     * @param id the ID of the viewer to delete
     * @return ResponseEntity with HTTP status 204 (No Content) if successful,
     *         or HTTP status 404 (Not Found) if the viewer doesn't exist
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a viewer", description = "Deletes a viewer by their ID")
    public ResponseEntity<Void> deleteViewer(@PathVariable String id) {
        try {
            viewerService.deleteViewer(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
