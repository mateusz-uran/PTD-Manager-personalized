package io.github.mateuszuran.ptdmanagerpersonalized.payload.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
