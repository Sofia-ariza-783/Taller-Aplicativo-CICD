package com.example.Taller_Aplicativo_CICD.model.models;

import com.example.Taller_Aplicativo_CICD.model.models.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cooker {
  @Id private String id;
  private String fullName;
  private Role role;
}
