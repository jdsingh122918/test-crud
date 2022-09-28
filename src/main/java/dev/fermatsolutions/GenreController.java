package dev.fermatsolutions;

import dev.fermatsolutions.domain.Genre;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;

@Controller("/genres")
public class GenreController {

    protected final GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Get("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Mono<Genre> show(@PathVariable(name = "id") Long id) {
        return genreRepository.findById(id);
    }

    @Put
    public Mono<HttpResponse<Genre>> update(@Body @Valid GenreUpdateCommand command) {
        return genreRepository.update(command.id(), command.name())
                .map(genre -> HttpResponse
                        .<Genre>noContent()
                        .header(HttpHeaders.LOCATION, URI.create("/genres/" + command.id()).getPath()));
    }

    @Get("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Mono<List<Genre>> list(@Valid Pageable pageable) {
        return genreRepository.findAll(pageable)
                .map(Page::getContent);
    }

    @Post
    @Status(HttpStatus.CREATED)
    public Mono<HttpResponse<Genre>> save(@Body("name") @NotBlank String name) {
        return genreRepository.save(name)
                .map(genre -> HttpResponse
                        .<Genre>noContent()
                        .header(HttpHeaders.LOCATION, URI.create("/genres/" + genre.getId()).getPath()));
    }

    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    public Mono<HttpResponse<?>> delete(@PathVariable(name = "id") Long id) {
        return genreRepository.deleteById(id)
                .map(deleteId -> HttpResponse.noContent());
    }
}
