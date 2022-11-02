package io.github.mateuszuran.ptdmanagerpersonalized.service;

import io.github.mateuszuran.ptdmanagerpersonalized.exception.CardExceptions;
import io.github.mateuszuran.ptdmanagerpersonalized.exception.VehicleNotFoundException;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Card;
import io.github.mateuszuran.ptdmanagerpersonalized.model.User;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Vehicle;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.CardRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    @Mock
    private CardRepository repository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CardService service;
    private Card card;
    private User user;

    @BeforeEach
    void setUp() {
        Set<Vehicle> vehicles = new HashSet<>();
        vehicles.add(
                Vehicle.builder()
                        .truckMainModel("model")
                        .truckLicensePlate("lop123")
                        .build()
        );

        user = User.builder()
                .username("john")
                .password("john123")
                .vehicles(vehicles)
                .build();

        card = Card.builder()
                .number("123XYZ")
                .user(user)
                .build();
    }

    @Test
    void givenUser_whenSaveCard_thenReturnObject() {
        //given
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(repository.save(card)).willReturn(card);
        //when
        var result = service.saveCard(user.getUsername(), card);
        //then
        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isEqualTo(card.getNumber());
        verify(repository).save(any(Card.class));
    }

    @Test
    void givenUser_whenSaveCard_thenThrowVehicleNotFoundException() {
        //given
        Set<Vehicle> vehicles = new HashSet<>();
        User throwUser = User.builder()
                .username("john")
                .password("john123")
                .vehicles(vehicles)
                .build();
        given(userRepository.findByUsername(throwUser.getUsername())).willReturn(Optional.of(throwUser));
        given(userRepository.findById(throwUser.getId())).willReturn(Optional.of(throwUser));
        //when + then
        assertThatThrownBy(() -> service.saveCard(throwUser.getUsername(), card))
                .isInstanceOf(VehicleNotFoundException.class)
                .hasMessageContaining("User has not signed vehicle, can't add new card.");
    }

    @Test
    void givenUser_whenSaveCard_thenThrowCardException() {
        //given
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(repository.existsByNumber(card.getNumber())).willReturn(true);
        //when + then
        assertThatThrownBy(() -> service.saveCard(user.getUsername(), card))
                .isInstanceOf(CardExceptions.class)
                .hasMessageContaining("Card with given name: " + card.getNumber() + " already exists.");
    }

    @Test
    void givenCardId_whenDelete_thenDoNothing() {
        //given
        given(repository.findById(card.getId())).willReturn(Optional.of(card));
        //when
        service.delete(card.getId());
        // then
        verify(repository, times(1)).delete(card);
    }

    @Test
    void givenIdCard_whenUpdate_thenReturnObject() {
        //given
        given(repository.save(card)).willReturn(card);
        given(repository.findById(card.getId())).willReturn(Optional.of(card));
        //when
        var result = service.editCard(card.getId(), "toUpdate");
        //then
        assertThat(result.getNumber()).isNotEqualTo("123XYZ");
        assertThat(result.getNumber()).isEqualTo("toUpdate");
    }

    @Test
    void givenUsername_whenFindAllCards_thenReturnListOfCards() {
        //when
        Card cardToList = Card.builder()
                .number("abc123")
                .user(user)
                .build();
        List<Card> list = new ArrayList<>();
        list.add(card);
        list.add(cardToList);
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(repository.findAllByUser(user)).willReturn(list);
        //when
        var result = service.getCards(user.getUsername());
        //then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }
}