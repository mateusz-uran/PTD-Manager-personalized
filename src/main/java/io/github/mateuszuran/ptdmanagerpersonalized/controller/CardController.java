package io.github.mateuszuran.ptdmanagerpersonalized.controller;

import io.github.mateuszuran.ptdmanagerpersonalized.converter.CardConverter;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.CardRequestDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.dto.CardResponseDTO;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Card;
import io.github.mateuszuran.ptdmanagerpersonalized.service.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/card")
@Controller
public class CardController {
    private final CardService service;
    private final CardConverter converter;

    public CardController(final CardService service, final CardConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardResponseDTO> addCard(@Valid @RequestBody CardRequestDTO cardDto) {
        Card card = converter.cardRequestDtoConvertToEntity(cardDto);
        Card createdCard = service.saveCard(cardDto.getUsername(), card);
        return ResponseEntity.ok()
                .body(converter.cardConvertToResponseDto(createdCard));
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteCard(@RequestParam Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
