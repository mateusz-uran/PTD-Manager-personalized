package io.github.mateuszuran.ptdmanagerpersonalized.service.logic;

import io.github.mateuszuran.ptdmanagerpersonalized.exception.CardExceptions;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Card;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Counters;
import io.github.mateuszuran.ptdmanagerpersonalized.model.EToggle;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.CardRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.CountersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CardValidatorTest {
    @Mock
    private CardRepository repository;
    @Mock
    private CountersRepository countersRepository;
    @InjectMocks
    private CardValidator validator;
    private Card card;

    @BeforeEach
    void setUp() {
        Counters counters = Counters.builder()
                .toggle(EToggle.DONE)
                .build();
        card = Card.builder()
                .number("123XYZ")
                .user(null)
                .counters(counters)
                .build();
    }

    @Test
    void givenCardId_whenCheckIfExists_thenReturnObject() {
        //given
        given(repository.findById(card.getId())).willReturn(Optional.of(card));
        //when
        var result = validator.checkIfCardExists(card.getId());
        //then
        assertThat(result).isEqualTo(card);
    }

    @Test
    void givenCardId_whenCheckIfExists_thenThrowException() {
        //given
        given(repository.findById(card.getId())).willReturn(Optional.empty());
        //when + then
        assertThatThrownBy(() -> validator.checkIfCardExists(card.getId()))
                .isInstanceOf(CardExceptions.class)
                .hasMessageContaining("Card with given id: " + card.getId() + " not found.");
    }

    @Test
    void givenCardId_whenValidateCounters_thenVerify() {
        //given
        given(repository.findById(card.getId())).willReturn(Optional.of(card));
        //when
        validator.validateCounters(card.getId());
        //then
        verify(countersRepository, times(1)).save(card.getCounters());
    }
}