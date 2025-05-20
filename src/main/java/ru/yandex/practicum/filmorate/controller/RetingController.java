package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Reting;
import ru.yandex.practicum.filmorate.service.RetingService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class RetingController {

    private final RetingService retingService;

    @GetMapping
    public Collection<Reting> getRetings() {
        return retingService.getRatings();
    }

    @GetMapping("{id}")
    public Reting getRetingById(@PathVariable int id) {
        return retingService.getRatingById(id);
    }
}
