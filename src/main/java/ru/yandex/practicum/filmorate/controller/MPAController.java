package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.List;

@RestController
@Slf4j
public class MPAController {
    private final MPAService mpaService;

    @Autowired
    public MPAController(MPAService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/mpa")
    public List<MPA> getMPAs() {
        log.info("Получен GET-запрос списка всех MPA-рейтингов");

        return mpaService.getAllMPAs();
    }

    @GetMapping("/mpa/{id}")
    public MPA findById(@PathVariable("id") int mpaId) {
        log.info("Получен GET-запрос на получение MPA-рейтинга - {}", mpaId);

        return mpaService.getMPAById(mpaId);
    }
}
