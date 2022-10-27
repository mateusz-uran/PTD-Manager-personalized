package io.github.mateuszuran.ptdmanagerpersonalized.controller;

import io.github.mateuszuran.ptdmanagerpersonalized.converter.TripConverter;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.TripRequestDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.TripRequestListDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.TripResponseDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Trip;
import io.github.mateuszuran.ptdmanagerpersonalized.service.TripService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/api/trip")
@RestController
public class TripController {
    private final TripService service;
    private final TripConverter converter;

    public TripController(final TripService service, final TripConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TripResponseDTO>> saveTripList(@Valid @RequestBody TripRequestListDTO tripDto, @RequestParam Long id) {
        var tripList = tripDto.getTripList().stream()
                .map(converter::tripRequestDtoConvertToEntity)
                .collect(Collectors.toList());
        var result = service.saveTrip(tripList, id);
        return ResponseEntity.ok()
                .body(converter.tripListConvertToResponseDto(result));
    }

    @GetMapping(value = "/sort", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TripResponseDTO>> getAllSorted(@Valid @RequestParam Long id) {
        var list = service.getSortedTipsList(id);
        return ResponseEntity.ok()
                .body(converter.tripListConvertToResponseDto(list));
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TripResponseDTO>> getAll(@Valid @RequestParam Long id) {
        var list = service.getTripsList(id);
        return ResponseEntity.ok()
                .body(converter.tripListConvertToResponseDto(list));
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TripResponseDTO> findTrip(@RequestParam Long id) {
        var result = service.getSingleTrip(id);
        return ResponseEntity.ok(converter.tripConvertToResponseDto(result));
    }

    @PatchMapping(value = "/edit")
    public ResponseEntity<?> edit(@Valid @RequestParam Long id, @RequestBody TripRequestDTO tripDto) {
        Trip trip = converter.tripRequestDtoConvertToEntity(tripDto);
        service.editTrip(id, trip);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> delete(@RequestParam Long id) {
        service.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }
}
