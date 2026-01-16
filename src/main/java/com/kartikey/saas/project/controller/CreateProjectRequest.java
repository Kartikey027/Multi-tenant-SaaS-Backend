package com.kartikey.saas.project.controller;

import jakarta.validation.constraints.NotBlank;

public record CreateProjectRequest(@NotBlank String name) {
}
