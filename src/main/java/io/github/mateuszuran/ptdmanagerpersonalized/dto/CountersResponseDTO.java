package io.github.mateuszuran.ptdmanagerpersonalized.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CountersResponseDTO {
    private Integer startCounter;
    private Integer endCounter;
    private Integer sumCarMileage;
    private Integer totalRefuelling;
    private String toggle;
}
