package dev.fermatsolutions;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Introspected
public record GenreUpdateCommand(@NotNull Long id,
                                 @NotBlank String name) {
}
