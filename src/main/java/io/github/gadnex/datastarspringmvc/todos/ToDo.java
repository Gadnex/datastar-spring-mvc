package io.github.gadnex.datastarspringmvc.todos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record ToDo(
    UUID id,
    @NotBlank(message = "{Todos.text.notBlank}") @Size(min = 3, message = "{Todos.text.size}")
        String text,
    boolean done) {}
