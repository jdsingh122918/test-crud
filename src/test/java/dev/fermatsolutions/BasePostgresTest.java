package dev.fermatsolutions;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;
import java.util.Objects;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BasePostgresTest implements TestPropertyProvider {

    static GenericContainer<?> postgresContainer;

    @Inject
    @Client("/")
    HttpClient httpClient;

    void startPostgres() {
        if (Objects.isNull(postgresContainer)) {
            postgresContainer = new GenericContainer<>(DockerImageName.parse("postgres:alpine"))
                    .withExposedPorts(5432)
                    .withEnv("POSTGRES_USER", "jdsingh")
                    .withEnv("POSTGRES_PASSWORD", "password")
                    .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\s", 2));
        }
        if (!postgresContainer.isRunning()) {
            postgresContainer.start();
        }
    }

    String getDbUri() {
        if (Objects.isNull(postgresContainer) || !postgresContainer.isRunning()) {
            startPostgres();
        }
        return "jdbc:postgresql://localhost:" + postgresContainer.getMappedPort(5432) + "/postgres";
    }


    @Override
    @NonNull
    public Map<String, String> getProperties() {
        return CollectionUtils.mapOf(
                "jpa.default.properties.hibernate.connection.url", getDbUri(),
                "datasources.default.url", getDbUri());
    }

    @BeforeEach
    public void initiateMigration() {
        Flyway.configure()
                .locations(Location.FILESYSTEM_PREFIX + "src/test/resources/db/migrations")
                .dataSource(getDbUri(), "jdsingh", "password")
                .load()
                .migrate();
    }

    @AfterEach
    public void dropMigrations() {
        Flyway.configure()
                .locations(Location.FILESYSTEM_PREFIX + "src/test/resources/db/migrations")
                .dataSource(getDbUri(), "jdsingh", "password")
                .load()
                .clean();
    }

    @AfterAll
    public static void stop() {
        postgresContainer.close();
    }
}
