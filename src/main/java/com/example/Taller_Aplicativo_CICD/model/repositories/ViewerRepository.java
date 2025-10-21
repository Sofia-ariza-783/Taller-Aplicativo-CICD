package com.example.Taller_Aplicativo_CICD.model.repositories;

import com.example.Taller_Aplicativo_CICD.model.models.Viewer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ViewerRepository extends MongoRepository<Viewer, String> {
  @Query("{fullName: ?0}")
  Viewer findByFullName(String s);
}
