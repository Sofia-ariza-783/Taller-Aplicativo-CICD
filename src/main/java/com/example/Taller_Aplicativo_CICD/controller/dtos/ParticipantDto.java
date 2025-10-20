package com.example.Taller_Aplicativo_CICD.controller.dtos;

import com.example.Taller_Aplicativo_CICD.model.models.enums.Role;

public record ParticipantDto(
        String fullName,
        int season
) {}
