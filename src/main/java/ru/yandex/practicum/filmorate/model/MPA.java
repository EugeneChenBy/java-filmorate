package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.lang.Nullable;

@Data
public class MPA {
    private int id;
    @Nullable
    private final String name;

    public MPA(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public MPA(int id) {
        this.id = id;
        this.name = null;
    }
}
