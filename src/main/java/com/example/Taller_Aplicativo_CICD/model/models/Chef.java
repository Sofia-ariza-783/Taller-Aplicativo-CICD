package com.example.Taller_Aplicativo_CICD.model.models;

import com.example.Taller_Aplicativo_CICD.model.models.enums.Role;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a Chef entity in the system.
 * This class extends the Cooker class and is used to model users with chef privileges.
 * Chefs have the ability to create and manage recipes in the system.
 */
@Document(collection = "Chefs")
public class Chef extends Cooker {

    /**
     * Constructs a new Chef with the specified ID and full name.
     * The role is automatically set to CHEF.
     *
     * @param id the unique identifier for the chef
     * @param fullName the full name of the chef
     * @see Role#CHEF
     */
    public Chef(String id, String fullName) {
        super(id, fullName, Role.CHEF);
    }
}
