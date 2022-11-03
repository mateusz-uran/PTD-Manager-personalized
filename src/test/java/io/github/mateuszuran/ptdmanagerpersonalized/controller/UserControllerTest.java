package io.github.mateuszuran.ptdmanagerpersonalized.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mateuszuran.ptdmanagerpersonalized.model.ERole;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Role;
import io.github.mateuszuran.ptdmanagerpersonalized.model.User;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.RoleRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository repository;
    @Autowired
    private RoleRepository roleRepository;

    private final String URL = "/api/user";

    @AfterEach
    void flush() {
        repository.deleteAll();
    }

    @Test
    void givenRoleAndUser_whenSave_thenReturnNewObject() throws Exception {
        //given
        Role role = new Role(ERole.ROLE_USER);
        roleRepository.save(role);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user = User.builder()
                .username("John")
                .password("john123")
                .roles(roles)
                .build();
        //when
        ResultActions result = mockMvc.perform(post(URL + "/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));
        //then
        result.andExpect(status().isOk());
        User addedUser = repository.findByUsername("John").orElseThrow();
        assertThat(addedUser.getUsername()).isEqualTo("John");
    }

    @Test
    void givenUserId_whenGetById_thenUserObject() throws Exception {
        //given
        User user = User.builder()
                .username("John")
                .password("john123")
                .build();
        repository.save(user);
        //when
        ResultActions result = mockMvc.perform(get(URL + "/edit")
                .param("id", String.valueOf(user.getId())));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.username", is(user.getUsername())));
    }

    @Test
    void givenExistingUserAndNewPartialObject_whenUpdateUser_thenReturnUpdatedObject() throws Exception {
        //given
        User user = User.builder()
                .username("John")
                .password("temporaryPassword")
                .passwordChanged(false)
                .build();
        repository.save(user);
        User updateUser = User.builder()
                .password("changedPassword")
                .passwordChanged(true)
                .build();
        //when
        ResultActions result = mockMvc
                .perform(put(URL + "/update")
                        .param("id", String.valueOf(user.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser)));
        //then
        result.andExpect(status().isOk());
        User updatedUser = repository.findByUsername("John").orElseThrow();
        assertThat(updatedUser.isPasswordChanged()).isEqualTo(true);
    }

    @Test
    void givenUserId_whenDeleteById_thenReturnStatusIsOk200() throws Exception {
        //given
        User user = User.builder()
                .username("John")
                .password("temporaryPassword")
                .passwordChanged(false)
                .build();
        repository.save(user);
        //when
        ResultActions result = mockMvc
                .perform(delete(URL + "/delete")
                        .param("id", String.valueOf(user.getId())));
        //then
        result.andExpect(status().isOk());
    }
}