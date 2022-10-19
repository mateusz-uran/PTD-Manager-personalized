package io.github.mateuszuran.ptdmanagerpersonalized.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VehicleResponseDTO {
    private String truckMainModel;
    private String truckLicensePlate;
    private String truckType;
    private String trailerLicensePlate;
    private String trailerType;
    private Integer leftTankFuelCapacity;
    private Integer rightTankFuelCapacity;
    private Integer adBlueCapacity;
    private Integer trailerCapacity;
    private UserResponseDTO user;
}
