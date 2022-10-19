package io.github.mateuszuran.ptdmanagerpersonalized.converter;

import io.github.mateuszuran.ptdmanagerpersonalized.dto.VehicleRequestDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.VehicleResponseDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Vehicle;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;

@Controller
public class VehicleConverter {
    ModelMapper mapper = new ModelMapper();

    public VehicleResponseDTO vehicleConvertToResponseDto(Vehicle vehicle) {
        return mapper.map(vehicle, VehicleResponseDTO.class);
    }

    public Vehicle vehicleResponseDtoConvertToEntity(VehicleResponseDTO vehicleDTO) {
        return mapper.map(vehicleDTO, Vehicle.class);
    }

    public Vehicle vehicleRequestDtoConvertToEntity(VehicleRequestDTO vehicleDto) {
        return mapper.map(vehicleDto, Vehicle.class);
    }

    public VehicleRequestDTO vehicleConvertToRequestDto(Vehicle vehicle) {
        return mapper.map(vehicle, VehicleRequestDTO.class);
    }
}
