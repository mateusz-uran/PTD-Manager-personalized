package io.github.mateuszuran.ptdmanagerpersonalized.service;

import io.github.mateuszuran.ptdmanagerpersonalized.model.Card;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Fuel;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.CardRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.FuelRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.service.logic.CardValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FuelServiceTest {
    @Mock
    private FuelRepository repository;
    @Mock
    private CardValidator validator;
    @InjectMocks
    private FuelService service;
    private Fuel fuel;
    private Card card;

    @BeforeEach
    void setUp() {
        card = Card.builder()
                .number("123XYZ")
                .user(null)
                .build();
        fuel = Fuel.builder()
                .refuelingDate("15.01")
                .refuelingLocation("PL")
                .actualVehicleCounter(123456)
                .refilledFuelAmount(500)
                .card(card)
                .build();

    }

    @Test
    void givenCardIdAndObject_whenSave_thenReturnObject() {
        //given
        given(validator.checkIfCardExists(card.getId())).willReturn(card);
        //when
        when(repository.save(fuel)).thenReturn(fuel);
        var result = service.saveRefuelling(card.getId(), fuel);
        //then
        assertThat(result).isEqualTo(fuel);
    }

    @Test
    void givenCardIdAndObjects_whenGetFuelList_thenReturnObjectsList() {
        //given
        given(validator.checkIfCardExists(card.getId())).willReturn(card);
        Fuel fuel2 = Fuel.builder()
                .refuelingDate("18.01")
                .refuelingLocation("PL")
                .actualVehicleCounter(144152)
                .refilledFuelAmount(590)
                .build();
        given(repository.findAllByCardId(card.getId())).willReturn(List.of(fuel, fuel2));
        //when
        var result = service.getFuelList(card.getId());
        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void givenCardIdAndObjects_whenGetFuelList_thenReturnSortedList() {
        //given
        given(validator.checkIfCardExists(card.getId())).willReturn(card);
        Fuel fuel2 = Fuel.builder()
                .refuelingDate("18.01")
                .refuelingLocation("PL")
                .actualVehicleCounter(111857)
                .refilledFuelAmount(590)
                .build();
        given(repository.findAllByCardId(card.getId(), Sort.by("actualVehicleCounter").ascending())).willReturn(List.of(fuel2, fuel));
        //when
        var result = service.getSortedFuelList(card.getId());
        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0)).isEqualTo(fuel2);
    }

    @Test
    void givenFuelId_whenEdit_thenUpdateObject() {
        //given
        given(repository.save(fuel)).willReturn(fuel);
        Fuel fuel2 = Fuel.builder()
                .refuelingDate("18.01")
                .refuelingLocation("PL")
                .actualVehicleCounter(144152)
                .refilledFuelAmount(590)
                .build();
        given(repository.findById(fuel.getId())).willReturn(Optional.of(fuel));
        //when
        var result = service.editFuel(fuel.getId(), fuel2);
        //then
        assertThat(result.getRefuelingDate()).isEqualTo("18.01");
    }

    @Test
    void givenFuelId_thenUpdate_thenPartialUpdate() {
        given(repository.save(fuel)).willReturn(fuel);
        given(repository.findById(fuel.getId())).willReturn(Optional.of(fuel));
        Fuel fuel2 = Fuel.builder()
                .refuelingDate("18.01")
                .build();
        //when
        var result = service.partialUpdate(fuel.getId(), fuel2);
        //then
        assertThat(result.getRefuelingDate()).isEqualTo("18.01");
        assertThat(result.getActualVehicleCounter()).isEqualTo(123456);
    }

    @Test
    void givenFuelId_whenDeleted_thenDoNothing() {
        //given
        given(repository.findById(fuel.getId())).willReturn(Optional.of(fuel));
        //when
        service.deleteFuel(fuel.getId());
        //then
        verify(repository, times(1)).deleteById(fuel.getId());
    }
}