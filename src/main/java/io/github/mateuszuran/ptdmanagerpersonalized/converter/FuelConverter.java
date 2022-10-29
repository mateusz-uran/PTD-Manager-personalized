package io.github.mateuszuran.ptdmanagerpersonalized.converter;

import io.github.mateuszuran.ptdmanagerpersonalized.dto.FuelRequestDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.FuelResponseDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Fuel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class FuelConverter {
    ModelMapper mapper = new ModelMapper();

    public Fuel fuelRequestDtoConvertToEntity(FuelRequestDTO fuelDto) {
        return mapper.map(fuelDto, Fuel.class);
    }

    public FuelResponseDTO fuelEntityConvertToResponseDto(Fuel fuel) {
        return mapper.map(fuel, FuelResponseDTO.class);
    }
}
