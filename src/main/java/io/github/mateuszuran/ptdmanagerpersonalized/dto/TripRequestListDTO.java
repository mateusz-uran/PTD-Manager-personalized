package io.github.mateuszuran.ptdmanagerpersonalized.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripRequestListDTO {
    private List<TripRequestDTO> tripList;
}
