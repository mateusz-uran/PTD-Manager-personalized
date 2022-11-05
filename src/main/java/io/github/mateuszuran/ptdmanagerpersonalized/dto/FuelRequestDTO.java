package io.github.mateuszuran.ptdmanagerpersonalized.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuelRequestDTO {
    private String refuelingDate;
    private String refuelingLocation;
    private Integer actualVehicleCounter;
    private Integer refilledFuelAmount;
}
