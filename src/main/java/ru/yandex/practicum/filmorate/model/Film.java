package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

@Builder
@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
    private MPA mpa;
    private int likes;

    public Film(int id, String name, String description, LocalDate releaseDate, Duration duration, MPA mpa, int likes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.likes = likes;
    }

    public Film(int id, String name, String description, LocalDate releaseDate, Duration duration, MPA mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.likes = 0;
    }

    @JsonProperty("mpa")
    private void unpackNested(Map<String,Object> mpa) {
        int mpaId = (int)mpa.get("id");
        this.mpa = new MPA(mpaId, null);
    }
}
