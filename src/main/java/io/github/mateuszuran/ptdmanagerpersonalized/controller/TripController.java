package io.github.mateuszuran.ptdmanagerpersonalized.controller;

import io.github.mateuszuran.ptdmanagerpersonalized.converter.TripConverter;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.TripRequestDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.TripRequestListDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Trip;
import io.github.mateuszuran.ptdmanagerpersonalized.service.TripService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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

/*    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveTrip(@Valid @RequestBody TripRequestDTO tripDto, @RequestParam Long id) {
        Trip trip = converter.tripRequestDtoConvertToEntity(tripDto);
        service.saveTrip(trip, id);
        log.info("no param");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }*/

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveTripList(@Valid @RequestBody TripRequestListDTO tripDto, @RequestParam Long id) {
         var tripList = tripDto.getTripList().stream()
                .map(converter::tripRequestDtoConvertToEntity)
                .collect(Collectors.toList());
        var result = service.save(tripList, id);
        return ResponseEntity.ok()
                .body(converter.tripListConvertToResponseDto(result));
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(@Valid @RequestParam Long id) {
        var list = service.getTrips(id);
        return ResponseEntity.ok().body(list.stream()
                .map(converter::tripConvertToResponseDto)
                .collect(Collectors.toList()));
    }
}
