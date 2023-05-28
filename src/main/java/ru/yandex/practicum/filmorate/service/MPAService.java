package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPADBStorage;

import java.util.List;

@Service
public class MPAService {
    private final MPADBStorage mpaDBStorage;

    @Autowired
    public MPAService(MPADBStorage mpaDBStorage) {
        this.mpaDBStorage = mpaDBStorage;
    }

    public List<MPA> getAllMPAs() {
        return mpaDBStorage.getMPAsList();
    };

    public MPA getMPAById(int id) {
        return mpaDBStorage.getMPAById(id);
    };
}
