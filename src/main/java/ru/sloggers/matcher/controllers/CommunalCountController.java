package ru.sloggers.matcher.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sloggers.matcher.services.CommunalCountService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("counter")
public class CommunalCountController {

    private final CommunalCountService communalCountService;

    @GetMapping("check")
    public Boolean existsByOldNumberAndNewNumber(@RequestParam String oldNumber, @RequestParam String newNumber) {
        return communalCountService.existsByOldNumberAndNewNumber(oldNumber, newNumber);
    }
}
