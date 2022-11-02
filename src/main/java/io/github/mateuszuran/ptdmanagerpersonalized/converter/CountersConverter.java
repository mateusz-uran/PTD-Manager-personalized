package io.github.mateuszuran.ptdmanagerpersonalized.converter;

import io.github.mateuszuran.ptdmanagerpersonalized.dto.CountersResponseDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Counters;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CountersConverter {
    ModelMapper mapper = new ModelMapper();

    public CountersResponseDTO countersResponseDtoConvertToEntity(Counters counters) {
        return mapper.map(counters, CountersResponseDTO.class);
    }
}
