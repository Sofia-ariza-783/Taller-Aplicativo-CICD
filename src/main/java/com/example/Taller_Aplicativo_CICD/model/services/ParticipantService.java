package com.example.Taller_Aplicativo_CICD.model.services;

import com.example.Taller_Aplicativo_CICD.controller.dtos.ParticipantDto;
import com.example.Taller_Aplicativo_CICD.model.models.Participant;
import com.example.Taller_Aplicativo_CICD.model.repositories.ParticipantRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service class for managing Participant entities.
 * Handles business logic for participant-related operations including creation, retrieval, and deletion.
 */
@Service
@AllArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private static final Logger logger = LoggerFactory.getLogger(ParticipantService.class);

    /**
     * Creates a new participant with the provided details.
     *
     * @param participantDto the data transfer object containing participant information
     * @return the created Participant entity
     * @throws RuntimeException if a participant with the same name already exists or if there's an error during creation
     */
    public Participant createParticipant(ParticipantDto participantDto) {
        Participant existingParticipant = participantRepository.findByFullName(participantDto.fullName());
        if (existingParticipant != null) {
            logger.error("Participant with name {} already exists", participantDto.fullName());
            throw new RuntimeException("Participant with name " + participantDto.fullName() + " already exists");
        }

        String id = UUID.randomUUID().toString();
        Participant participant = new Participant(id,participantDto.fullName(), participantDto.season());
        try {
            return participantRepository.save(participant);
        } catch (Exception e) {
            logger.error("Error creating participant: {} ", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a participant by their full name.
     *
     * @param fullName the full name of the participant to retrieve
     * @return the found Participant entity
     * @throws RuntimeException if no participant exists with the given name
     */
    public Participant getParticipantByName(String fullName) {
        Participant participant = participantRepository.findByFullName(fullName);
        if (participant == null) {
            logger.error("Participant with name {} does not exists", fullName);
            throw new RuntimeException("Participant with name " + fullName + " does not exists");
        }
        return participant;
    }

    /**
     * Retrieves a participant by their unique identifier.
     *
     * @param id the unique identifier of the participant to retrieve
     * @return the found Participant entity
     * @throws RuntimeException if no participant exists with the given ID
     */
    public Participant getParticipantById(String id) {
        return participantRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Participant with id {} does not exist", id);
                    return new RuntimeException("Participant with id " + id + " does not exist");
                });
    }

    /**
     * Deletes a participant by their unique identifier.
     *
     * @param id the unique identifier of the participant to delete
     * @throws RuntimeException if there's an error during deletion
     */
    public void deleteParticipant(String id) {
        try {
            participantRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting participant: {} ", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves all participants for a specific season.
     *
     * @param season the season number to filter participants by
     * @return a list of Participant entities for the specified season
     * @throws RuntimeException if there's an error retrieving the participants
     */
    public List<Participant> getParticipantsBySeason(int season) {
        try{
            return participantRepository.findBySeason(season);
        }catch (Exception e){
            logger.error("Error getting participants by season: {} ", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
