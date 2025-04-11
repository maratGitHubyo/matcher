package ru.sloggers.matcher.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.sloggers.matcher.services.RegistryParser;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("registry")
public class CounterRegistryController {

    private final RegistryParser registryParser;

    @PostMapping
    public Integer registryParse(@RequestParam MultipartFile file) {
        return registryParser.parse(file);
    }

}
