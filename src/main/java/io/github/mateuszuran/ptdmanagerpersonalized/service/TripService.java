package io.github.mateuszuran.ptdmanagerpersonalized.service;

import io.github.mateuszuran.ptdmanagerpersonalized.model.Trip;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.CardRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TripService {
    private final TripRepository repository;
    private final CardRepository cardRepository;

    public TripService(final TripRepository repository, final CardRepository cardRepository) {
        this.repository = repository;
        this.cardRepository = cardRepository;
    }

    public List<Trip> save(List<Trip> trips, Long id) {
        var result = cardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
        trips.forEach(trip -> trip.setCard(result));
        return repository.saveAll(trips);
    }

    public List<Trip> getTrips(Long id) {
        return repository.findAllByCardId(id);
    }
}
