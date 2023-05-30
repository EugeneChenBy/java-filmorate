package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MPAStorageTest {
    @Autowired
    MPADBStorage storage;

    static List<MPA> etalon;

    @BeforeAll
    static void BeforeAll() {
        etalon = List.of(new MPA(1, "G"),
                new MPA(2, "PG"),
                new MPA(3, "PG-13"),
                new MPA(4, "R"),
                new MPA(5, "NC-17")
                );
    }

    @Test
    void shouldGetAll() {
        List<MPA> real = storage.getMPAsList();

        assertEquals(etalon, real, "Данные по списку MPA-рейтингов не равны!");
    }

    @Test
    void shouldGetById() {
        MPA mpa = storage.getMPAById(3);

        assertEquals(etalon.get(2), mpa, "Запрошенное значение MPA-рейтинга не равно ожидаемому");
    }

    @Test
    void shouldThrowGetByWrongId() {
        try {
            MPA mpa = storage.getMPAById(7);
        } catch (Exception e) {
            assertEquals("MPA-рейтинг с id = 7 не найден", e.getMessage(), "Ошибка запроса несуществующего MPA-рейтинга не отловлена");
        }
    }
}
