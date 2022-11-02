package io.github.mateuszuran.ptdmanagerpersonalized.service.logic;

import io.github.mateuszuran.ptdmanagerpersonalized.model.Card;
import io.github.mateuszuran.ptdmanagerpersonalized.model.EToggle;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.CardRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.CountersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CardValidator {
    private final CardRepository repository;
    private final CountersRepository countersRepository;

    public CardValidator(final CardRepository repository, final CountersRepository countersRepository) {
        this.repository = repository;
        this.countersRepository = countersRepository;
    }

    public Card checkIfCardExists(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
    }

    public void validateCounters(Long id) {
        var result = checkIfCardExists(id).getCounters();
        if(result.getToggle() == EToggle.DONE) {
            result.setToggle(EToggle.UNDONE);
            countersRepository.save(result);
        }
    }
}
