package io.github.gadnex.datastarspringmvc.person;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Person(@NotBlank @Size(min = 3) String name, @NotBlank @Email String email) {}
