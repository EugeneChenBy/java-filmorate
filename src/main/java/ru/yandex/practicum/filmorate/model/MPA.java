package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MPA {
    private int id;
    @Nullable
    private String name;

    public MPA(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public MPA(int id) {
        this.id = id;
        this.name = null;
    }
}
