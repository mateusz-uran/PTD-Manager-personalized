package io.github.mateuszuran.ptdmanagerpersonalized.service.logic;

import io.github.mateuszuran.ptdmanagerpersonalized.model.Card;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.CardRepository;
import org.springframework.stereotype.Component;

@Component
public class CardValidator {
    private final CardRepository repository;

    public CardValidator(final CardRepository repository) {
        this.repository = repository;
    }

    public Card checkIfCardExists(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
    }
}
