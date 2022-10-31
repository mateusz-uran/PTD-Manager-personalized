package io.github.mateuszuran.ptdmanagerpersonalized.service;

import io.github.mateuszuran.ptdmanagerpersonalized.model.Counters;
import io.github.mateuszuran.ptdmanagerpersonalized.model.EToggle;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Fuel;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Trip;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.CountersRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.service.logic.CardValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class CountersService {
    private final CountersRepository repository;
    private final CardValidator validator;

    public CountersService(final CountersRepository repository, final CardValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public Counters createOrUpdateCounters(Long id) {
        Counters counters = repository.findByCardId(id);
        var map = getTripAndFuelValues(id);
        if(counters != null) {
            counters.setStartCounter(map.get("minimalCounter"));
            counters.setEndCounter(map.get("maximumCounter"));
            counters.setSumCarMileage(map.get("sumMileage"));
            counters.setTotalRefuelling(map.get("sumFuel"));
            counters.setToggle(EToggle.DONE);
        } else {
            counters = new Counters();
            counters.setStartCounter(map.get("minimalCounter"));
            counters.setEndCounter(map.get("maximumCounter"));
            counters.setSumCarMileage(map.get("sumMileage"));
            counters.setTotalRefuelling(map.get("sumFuel"));
            counters.setToggle(EToggle.DONE);
            counters.setCard(validator.checkIfCardExists(id));
        }
        return repository.save(counters);
    }

    public Counters getCountersFromCard(Long id) {
        return validator.checkIfCardExists(id).getCounters();
    }

    private Map<String, Integer> getTripAndFuelValues(Long id) {
        var card = validator.checkIfCardExists(id);
        var minimalCounter = card.getTrips().stream().mapToInt(Trip::getTripStartVehicleCounter).min()
                .orElseThrow(NoSuchElementException::new);
        var maximumCounter = card.getTrips().stream().mapToInt(Trip::getTripEndVehicleCounter).max()
                .orElseThrow(NoSuchElementException::new);
        var sumMileage = card.getTrips().stream().mapToInt(Trip::getCarMileage).sum();
        var sumFuel = card.getFuels().stream().mapToInt(Fuel::getRefilledFuelAmount).sum();
        Map<String, Integer> metadata = new HashMap<>();
        metadata.put("minimalCounter", minimalCounter);
        metadata.put("maximumCounter", maximumCounter);
        metadata.put("sumMileage", sumMileage);
        metadata.put("sumFuel", sumFuel);
        return metadata;
    }
}
