package dev.fermatsolutions;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;

import static io.micronaut.context.env.Environment.TEST;

@Singleton
@Requires(notEnv = TEST)
public class Dataloader {

    private final String url;
    private final String username;
    private final String password;

    public Dataloader(@Property(name = "datasources.default.url") String url,
                      @Property(name = "datasources.default.username") String username,
                      @Property(name = "datasources.default.password") String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @EventListener
    @Async
    public void initiateMigration(final StartupEvent startupEvent) {
        Flyway.configure()
                .locations(Location.FILESYSTEM_PREFIX + "src/main/resources/db/migrations")
                .dataSource(url, username, password)
                .load()
                .migrate();
    }
}
