package io.github.mateuszuran.ptdmanagerpersonalized.service;

import io.github.mateuszuran.ptdmanagerpersonalized.model.Card;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Trip;
import io.github.mateuszuran.ptdmanagerpersonalized.model.User;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.CardRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {
    @Mock
    private TripRepository repository;
    @Mock
    private CardRepository cardRepository;
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
                .card(card)
                .build();
    }

    @Test
    void givenCardIdAndTripList_whenSaveAll_thenReturnList() {
        //given
        given(cardRepository.findById(card.getId())).willReturn(Optional.of(card));
        Trip trip2 = Trip.builder()
                .tripStartDay("5.01")
                .tripEndDay("12.01")
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
        given(cardRepository.findById(card.getId())).willReturn(Optional.of(card));
        Trip trip2 = Trip.builder()
                .tripStartDay("5.01")
                .tripEndDay("12.01")
                .card(card)
                .build();
        given(repository.saveAll(List.of(trip, trip2))).willReturn(List.of(trip, trip2));
        //when
        var result = service.getTripsList(card.getId());
        //then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
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
    void givenCardIdAndObject_whenEditTrip_thenReturnEditedObject() {
        //given
        given(cardRepository.findById(card.getId())).willReturn(Optional.of(card));
        Trip trip2 = Trip.builder()
                .tripStartDay("5.01")
                .tripEndDay("12.01")
                .card(card)
                .build();
        given(repository.findByCardId(card.getId())).willReturn(Optional.of(trip));
        //when
        service.editTrip(card.getId(), trip2);
        //then
        verify(repository).save(any(Trip.class));
        assertThat(trip.getTripStartDay()).isEqualTo("5.01");
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