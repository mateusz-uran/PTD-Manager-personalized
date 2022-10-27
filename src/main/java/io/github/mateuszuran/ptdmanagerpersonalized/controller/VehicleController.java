package io.github.mateuszuran.ptdmanagerpersonalized.controller;

import io.github.mateuszuran.ptdmanagerpersonalized.converter.VehicleConverter;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.VehicleRequestDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Vehicle;
import io.github.mateuszuran.ptdmanagerpersonalized.payload.response.MessageResponse;
import io.github.mateuszuran.ptdmanagerpersonalized.service.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/api/vehicle")
@Controller
public class VehicleController {
    private final VehicleService service;
    private final VehicleConverter converter;

    public VehicleController(final VehicleService service, final VehicleConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VehicleRequestDTO> getVehicle(@Valid @RequestParam Long id) {
        return ResponseEntity.ok()
                .body(converter.vehicleConvertToRequestDto(service.getVehicle(id)));
    }

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveVehicle(@Valid @RequestBody VehicleRequestDTO vehicleDTO) {
        Vehicle vehicle = converter.vehicleRequestDtoConvertToEntity(vehicleDTO);
        Vehicle createdVehicle = service.saveVehicle(vehicle, vehicleDTO.getUsername());
        var result = converter.vehicleConvertToResponseDto(createdVehicle);
        return ResponseEntity.ok(result);
    }

    @PostMapping(path = "/upload",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadImageToPostByParam(@RequestParam Long id, @RequestParam MultipartFile file) {
        service.uploadVehicleImage(id, file);
        return ResponseEntity.ok()
                .body(new MessageResponse("Image added successfully"));
    }

    @GetMapping(value = "/download")
    public ResponseEntity<byte[]> downloadVehicleImage(@RequestParam Long id) {
        return new ResponseEntity<>(service.downloadVehicleImage(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<HttpStatus> deleteVehicle(@RequestParam Long id) {
        service.deleteVehicle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/delete-image")
    public ResponseEntity<HttpStatus> deleteVehicleImage(@RequestParam Long id) {
        service.deleteVehicleImage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
