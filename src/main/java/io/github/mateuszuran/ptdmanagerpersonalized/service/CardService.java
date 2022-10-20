package io.github.mateuszuran.ptdmanagerpersonalized.service;

import io.github.mateuszuran.ptdmanagerpersonalized.model.Card;
import io.github.mateuszuran.ptdmanagerpersonalized.model.User;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.CardRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        User user = findUser(username);
        if (checkIfUserHasVehicle(user.getId())) {
            throw new IllegalArgumentException("User has not signed vehicle");
        }
        card.setUser(user);
        return repository.save(card);
    }

    public void delete(Long id) {
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Card not found")));
    }

    public Card editCard(Long id, String number) {
        return repository.findById(id)
                .map(card -> {
                    card.setNumber(number);
                    return repository.save(card);
                })
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
    }

    public List<Card> getCards(String username) {
        return repository.findAllByUser(findUser(username));
    }

    private User findUser(final String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private boolean checkIfUserHasVehicle(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found when searching vehicle."))
                .getVehicles().isEmpty();
    }
}
