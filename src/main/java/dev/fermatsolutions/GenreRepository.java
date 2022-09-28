package dev.fermatsolutions;

import dev.fermatsolutions.domain.Genre;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Repository
public interface GenreRepository extends ReactorPageableRepository<Genre, Long> {

    Mono<Genre> save(@NonNull @NotBlank String name);

    Mono<Long> update(@NonNull @NotNull @Id Long id, @NonNull @NotBlank String name);
}
