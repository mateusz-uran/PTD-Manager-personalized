package io.github.mateuszuran.ptdmanagerpersonalized.controller;

import io.github.mateuszuran.ptdmanagerpersonalized.converter.FuelConverter;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.FuelRequestDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.FuelResponseDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Fuel;
import io.github.mateuszuran.ptdmanagerpersonalized.service.FuelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping("/api/fuel")
@RestController
public class FuelController {
    private final FuelService service;
    private final FuelConverter converter;

    public FuelController(final FuelService service, final FuelConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FuelResponseDTO> addFuel(@Valid @RequestBody FuelRequestDTO fuelDto, @RequestParam Long id) {
        Fuel fuel = converter.fuelRequestDtoConvertToEntity(fuelDto);
        var result = service.saveRefuelling(id, fuel);
        return ResponseEntity.ok()
                .body(converter.fuelEntityConvertToResponseDto(result));
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FuelResponseDTO>> getAllRefueling(@RequestParam Long id) {
        var result = service.getFuelList(id);
        return ResponseEntity.ok().body(result.stream()
                .map(converter::fuelEntityConvertToResponseDto)
                .collect(Collectors.toList()));
    }

    @GetMapping(value = "/sort", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FuelResponseDTO>> getAllRefuelingSorted(@RequestParam Long id) {
        var result = service.getSortedFuelList(id);
        return ResponseEntity.ok().body(result.stream()
                .map(converter::fuelEntityConvertToResponseDto)
                .collect(Collectors.toList()));
    }

    @PutMapping(value = "/edit-partial", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FuelResponseDTO> partialUpdateFuel(@Valid @RequestParam Long id, @RequestBody FuelRequestDTO fuelDto) {
        Fuel fuel = converter.fuelRequestDtoConvertToEntity(fuelDto);
        var result = service.partialUpdate(id, fuel);
        return ResponseEntity.ok()
                .body(converter.fuelEntityConvertToResponseDto(result));
    }

    @PatchMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FuelResponseDTO> editFuel(@Valid @RequestParam Long id, @RequestBody FuelRequestDTO fuelDto) {
        Fuel fuel = converter.fuelRequestDtoConvertToEntity(fuelDto);
        var result = service.editFuel(id, fuel);
        return ResponseEntity.ok()
                .body(converter.fuelEntityConvertToResponseDto(result));
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> delete(@RequestParam Long id) {
        service.deleteFuel(id);
        return ResponseEntity.noContent().build();
    }
}
