package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Genre {
    private int id;
    @Nullable
    private String name;

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
