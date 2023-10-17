package com.undead4j.javalin.example.view.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserModel(@NotBlank String name, @NotBlank @Email String email) {
}
