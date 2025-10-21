package com.example.Taller_Aplicativo_CICD.model.services;

import com.example.Taller_Aplicativo_CICD.controller.dtos.ViewerDto;
import com.example.Taller_Aplicativo_CICD.model.models.Viewer;
import com.example.Taller_Aplicativo_CICD.model.repositories.ViewerRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service class for managing Viewer entities. Handles business logic for viewer-related operations
 * including creation, retrieval, and deletion.
 */
@Service
@AllArgsConstructor
public class ViewerService {
  private final ViewerRepository viewerRepository;
  private static final Logger logger = LoggerFactory.getLogger(ViewerService.class);

  /**
   * Creates a new viewer with the provided details.
   *
   * @param viewerDto the data transfer object containing viewer information
   * @return the created Viewer entity
   * @throws RuntimeException if a viewer with the same name already exists or if there's an error
   *     during creation
   */
  public Viewer createViewer(ViewerDto viewerDto) {
    Viewer existingViewer = viewerRepository.findByFullName(viewerDto.fullName());
    if (existingViewer != null) {
      logger.error("Viewer with name {} already exists", viewerDto.fullName());
      throw new RuntimeException("Viewer with name " + viewerDto.fullName() + " already exists");
    }

    String id = UUID.randomUUID().toString();
    Viewer viewer = new Viewer(id, viewerDto.fullName());
    try {
      return viewerRepository.save(viewer);
    } catch (Exception e) {
      logger.error("Error creating viewer: {} ", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  /**
   * Retrieves a viewer by their full name.
   *
   * @param fullName the full name of the viewer to retrieve
   * @return the found Viewer entity
   * @throws RuntimeException if no viewer with the given name exists
   */
  public Viewer getViewerByName(String fullName) {
    Viewer viewer = viewerRepository.findByFullName(fullName);
    if (viewer == null) {
      logger.error("Viewer with name {} does not exists", fullName);
      throw new RuntimeException("Viewer with name " + fullName + " does not exists");
    }
    return viewer;
  }

  /**
   * Deletes a viewer by their unique identifier.
   *
   * @param id the unique identifier of the viewer to delete
   * @throws RuntimeException if there's an error during deletion
   */
  public void deleteViewer(String id) {
    try {
      viewerRepository.deleteById(id);
    } catch (Exception e) {
      logger.error("Error deleting viewer: {} ", e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
