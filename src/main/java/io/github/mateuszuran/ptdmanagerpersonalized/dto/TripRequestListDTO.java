package io.github.mateuszuran.ptdmanagerpersonalized.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripRequestListDTO {
    private List<TripRequestDTO> tripList;
}
