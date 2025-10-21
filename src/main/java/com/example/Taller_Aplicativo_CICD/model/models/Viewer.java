package com.example.Taller_Aplicativo_CICD.model.models;

import com.example.Taller_Aplicativo_CICD.model.models.enums.Role;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a Viewer entity in the system.
 * This class extends the Cooker class and is used to model users with viewer privileges.
 * Viewers have read-only access to the system's resources.
 *
 */
@Document(collection = "Viewers")
public class Viewer extends Cooker {
    
    /**
     * Constructs a new Viewer with the specified ID and full name.
     * The role is automatically set to VIEWER.
     *
     * @param id the unique identifier for the viewer
     * @param fullName the full name of the viewer
     * @see Role#VIEWER
     */
    public Viewer(String id, String fullName) {
        super(id, fullName, Role.VIEWER);
    }
}
