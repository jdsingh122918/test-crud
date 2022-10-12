package dev.fermatsolutions.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Genre {

    @Id
    @SequenceGenerator(name = "genre_seq", sequenceName = "genre_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genre_seq")
    private Long id;

    @NotNull
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
