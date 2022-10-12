package io.github.mateuszuran.ptdmanagerpersonalized.dto;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String login;
    private String password;
}
