package io.github.mateuszuran.ptdmanagerpersonalized.service;

import io.github.mateuszuran.ptdmanagerpersonalized.exception.TripNotFoundException;
import io.github.mateuszuran.ptdmanagerpersonalized.exception.VehicleNotFoundException;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Card;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Trip;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.CardRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.TripRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.service.logic.CardValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TripServiceTest {
    @Mock
    private TripRepository repository;
    @Mock
    private CardValidator validator;
    @InjectMocks
    private TripService service;
    private Card card;
    private Trip trip;

    @BeforeEach
    void setUp() {
        card = Card.builder()
                .number("123XYZ")
                .user(null)
                .build();
        trip = Trip.builder()
                .tripStartDay("1.01")
                .tripEndDay("3.01")
                .tripStartVehicleCounter(150)
                .tripEndVehicleCounter(230)
                .card(card)
                .build();
    }

    @Test
    void givenCardIdAndTripList_whenSaveAll_thenReturnList() {
        //given
        given(validator.checkIfCardExists(card.getId())).willReturn(card);
        Trip trip2 = Trip.builder()
                .tripStartDay("5.01")
                .tripEndDay("12.01")
                .tripStartVehicleCounter(280)
                .tripEndVehicleCounter(410)
                .card(card)
                .build();
        //when
        when(repository.saveAll(List.of(trip, trip2))).thenReturn(List.of(trip, trip2));
        var result = service.saveTrip(List.of(trip, trip2), card.getId());
        //then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void givenCardId_whenGetTrips_thenReturnList() {
        //given
        given(validator.checkIfCardExists(card.getId())).willReturn(card);
        Trip trip2 = Trip.builder()
                .tripStartDay("5.01")
                .tripEndDay("12.01")
                .tripStartVehicleCounter(280)
                .tripEndVehicleCounter(410)
                .card(card)
                .build();
        given(repository.findAllByCardId(card.getId())).willReturn(List.of(trip, trip2));
        //when
        var result = service.getTripsList(card.getId());
        //then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void givenCardId_whenGetTrips_thenReturnSortedList() {
        //given
        given(validator.checkIfCardExists(card.getId())).willReturn(card);
        Trip trip1 = Trip.builder()
                .tripStartVehicleCounter(300)
                .tripEndVehicleCounter(450)
                .card(card)
                .build();
        Trip trip2 = Trip.builder()
                .tripStartVehicleCounter(100)
                .tripEndVehicleCounter(200)
                .card(card)
                .build();
        Trip trip3 = Trip.builder()
                .tripStartVehicleCounter(250)
                .tripEndVehicleCounter(350)
                .card(card)
                .build();
        given(repository.findAllByCardId(card.getId(), Sort.by("tripStartVehicleCounter").ascending())).willReturn(List.of(trip2, trip3, trip1));
        //when
        var result = service.getSortedTipsList(card.getId());
        //then
        assertThat(result.get(0).getTripStartVehicleCounter()).isEqualTo(100);
    }

    @Test
    void givenTripId_whenGetSingleTrip_thenReturnObject() {
        //given
        given(repository.findById(trip.getId())).willReturn(Optional.of(trip));
        //when
        var result = service.getSingleTrip(trip.getId());
        //then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(trip);
    }

    @Test
    void givenTripId_whenGetSingleTrip_thenThrowTripNotFoundException() {
        //given
        given(repository.findById(trip.getId())).willReturn(Optional.empty());
        //when + then
        assertThatThrownBy(() -> service.getSingleTrip(trip.getId()))
                .isInstanceOf(TripNotFoundException.class)
                .hasMessageContaining("Trip with given id: " + trip.getId() + " not found.");
    }

    @Test
    void givenTripIdAndObject_whenEditTrip_thenReturnEditedObject() {
        //given
        given(repository.save(trip)).willReturn(trip);
        Trip tripToUpdate = Trip.builder()
                .tripStartDay("15.01")
                .tripEndDay("28.01")
                .tripStartVehicleCounter(500)
                .tripEndVehicleCounter(1000)
                .card(card)
                .build();
        given(repository.findById(trip.getId())).willReturn(Optional.of(trip));
        //when
        var result = service.editTripById(trip.getId(), tripToUpdate);
        //then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(trip);
        assertThat(trip.getTripStartDay()).isEqualTo("15.01");
        assertThat(trip.getCarMileage()).isEqualTo(500);
    }

    @Test
    void givenTripId_whenEditTrip_thenThrowTripNotFoundException() {
        //given
        given(repository.findById(trip.getId())).willReturn(Optional.empty());
        Trip tripToUpdate = Trip.builder()
                .tripStartDay("15.01")
                .tripEndDay("28.01")
                .tripStartVehicleCounter(500)
                .tripEndVehicleCounter(1000)
                .card(card)
                .build();
        //when + then
        assertThatThrownBy(() -> service.editTripById(trip.getId(), tripToUpdate))
                .isInstanceOf(TripNotFoundException.class)
                .hasMessageContaining("Trip with given id: " + trip.getId() + " not found.");
    }

    @Test
    void givenTripId_whenDelete_thenDoNothing() {
        //given
        given(repository.findById(trip.getId())).willReturn(Optional.of(trip));
        //when
        service.deleteTrip(trip.getId());
        //then
        verify(repository, times(1)).deleteById(trip.getId());
    }
}