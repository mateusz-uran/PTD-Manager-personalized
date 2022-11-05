package io.github.mateuszuran.ptdmanagerpersonalized.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
