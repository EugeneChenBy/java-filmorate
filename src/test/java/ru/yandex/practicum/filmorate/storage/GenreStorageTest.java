package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreStorageTest {
    @Autowired
    GenreDBStorage storage;

    static List<Genre> etalon;

    @BeforeAll
    static void beforeAll() {
        etalon = List.of(new Genre(1, "Комедия"),
                new Genre(2, "Драма"),
                new Genre(3, "Мультфильм"),
                new Genre(4, "Триллер"),
                new Genre(5, "Документальный"),
                new Genre(6, "Боевик")
        );
    }

    @Test
    void shouldGetAll() {
        List<Genre> real = storage.getGenresList();

        assertEquals(etalon, real, "Данные по списку жанров не равны!");
    }

    @Test
    void shouldGetById() {
        Genre genre = storage.getGenreById(3);

        assertEquals(etalon.get(2), genre, "Запрошенное значение жанра не равно ожидаемому");
    }

    @Test
    void shouldThrowGetByWrongId() {
        try {
            Genre genre = storage.getGenreById(10);
        } catch (Exception e) {
            assertEquals("Жанр с id = 10 не найден", e.getMessage(), "Ошибка запроса несуществующего жанра не отловлена");
        }
    }
}
