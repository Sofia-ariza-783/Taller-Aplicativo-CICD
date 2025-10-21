package com.example.Taller_Aplicativo_CICD.model.models;

import com.example.Taller_Aplicativo_CICD.model.models.enums.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Participants")
@EqualsAndHashCode(callSuper = true)
public class Participant extends Cooker {
  private int season;

  public Participant() {
    super();
  }

  public Participant(String id, String fullName, int season) {
    super(id, fullName, Role.PARTICIPANT);
    this.season = season;
  }

  public Participant(String id, String fullName, Role role, int season) {
    super(id, fullName, role);
    this.season = season;
  }
}
