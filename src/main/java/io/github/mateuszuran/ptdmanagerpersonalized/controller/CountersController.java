package io.github.mateuszuran.ptdmanagerpersonalized.controller;

import io.github.mateuszuran.ptdmanagerpersonalized.converter.CountersConverter;
import io.github.mateuszuran.ptdmanagerpersonalized.service.CountersService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/counters")
@RestController
public class CountersController {
    private final CountersService service;
    private final CountersConverter converter;

    public CountersController(final CountersService service, final CountersConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @PutMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCounters(@RequestParam Long id) {
        var result = service.updateCounters(id);
        return ResponseEntity.ok().body(
                converter.countersResponseDtoConvertToEntity(result));
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> showCounters(@RequestParam Long id) {
        var result = service.getCountersFromCard(id);
        return ResponseEntity.ok().body(
                converter.countersResponseDtoConvertToEntity(result));
    }
}
