package com.example.Taller_Aplicativo_CICD.model.repositories;

import com.example.Taller_Aplicativo_CICD.model.models.Chef;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ChefRepository extends MongoRepository<Chef, String> {
    @Query("{fullName: ?0}")
    Chef findByFullName(String s);
}
