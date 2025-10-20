package com.example.Taller_Aplicativo_CICD.model.repositories;

import com.example.Taller_Aplicativo_CICD.model.models.Chef;
import com.example.Taller_Aplicativo_CICD.model.models.Participant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ParticipantRepository extends MongoRepository<Participant, String> {
    @Query("{fullName: ?0}")
    Participant findByFullName(String s);

    List<Participant> findBySeason(int season);
}
