package io.github.mateuszuran.ptdmanagerpersonalized.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardRequestDTO {
    private String number;
    private String username;
}
