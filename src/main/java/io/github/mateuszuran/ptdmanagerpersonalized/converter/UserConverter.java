package io.github.mateuszuran.ptdmanagerpersonalized.converter;

import io.github.mateuszuran.ptdmanagerpersonalized.dto.UserRequestDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.model.User;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.UserResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    ModelMapper mapper = new ModelMapper();

    public UserResponseDTO userConvertToResponseDto(User user) {
        return mapper.map(user, UserResponseDTO.class);
    }

    public UserRequestDTO userConvertToRequestDto(User user) {
        return mapper.map(user, UserRequestDTO.class);
    }

    public User convertResponseDtoToEntity(UserResponseDTO userDTO) {
        return mapper.map(userDTO, User.class);
    }

    public User convertRequestDtoToEntity(UserRequestDTO userDTO) {
        return mapper.map(userDTO, User.class);
    }
}
