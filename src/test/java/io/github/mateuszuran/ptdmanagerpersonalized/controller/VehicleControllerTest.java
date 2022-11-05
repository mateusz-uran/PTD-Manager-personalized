package io.github.mateuszuran.ptdmanagerpersonalized.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.VehicleRequestDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.model.User;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Vehicle;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.UserRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.VehicleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@AutoConfigureMockMvc
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private VehicleRepository repository;
    @Autowired
    private UserRepository userRepository;

    private final String URL = "/api/vehicle";

    @AfterEach
    void flush() {
        repository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void givenVehicleId_whenGet_thenReturnObject() throws Exception {
        //given
        User user = User.builder()
                .username("John")
                .password("john123")
                .build();
        userRepository.save(user);
        Vehicle vehicle = Vehicle.builder()
                .truckMainModel("VOLVO")
                .truckLicensePlate("LOP123")
                .user(user)
                .build();
        repository.save(vehicle);
        ResultActions result = mockMvc.perform(get(URL + "/get")
                .param("id", String.valueOf(vehicle.getId())));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.truckMainModel", is(vehicle.getTruckMainModel())));
    }

    @Test
    void givenUserAndObject_whenSave_thenReturnVehicle() throws Exception {
        //given
        User user = User.builder()
                .username("John")
                .password("john123")
                .build();
        userRepository.save(user);
        Vehicle vehicle = Vehicle.builder()
                .truckMainModel("VOLVO")
                .truckLicensePlate("LOP123")
                .user(user)
                .build();
        VehicleRequestDTO vehicleDto = VehicleRequestDTO.builder()
                .truckMainModel("VOLVO")
                .truckLicensePlate("LOP123")
                .username("John")
                .build();
        //when
        ResultActions result = mockMvc.perform(post(URL + "/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDto)));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.truckMainModel", is(vehicle.getTruckMainModel())));
    }

    @Test
    void givenUserAndVehicleId_whenUpload_thenReturnMessage() throws Exception {
        //given
        User user = User.builder()
                .username("John")
                .password("john123")
                .build();
        userRepository.save(user);
        Vehicle vehicle = Vehicle.builder()
                .truckMainModel("VOLVO")
                .truckLicensePlate("LOP123")
                .user(user)
                .build();
        repository.save(vehicle);
        MockMultipartFile file = new MockMultipartFile("file", "image.png", "image/png", "data".getBytes());
        //when
        ResultActions result = mockMvc.perform(multipart(URL + "/upload").file(file)
                .param("id", String.valueOf(vehicle.getId())));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message", is("Image added successfully")));
    }

    @Test
    void givenVehicleId_whenDownload_thenReturnFile() throws Exception {
        //given
        User user = User.builder()
                .username("John")
                .password("john123")
                .build();
        userRepository.save(user);
        Vehicle vehicle = Vehicle.builder()
                .truckMainModel("VOLVO")
                .truckLicensePlate("LOP123")
                .user(user)
                .build();
        repository.save(vehicle);
        MockMultipartFile file = new MockMultipartFile("file", "image.png", "image/png", "data".getBytes());
        mockMvc.perform(multipart(URL + "/upload").file(file)
                .param("id", String.valueOf(vehicle.getId())));
        //when
        ResultActions result = mockMvc.perform(get(URL + "/download")
                .param("id", String.valueOf(vehicle.getId())));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().bytes(file.getBytes()));
    }

    @Test
    void givenVehicleId_whenDelete_thenDeleteVehicleWithAndImage() throws Exception{
        //given
        User user = User.builder()
                .username("John")
                .password("john123")
                .build();
        userRepository.save(user);
        Vehicle vehicle = Vehicle.builder()
                .truckMainModel("VOLVO")
                .truckLicensePlate("LOP123")
                .user(user)
                .build();
        repository.save(vehicle);
        MockMultipartFile file = new MockMultipartFile("file", "image.png", "image/png", "data".getBytes());
        mockMvc.perform(multipart(URL + "/upload").file(file)
                .param("id", String.valueOf(vehicle.getId())));
        //when
        ResultActions result = mockMvc.perform(delete(URL + "/delete")
                .param("id", String.valueOf(vehicle.getId())));
        //then
        result.andExpect(status().isNoContent());
    }

    @Test
    void givenVehicleId_whenDelete_thenDeleteOnlyImage() throws Exception{
        //given
        User user = User.builder()
                .username("John")
                .password("john123")
                .build();
        userRepository.save(user);
        Vehicle vehicle = Vehicle.builder()
                .truckMainModel("VOLVO")
                .truckLicensePlate("LOP123")
                .user(user)
                .build();
        repository.save(vehicle);
        MockMultipartFile file = new MockMultipartFile("file", "image.png", "image/png", "data".getBytes());
        mockMvc.perform(multipart(URL + "/upload").file(file)
                .param("id", String.valueOf(vehicle.getId())));
        //when
        ResultActions result = mockMvc.perform(delete(URL + "/delete-image")
                .param("id", String.valueOf(vehicle.getId())));
        //then
        result.andExpect(status().isNoContent());
        assertThat(repository.findById(vehicle.getId())).isPresent();
    }
}