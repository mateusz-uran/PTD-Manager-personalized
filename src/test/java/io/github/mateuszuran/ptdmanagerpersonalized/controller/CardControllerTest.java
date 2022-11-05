package io.github.mateuszuran.ptdmanagerpersonalized.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.CardRequestDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Card;
import io.github.mateuszuran.ptdmanagerpersonalized.model.User;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Vehicle;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.CardRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.UserRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.VehicleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@AutoConfigureMockMvc
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CardRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    private User user;
    private Card card;

    private final String URL = "/api/card";

    @AfterEach
    void flush() {
        repository.deleteAll();
        userRepository.deleteAll();
        vehicleRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("John")
                .password("john123")
                .build();
        userRepository.save(user);
        Vehicle vehicle = Vehicle.builder()
                .truckMainModel("VOLVO")
                .truckLicensePlate("LOP123")
                .user(user)
                .build();
        vehicleRepository.save(vehicle);
        card = Card.builder()
                .number("XYZ")
                .user(user)
                .build();
    }

    @Test
    void givenUserAndCard_whenSave_thenReturnObject() throws Exception {
        //given
        CardRequestDTO cardDto = CardRequestDTO.builder()
                .number("XYZ")
                .username(card.getUser().getUsername())
                .build();
        //when
        ResultActions result = mockMvc.perform(post(URL + "/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardDto)));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.number", is(card.getNumber())));
    }

    @Test
    void givenCardId_whenGet_thenReturnObject() throws Exception {
        //given
        Card card2 = Card.builder()
                .number("ABC")
                .user(user)
                .build();
        List<Card> cards = new ArrayList<>();
        cards.add(card);
        cards.add(card2);
        repository.saveAll(cards);
        //when
        ResultActions result = mockMvc.perform(get(URL + "/all")
                .param("username", user.getUsername()));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(cards.size())));
    }

    @Test
    void givenCardIdAndObject_whenUpdate_thenReturnUpdatedCard() throws Exception {
        //given
        repository.save(card);
        Card card2 = Card.builder()
                .number("ABC")
                .user(user)
                .build();
        //when
        ResultActions result = mockMvc.perform(put(URL + "/edit")
                .param("id", String.valueOf(card.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(card2)));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.number", is(card2.getNumber())));
    }

    @Test
    void givenCardId_whenDelete_thenReturnStatus204() throws Exception {
        //given
        repository.save(card);
        //when
        ResultActions result = mockMvc.perform(delete(URL + "/delete")
                .param("id", String.valueOf(card.getId())));
        //then
        result.andExpect(status().isNoContent());
    }
}