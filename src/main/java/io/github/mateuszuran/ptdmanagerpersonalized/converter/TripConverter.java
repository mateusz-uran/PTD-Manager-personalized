package io.github.mateuszuran.ptdmanagerpersonalized.converter;

import io.github.mateuszuran.ptdmanagerpersonalized.dto.TripRequestDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.TripResponseDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Trip;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TripConverter {
    ModelMapper mapper = new ModelMapper();

    public Trip tripRequestDtoConvertToEntity(TripRequestDTO tripDto) {
        return mapper.map(tripDto, Trip.class);
    }

    public TripResponseDTO tripConvertToResponseDto(Trip trip) {
        return mapper.map(trip, TripResponseDTO.class);
    }

    public List<TripResponseDTO> tripListConvertToResponseDto(List<Trip> trips) {
        return trips.stream()
                .map(trip -> mapper.map(trip, TripResponseDTO.class))
                .collect(Collectors.toList());
    }
}
