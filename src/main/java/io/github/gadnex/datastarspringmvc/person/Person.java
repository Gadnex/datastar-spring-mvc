package io.github.gadnex.datastarspringmvc.person;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Person(
    @NotBlank(message = "{Person.name.notBlank}") @Size(min = 3, message = "{Person.name.size}")
        String name,
    @NotBlank(message = "{Person.email.notBlank}") @Email(message = "{Person.email.email}")
        String email) {}
