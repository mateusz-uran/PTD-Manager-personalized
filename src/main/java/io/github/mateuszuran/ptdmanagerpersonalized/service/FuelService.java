package io.github.mateuszuran.ptdmanagerpersonalized.service;

import io.github.mateuszuran.ptdmanagerpersonalized.exception.FuelNotFoundException;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Fuel;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.FuelRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.service.logic.CardValidator;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuelService {
    private final FuelRepository repository;
    private final CardValidator validator;

    public FuelService(final FuelRepository repository, final CardValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public Fuel saveRefuelling(Long id, Fuel fuel) {
        var result = validator.checkIfCardExists(id);
        validator.validateCounters(id);
        fuel.setCard(result);
        return repository.save(fuel);
    }

    public List<Fuel> getFuelList(Long id) {
        var result = validator.checkIfCardExists(id);
        return repository.findAllByCardId(result.getId());
    }

    public List<Fuel> getSortedFuelList(Long id) {
        var result = validator.checkIfCardExists(id);
        return repository.findAllByCardId(result.getId(), Sort.by("actualVehicleCounter").ascending());
    }

    public Fuel editFuel(Long id, Fuel toUpdate) {
        return repository.findById(id)
                .map(fuel -> {
                    fuel.updateForm(toUpdate);
                    validator.validateCounters(fuel.getCard().getId());
                    return repository.save(fuel);
                }).orElseThrow(() -> new FuelNotFoundException(id));
    }

    public Fuel partialUpdate(Long id, Fuel toUpdate) {
        return repository.findById(id)
                .map(fuelToUpdate -> {
                    if (toUpdate.getRefuelingDate() != null) {
                        fuelToUpdate.setRefuelingDate(toUpdate.getRefuelingDate());
                    }
                    if (toUpdate.getRefuelingLocation() != null) {
                        fuelToUpdate.setRefuelingLocation(toUpdate.getRefuelingLocation());
                    }
                    if (toUpdate.getActualVehicleCounter() != null) {
                        fuelToUpdate.setActualVehicleCounter(toUpdate.getActualVehicleCounter());
                    }
                    if (toUpdate.getRefilledFuelAmount() != null) {
                        fuelToUpdate.setRefilledFuelAmount(toUpdate.getRefilledFuelAmount());
                    }
                    validator.validateCounters(fuelToUpdate.getCard().getId());
                    return repository.save(fuelToUpdate);
                }).orElseThrow(() -> new FuelNotFoundException(id));
    }

    public void deleteFuel(Long id) {
        repository.findById(id)
                .ifPresent(fuel -> {
                    validator.validateCounters(fuel.getCard().getId());
                    repository.deleteById(fuel.getId());
                });
    }
}
