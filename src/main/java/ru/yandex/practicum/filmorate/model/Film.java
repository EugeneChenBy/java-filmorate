package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Builder
@Data
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Duration duration;
    private MPA mpa;
    private int likes;
    private List<Genre> genres;

    public Film(int id, String name, String description, LocalDate releaseDate, Duration duration, MPA mpa, int likes, List<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.likes = likes;
        this.genres = genres;
    }

    @JsonProperty("mpa")
    private void unpackMPA(Map<String,String> mpa) {
        int mpaId = Integer.parseInt(mpa.get("id"));
        String mpaName = mpa.get("name");
        this.mpa = new MPA(mpaId, mpaName);
        log.debug(mpaId + " " + mpaName + " " + mpa.toString());
    }

    @JsonProperty("genres")
    private void unpackGenres(List<Map<String, String>> genres) {
        if (genres.size() > 0) {
            this.genres = new ArrayList<>();
            for(Map<String, String> genreRow : genres) {
                int genreId = Integer.parseInt(genreRow.get("id"));
                String genreName = genreRow.get("name");
                this.genres.add(new Genre(genreId, genreName));
            }
        }
        log.debug(this.genres.toString());
    }
}
