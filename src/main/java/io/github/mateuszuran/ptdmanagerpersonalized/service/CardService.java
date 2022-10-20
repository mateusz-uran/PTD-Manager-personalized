package io.github.mateuszuran.ptdmanagerpersonalized.service;

import io.github.mateuszuran.ptdmanagerpersonalized.model.Card;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.CardRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class CardService {
    private final CardRepository repository;
    private final UserRepository userRepository;

    public CardService(final CardRepository repository, final UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public Card saveCard(String username, Card card) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (checkIfUserHasVehicle(user.getId())) {
            throw new IllegalArgumentException("User has not signed vehicle");
        }
        card.setUser(user);
        return repository.save(card);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Card editCard(String username, String number, Long id) {
        var cardUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        var cardToEdit = cardUser.getCards().stream()
                .filter(card -> card.getId().equals(id))
                .findAny().orElseThrow(() -> new IllegalArgumentException("Card not found"));
        cardToEdit.setNumber(number);
        return repository.save(cardToEdit);
    }

    public List<Card> getCards(String username) {
        var cardUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return repository.findAllByUser(cardUser);
    }

    private boolean checkIfUserHasVehicle(Long id) {
        var checkUser = userRepository.findById(id).orElseThrow();
        return checkUser.getVehicles().isEmpty();
    }
}
