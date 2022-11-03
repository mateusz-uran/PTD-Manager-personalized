package io.github.mateuszuran.ptdmanagerpersonalized.dto;

import lombok.*;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserRequestDTO {
    private String username;
    private String password;
    private String temporaryPassword;
    private List<String> roles;
}
