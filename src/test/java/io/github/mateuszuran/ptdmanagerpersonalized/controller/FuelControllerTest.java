package io.github.mateuszuran.ptdmanagerpersonalized.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.FuelRequestDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.model.*;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@AutoConfigureMockMvc
class FuelControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FuelRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CountersRepository countersRepository;
    private Card card;
    private Fuel fuel;

    private final String URL = "/api/fuel";

    @AfterEach
    void flush() {
        repository.deleteAll();
        countersRepository.deleteAll();
        cardRepository.deleteAll();
        userRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .username("John")
                .password("john123")
                .build();
        userRepository.save(user);
        card = Card.builder()
                .number("XYZ")
                .user(user)
                .build();
        cardRepository.save(card);
        Counters counters = Counters.builder()
                .startCounter(500)
                .endCounter(1000)
                .card(card)
                .build();
        countersRepository.save(counters);
        fuel = Fuel.builder()
                .refuelingDate("15.01")
                .refuelingLocation("Opole")
                .actualVehicleCounter(500)
                .card(card)
                .build();

    }

    @Test
    void givenFuel_whenSave_thenReturnObject() throws Exception{
        //given
        Fuel fuel1 = Fuel.builder()
                .refuelingDate("16.01")
                .refuelingLocation("Warszawa")
                .actualVehicleCounter(1500)
                .build();
        // when
        ResultActions result = mockMvc.perform(post(URL + "/save")
                .param("id", String.valueOf(card.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fuel1)));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.fuel.refuelingDate", is(fuel1.getRefuelingDate())));
    }

    @Test
    void givenCardId_whenGetAll_thenReturnListOfObjects() throws Exception {
        //given
        Fuel fuel1 = Fuel.builder()
                .refuelingDate("16.01")
                .refuelingLocation("Warszawa")
                .actualVehicleCounter(1500)
                .card(card)
                .build();
        repository.saveAll(List.of(fuel, fuel1));
        //when
        ResultActions result = mockMvc.perform(get(URL + "/all")
                .param("id", String.valueOf(card.getId())));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(List.of(fuel, fuel1).size())));
    }

    @Test
    void givenCardId_whenGetAll_thenReturnSortedListOfObjects() throws Exception {
        //given
        Fuel fuel1 = Fuel.builder()
                .refuelingDate("16.01")
                .refuelingLocation("Warszawa")
                .actualVehicleCounter(1500)
                .card(card)
                .build();
        repository.saveAll(List.of(fuel1, fuel));
        //when
        ResultActions result = mockMvc.perform(get(URL + "/sort")
                .param("id", String.valueOf(card.getId())));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(List.of(fuel, fuel1).size())))
                .andExpect(jsonPath("$[0].fuel.refuelingDate", is(fuel.getRefuelingDate())));
    }

    @Test
    void givenCardIdAndObject_whenUpdate_thenReturnPartialUpdatedObject() throws Exception {
        //given
        Fuel fuel2 = Fuel.builder()
                .actualVehicleCounter(1920)
                .build();
        repository.save(fuel);
        //when
        ResultActions result = mockMvc.perform(put(URL + "/edit-partial")
                .param("id", String.valueOf(card.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fuel2)));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.fuel.actualVehicleCounter", is(fuel2.getActualVehicleCounter())));

    }

    @Test
    void givenFuelId_whenDelete_thenReturnStatus204() throws Exception {
        //given
        repository.save(fuel);
        //when
        ResultActions result = mockMvc.perform(delete(URL + "/delete")
                .param("id", String.valueOf(fuel.getId())));
        //then
        result.andExpect(status().isNoContent());
    }
}