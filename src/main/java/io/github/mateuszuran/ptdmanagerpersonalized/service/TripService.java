package io.github.mateuszuran.ptdmanagerpersonalized.service;

import io.github.mateuszuran.ptdmanagerpersonalized.model.Trip;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.TripRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.service.logic.CardValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TripService {
    private final TripRepository repository;
    private final CardValidator validator;

    public TripService(final TripRepository repository, final CardValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public List<Trip> saveTrip(List<Trip> trips, Long id) {
        var result = validator.checkIfCardExists(id);
        trips.forEach(trip -> {
            trip.setCarMileage(trip.subtract());
            trip.setCard(result);
        });
        validator.validateCounters(id);
        return repository.saveAll(trips);
    }

    public List<Trip> getSortedTipsList(Long id) {
        var result = validator.checkIfCardExists(id);
        return repository.findAllByCardId(result.getId(), Sort.by("tripStartVehicleCounter").ascending());
    }

    public List<Trip> getTripsList(Long id) {
        var result = validator.checkIfCardExists(id);
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
                    validator.validateCounters(trip.getCard().getId());
                    return repository.save(trip);
                }).orElseThrow(() -> new IllegalArgumentException("Trip not found"));
    }

    /** ready to implement **/
    /*public Trip partialUpdate(Long id, Trip toUpdate) {
        return repository.findById(id)
                .map(trip -> {
                    if (toUpdate.getTripStartDay() != null) {
                        trip.setTripStartDay(toUpdate.getTripStartDay());
                    } else if (toUpdate.getTripEndDay() != null) {
                        trip.setTripEndDay(toUpdate.getTripEndDay());
                    } else if (toUpdate.getTripStartHour() != null) {
                        trip.setTripStartHour(toUpdate.getTripStartHour());
                    } else if (toUpdate.getTripEndHour() != null) {
                        trip.setTripEndHour(toUpdate.getTripEndHour());
                    } else if (toUpdate.getTripStartLocation() != null) {
                        trip.setTripStartLocation(toUpdate.getTripStartLocation());
                    } else if (toUpdate.getTripEndLocation() != null) {
                        trip.setTripEndLocation(toUpdate.getTripEndLocation());
                    } else if (toUpdate.getTripStartCountry() != null) {
                        trip.setTripStartCountry(toUpdate.getTripStartCountry());
                    } else if (toUpdate.getTripEndCountry() != null) {
                        trip.setTripEndCountry(toUpdate.getTripEndCountry());
                    } else if (toUpdate.getTripStartVehicleCounter() != null) {
                        trip.setTripStartVehicleCounter(toUpdate.getTripStartVehicleCounter());
                    } else if (toUpdate.getTripEndVehicleCounter() != null) {
                        trip.setTripEndVehicleCounter(toUpdate.getTripEndVehicleCounter());
                    }
                    trip.setCarMileage(toUpdate.subtract());
                    return repository.save(trip);
                }).orElseThrow(() -> new IllegalArgumentException("Trip not found"));
    }*/

    public void deleteTrip(Long id) {
        repository.findById(id)
                .ifPresent(trip -> {
                    validator.validateCounters(trip.getCard().getId());
                    repository.deleteById(trip.getId());
                });
    }
}
