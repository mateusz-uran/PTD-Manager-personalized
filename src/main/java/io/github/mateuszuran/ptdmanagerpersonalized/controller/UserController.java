package io.github.mateuszuran.ptdmanagerpersonalized.controller;

import io.github.mateuszuran.ptdmanagerpersonalized.converter.UserConverter;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.UserRequestDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.UserResponseDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.model.User;
import io.github.mateuszuran.ptdmanagerpersonalized.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/user")
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService service;
    private final UserConverter converter;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> saveUser(@Valid @RequestBody UserResponseDTO userDTO) {
        User user = converter.convertResponseDtoToEntity(userDTO);
        User createdUser = service.saveUser(user);
        return ResponseEntity.ok()
                .body(converter.userConvertToResponseDto(createdUser));
    }

    @GetMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserRequestDTO getUser(@Valid @RequestParam Long id) {
        return converter.userConvertToRequestDto(service.getUser(id));
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserRequestDTO updateUser(@Valid @RequestParam Long id, @RequestBody UserRequestDTO userDTO) {
        User user = converter.convertRequestDtoToEntity(userDTO);
        User updatedUser = service.updatePassword(id, user.getPassword());
        return converter.userConvertToRequestDto(updatedUser);
    }

    @DeleteMapping(value = "/delete")
    public void deleteUser(@Valid @RequestParam Long id) {
        service.deleteUser(id);
    }
}
