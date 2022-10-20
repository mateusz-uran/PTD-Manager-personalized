package io.github.mateuszuran.ptdmanagerpersonalized.converter;

import io.github.mateuszuran.ptdmanagerpersonalized.dto.CardRequestDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.CardResponseDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Card;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CardConverter {
    ModelMapper mapper = new ModelMapper();

    public Card cardRequestDtoConvertToEntity(CardRequestDTO cardDto) {
        return mapper.map(cardDto, Card.class);
    }

    public CardRequestDTO cardConvertToRequestDto(Card card) {
        return mapper.map(card, CardRequestDTO.class);
    }

    public Card cardResponseDtoConvertToEntity(CardResponseDTO cardDto) {
        return mapper.map(cardDto, Card.class);
    }

    public CardResponseDTO cardConvertToResponseDto(Card card) {
        return mapper.map(card, CardResponseDTO.class);
    }
}
