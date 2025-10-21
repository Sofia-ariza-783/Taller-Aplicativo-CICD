package com.example.Taller_Aplicativo_CICD.model.repositories;

import com.example.Taller_Aplicativo_CICD.model.models.Participant;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ParticipantRepository extends MongoRepository<Participant, String> {
  @Query("{fullName: ?0}")
  Participant findByFullName(String s);

  List<Participant> findBySeason(int season);
}
