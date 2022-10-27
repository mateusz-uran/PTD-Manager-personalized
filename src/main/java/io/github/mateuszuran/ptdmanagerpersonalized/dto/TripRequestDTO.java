package io.github.mateuszuran.ptdmanagerpersonalized.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripRequestDTO {
    private String tripStartDay;
    private String tripEndDay;
    private String tripStartHour;
    private String tripEndHour;
    private String tripStartLocation;
    private String tripEndLocation;
    private String tripStartCountry;
    private String tripEndCountry;
    private Integer tripStartVehicleCounter;
    private Integer tripEndVehicleCounter;
}
