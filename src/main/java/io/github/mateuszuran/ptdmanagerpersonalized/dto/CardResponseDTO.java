package io.github.mateuszuran.ptdmanagerpersonalized.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardResponseDTO {
    private String number;
    private UserResponseDTO user;
}
