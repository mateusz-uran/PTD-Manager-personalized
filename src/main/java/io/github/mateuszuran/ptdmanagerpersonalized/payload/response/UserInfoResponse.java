package io.github.mateuszuran.ptdmanagerpersonalized.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class UserInfoResponse {
    private Long id;
    private String username;
    private List<String> roles;
}
