package io.github.mateuszuran.ptdmanagerpersonalized.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserRequestDTO {
    private String username;
    private String password;
    private String temporaryPassword;
    private List<String> roles;
}
