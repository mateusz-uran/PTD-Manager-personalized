package io.github.mateuszuran.ptdmanagerpersonalized.service;

import io.github.mateuszuran.ptdmanagerpersonalized.model.Card;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Trip;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.CardRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.TripRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TripService {
    private final TripRepository repository;
    private final CardRepository cardRepository;

    public TripService(final TripRepository repository, final CardRepository cardRepository) {
        this.repository = repository;
        this.cardRepository = cardRepository;
    }

    public List<Trip> saveTrip(List<Trip> trips, Long id) {
        var result = checkIfCardExists(id);
        trips.forEach(trip -> {
            trip.setCard(result);
            trip.setCarMileage(trip.subtract());
        });
        return repository.saveAll(trips);
    }

    public List<Trip> getSortedTipsList(Long id) {
        var result = checkIfCardExists(id);
        return repository.findAllByCardId(result.getId(), Sort.by("tripStartVehicleCounter").ascending());
    }

    public List<Trip> getTripsList(Long id) {
        var result = checkIfCardExists(id);
        return repository.findAllByCardId(result.getId());
    }

    public Trip getSingleTrip(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));
    }

    public Trip editTripById(Long id, Trip toUpdate) {
        return repository.findById(id)
                .map(trip -> {
                    trip.updateForm(toUpdate);
                    trip.setCarMileage(trip.subtract());
                    return repository.save(trip);
                }).orElseThrow(() -> new IllegalArgumentException("Trip not found"));
    }

    public void deleteTrip(Long id) {
        repository.findById(id)
                .ifPresent(trip -> repository.deleteById(trip.getId()));
    }

    private Card checkIfCardExists(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));
    }
}
