package io.github.mateuszuran.ptdmanagerpersonalized.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VehicleRequestDTO {
    private String truckMainModel;
    private String truckLicensePlate;
    private String truckType;
    private String trailerLicensePlate;
    private String trailerType;
    private Integer leftTankFuelCapacity;
    private Integer rightTankFuelCapacity;
    private Integer adBlueCapacity;
    private Integer trailerCapacity;
    private String username;
}
