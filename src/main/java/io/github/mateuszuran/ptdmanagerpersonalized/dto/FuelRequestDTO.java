package io.github.mateuszuran.ptdmanagerpersonalized.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelRequestDTO {
    private String refuelingDate;
    private String refuelingLocation;
    private Integer actualVehicleCounter;
    private Integer refilledFuelAmount;
}
