package io.github.mateuszuran.ptdmanagerpersonalized.service;

import io.github.mateuszuran.ptdmanagerpersonalized.model.*;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.CountersRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.TripRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.service.logic.CardValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CountersServiceTest {
    @Mock
    private CountersRepository repository;
    @Mock
    private CardValidator validator;
    @InjectMocks
    private CountersService service;
    private Card card;
    private Counters counters;

    @BeforeEach void setUp() {
        card = Card.builder()
                .number("123XYZ")
                .user(null)
                .trips(mirrorTripsData())
                .fuels(mirrorFuelData())
                .build();
        counters = Counters.builder()
                .startCounter(0)
                .endCounter(0)
                .card(card)
                .build();
    }

    @Test
    void givenCardId_whenSave_thenReturnUpdatedCounters() {
        //given
        given(repository.save(counters)).willReturn(counters);
        given(repository.findByCardId(card.getId())).willReturn(Optional.of(counters));
        given(validator.checkIfCardExists(card.getId())).willReturn(card);
        //when
        var result = service.updateCounters(card.getId());
        //then
        assertThat(result.getStartCounter()).isEqualTo(500);
    }

    @Test
    void getCountersFromCard() {
        //given
        given(validator.checkIfCardExists(card.getId())).willReturn(card);
        given(repository.findAllByCardId(card.getId())).willReturn(counters);
        //when
        var result = service.getCountersFromCard(card.getId());
        //then
        assertThat(result).isEqualTo(counters);
    }

    private Set<Trip> mirrorTripsData() {
        Trip trip1 = Trip.builder()
                .tripStartVehicleCounter(500)
                .tripEndVehicleCounter(1000)
                .carMileage(500)
                .build();
        Trip trip2 = Trip.builder()
                .tripStartVehicleCounter(1100)
                .tripEndVehicleCounter(2300)
                .carMileage(1200)
                .build();
        Set<Trip> trips = new HashSet<>();
        trips.add(trip1);
        trips.add(trip2);
        return trips;
    }

    private Set<Fuel> mirrorFuelData() {
        Fuel fuel1 = Fuel.builder()
                .refilledFuelAmount(500)
                .build();
        Fuel fuel2 = Fuel.builder()
                .refilledFuelAmount(750)
                .build();
        Set<Fuel> fuels = new HashSet<>();
        fuels.add(fuel1);
        fuels.add(fuel2);
        return fuels;
    }
}