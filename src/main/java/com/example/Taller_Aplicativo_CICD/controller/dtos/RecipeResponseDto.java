package com.example.Taller_Aplicativo_CICD.controller.dtos;

import java.util.List;

public record RecipeResponseDto(
    String id,String title, String author, List<String> ingredients, List<String> instructions, int season) {}
