package com.undead4j.javalin.example.view.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * UserModel is a simple model for a user with a name and email with Jakarta Bean Validation annotations
 * for use with Undead's {@link com.undead4j.form.Form}.
 * @param name the name of the user
 * @param email the email of the user
 */
public record UserModel(@NotBlank String name, @NotBlank @Email String email) { }
