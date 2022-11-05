package io.github.mateuszuran.ptdmanagerpersonalized.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.TripRequestDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.TripRequestListDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Card;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Counters;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Trip;
import io.github.mateuszuran.ptdmanagerpersonalized.model.User;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.CardRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.CountersRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.TripRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.UserRepository;
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

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@AutoConfigureMockMvc
class TripControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TripRepository repository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CountersRepository countersRepository;
    private Trip trip;
    private Card card;

    private final String URL = "/api/trip";

    @AfterEach
    void flush() {
        repository.deleteAll();
        cardRepository.deleteAll();
        userRepository.deleteAll();;
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
        trip = Trip.builder()
                .tripStartDay("15.01")
                .tripEndDay("28.01")
                .tripStartHour("15:00")
                .tripEndHour("18:30")
                .tripStartLocation("Opole")
                .tripEndLocation("Warszawa")
                .tripStartCountry("PL")
                .tripEndCountry("PL")
                .tripStartVehicleCounter(500)
                .tripEndVehicleCounter(1000)
                .card(card)
                .build();
        Counters counters = Counters.builder()
                .startCounter(trip.getTripStartVehicleCounter())
                .endCounter(trip.getTripEndVehicleCounter())
                .card(card)
                .build();
        countersRepository.save(counters);
    }

    @Test
    void givenTripList_whenSave_thenReturnListOfObjects() throws Exception {
        //given
        TripRequestDTO tripDto = TripRequestDTO.builder()
                .tripStartDay("15.01")
                .tripEndDay("28.01")
                .tripStartVehicleCounter(500)
                .tripEndVehicleCounter(1000)
                .build();
        TripRequestDTO tripDto2 = TripRequestDTO.builder()
                .tripStartDay("15.01")
                .tripEndDay("28.01")
                .tripStartVehicleCounter(500)
                .tripEndVehicleCounter(1000)
                .build();
        var listOfTripDto = List.of(tripDto, tripDto2);
        TripRequestListDTO list = TripRequestListDTO.builder()
                .tripList(listOfTripDto)
                .build();
        //when
        ResultActions result = mockMvc.perform(post(URL + "/save")
                .param("id", String.valueOf(card.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(list)));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfTripDto.size())));
    }

    @Test
    void givenCardId_whenGet_thenReturnSortedListOfObjects() throws Exception {
        //given
        Trip trip2 = Trip.builder()
                .tripStartDay("2.02")
                .tripEndDay("13.02")
                .tripStartVehicleCounter(1200)
                .tripEndVehicleCounter(1900)
                .card(card)
                .build();
        repository.saveAll(List.of(trip2, trip));
        //when
        ResultActions result = mockMvc.perform(get(URL + "/sort")
                .param("id", String.valueOf(card.getId())));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].trip.tripStartVehicleCounter", is(trip.getTripStartVehicleCounter())));
    }

    @Test
    void givenCardId_whenGet_thenReturnListOfObjects() throws Exception {
        //given
        Trip trip2 = Trip.builder()
                .tripStartDay("2.02")
                .tripEndDay("13.02")
                .tripStartVehicleCounter(1200)
                .tripEndVehicleCounter(1900)
                .card(card)
                .build();
        repository.saveAll(List.of(trip, trip2));
        //when
        ResultActions result = mockMvc.perform(get(URL + "/all")
                .param("id", String.valueOf(card.getId())));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(List.of(trip, trip2).size())));
    }

    @Test
    void givenTripId_whenGet_thenReturnObject() throws Exception {
        //given
        repository.save(trip);
        //when
        ResultActions result = mockMvc.perform(get(URL + "/get")
                .param("id", String.valueOf(trip.getId())));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.trip.tripStartDay", is(trip.getTripStartDay())));
    }

    @Test
    void givenTripId_whenEdit_thenReturnUpdatedObject() throws Exception {
        //given
        repository.save(trip);
        Trip trip2 = Trip.builder()
                .tripStartDay("2.02")
                .tripEndDay("28.01")
                .tripStartHour("15:00")
                .tripEndHour("18:30")
                .tripStartLocation("Opole")
                .tripEndLocation("Warszawa")
                .tripStartCountry("PL")
                .tripEndCountry("PL")
                .tripStartVehicleCounter(500)
                .tripEndVehicleCounter(1000)
                .build();
        //when
        ResultActions result = mockMvc.perform(patch(URL + "/edit")
                .param("id", String.valueOf(trip.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trip2)));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.trip.tripStartDay", is(trip2.getTripStartDay())));
    }

    @Test
    void givenTripId_whenDelete_thenReturnStatus204() throws Exception {
        //given
        repository.save(trip);
        //when
        ResultActions result = mockMvc.perform(delete(URL + "/delete")
                        .param("id", String.valueOf(trip.getId())));
        //then
        result.andExpect(status().isNoContent());
    }
}