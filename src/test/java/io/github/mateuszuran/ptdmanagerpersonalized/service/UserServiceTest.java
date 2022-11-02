package io.github.mateuszuran.ptdmanagerpersonalized.service;

import io.github.mateuszuran.ptdmanagerpersonalized.exception.PasswordChangedException;
import io.github.mateuszuran.ptdmanagerpersonalized.model.ERole;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Role;
import io.github.mateuszuran.ptdmanagerpersonalized.model.User;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.RoleRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
    void givenUserObject_whenSaveUser_thenReturnSavedObject() {
        //given
        given(userRepository.save(user)).willReturn(user);
        Role role = new Role(ERole.ROLE_USER);
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(role));
        //when
        var result = service.saveUser(user);
        //then
        verify(userRepository).save(any(User.class));
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(user);
    }

    @Test
    void whenUserExistsThenReturnTrue() {
        //given
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);
        //when + then
        assertTrue(service.checkIfUserExists(user.getUsername()));
    }

    @Test
    void whenUserFoundedByIdThenReturnUser() {
        //given
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        //when + then
        assertThat(service.getUser(user.getId())).isEqualTo(user);
    }

    @Test
    void givenUserIdAndNewPassword_whenUpdatePassword_thenReturnUser() {
        //given
        given(userRepository.save(user)).willReturn(user);
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        //when
        service.updatePassword(user.getId(), "test123");
        //then
        verify(userRepository).save(any(User.class));
        assertTrue(user.isPasswordChanged());
    }

    @Test
    void givenUserIdAndNewPassword_whenPasswordUpdated_thenThrow() {
        //given
        User newUser = new User(
                "walter",
                encoder.encode("walter123"),
                true,
                "walter123"
        );
        given(userRepository.findById(newUser.getId())).willReturn(Optional.of(newUser));
        //when + then
        assertThatThrownBy(() -> service.updatePassword(newUser.getId(), "heisenberg123"))
                .isInstanceOf(PasswordChangedException.class)
                .hasMessageContaining("User already changed password.");

    }

    @Test
    void givenUserId_whenDelete_thenDoNothing() {
        //given
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        //when
        service.deleteUser(user.getId());
        //then
        verify(userRepository, times(1)).deleteById(user.getId());
    }
}