package io.github.mateuszuran.ptdmanagerpersonalized.exception;

public class FuelNotFoundException extends RuntimeException {
    public FuelNotFoundException(Long id) {
        super("Fuel with given id: " + id + " not found.");
    }
}
