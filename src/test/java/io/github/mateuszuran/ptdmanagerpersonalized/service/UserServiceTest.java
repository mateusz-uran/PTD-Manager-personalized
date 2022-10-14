package io.github.mateuszuran.ptdmanagerpersonalized.service;

import io.github.mateuszuran.ptdmanagerpersonalized.model.ERole;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Role;
import io.github.mateuszuran.ptdmanagerpersonalized.model.User;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.RoleRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private UserService service;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(
                "john",
                encoder.encode("john123"),
                false,
                "john123"
        );
    }

    @Test
    void saveUser() {
        //given
        given(userRepository.save(user)).willReturn(user);
        Role role = new Role(ERole.ROLE_USER);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(role));
        //when
        service.saveUser(user);
        //then
        verify(userRepository).save(any(User.class));
    }

    @Disabled
    @Test
    void checkIfUserExists() {
    }
    @Disabled
    @Test
    void getUser() {
    }
    @Disabled
    @Test
    void updatePassword() {
    }
    @Disabled
    @Test
    void deleteUser() {
    }
}