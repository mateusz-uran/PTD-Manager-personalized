package io.github.mateuszuran.ptdmanagerpersonalized.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mateuszuran.ptdmanagerpersonalized.model.*;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@AutoConfigureMockMvc
class CountersControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private FuelRepository fuelRepository;
    @Autowired
    private CountersRepository repository;
    private Card card;

    private final String URL = "/api/counters";

    @AfterEach
    void flush() {
        fuelRepository.deleteAll();
        tripRepository.deleteAll();
        repository.deleteAll();
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
        Trip trip = Trip.builder()
                .tripStartVehicleCounter(498)
                .tripEndVehicleCounter(1351)
                .carMileage(853)
                .card(card)
                .build();
        tripRepository.save(trip);
        Fuel fuel = Fuel.builder()
                .actualVehicleCounter(523)
                .refilledFuelAmount(135)
                .card(card)
                .build();
        fuelRepository.save(fuel);
        Counters counters = Counters.builder()
                .startCounter(498)
                .endCounter(1351)
                .sumCarMileage(853)
                .totalRefuelling(135)
                .card(card)
                .build();
        repository.save(counters);
    }

    @Test
    void givenCardId_whenPut_thenReturnUpdatedCountersObject() throws Exception {
        //given
        Fuel fuel2 = Fuel.builder()
                .actualVehicleCounter(500)
                .refilledFuelAmount(50)
                .card(card)
                .build();
        fuelRepository.save(fuel2);
        //when
        ResultActions result = mockMvc.perform(put(URL + "/save")
                .param("id", String.valueOf(card.getId())));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.totalRefuelling", is(185)));
    }

    @Test
    void givenCardId_whenGet_thenReturnCardCounters() throws Exception {
        //given + when
        ResultActions result = mockMvc.perform(get(URL + "/all")
                .param("id", String.valueOf(card.getId())));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.totalRefuelling", is(135)))
                .andExpect(jsonPath("$.endCounter", is(1351)));
    }
}