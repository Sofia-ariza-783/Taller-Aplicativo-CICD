package com.example.Taller_Aplicativo_CICD.model.services;

import com.example.Taller_Aplicativo_CICD.controller.dtos.ChefDto;
import com.example.Taller_Aplicativo_CICD.model.models.Chef;
import com.example.Taller_Aplicativo_CICD.model.repositories.ChefRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing Chef entities.
 * Handles business logic for chef-related operations including creation, retrieval, and deletion.
 */
@Service
@AllArgsConstructor
public class ChefService {
    private final ChefRepository chefRepository;
    private static final Logger logger = LoggerFactory.getLogger(ChefService.class);

    /**
     * Creates a new chef with the provided details.
     *
     * @param chefDto the data transfer object containing chef information
     * @return the created Chef entity
     * @throws RuntimeException if a chef with the same name already exists or if there's an error during creation
     */
    public Chef createChef(ChefDto chefDto) {
        Chef existingChef = chefRepository.findByFullName(chefDto.fullName());
        if (existingChef != null) {
            logger.error("Chef with name {} already exists", chefDto.fullName());
            throw new RuntimeException("Chef with name " + chefDto.fullName() + " already exists");
        }

        String id = UUID.randomUUID().toString();
        Chef chef = new Chef(id,chefDto.fullName());
        try {
            return chefRepository.save(chef);
        } catch (Exception e) {
            logger.error("Error creating chef: {} ", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a chef by their unique identifier.
     *
     * @param id the unique identifier of the chef to retrieve
     * @return the found Chef entity
     * @throws RuntimeException if no chef exists with the given ID
     */
    public Chef getChefById(String id) {
        Optional<Chef> chef = chefRepository.findById(id);
        if (chef.isEmpty()) {
            logger.error("Chef with name {} does not exists", id);
            throw new RuntimeException("Chef with name " + id + " does not exists");
        }
        return chef.get();
    }

    /**
     * Deletes a chef by their unique identifier.
     *
     * @param id the unique identifier of the chef to delete
     * @throws RuntimeException if there's an error during deletion
     */
    public void deleteChef(String id) {
        try {
            chefRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting chef: {} ", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
