package io.github.mateuszuran.ptdmanagerpersonalized.exception;

public class TripNotFoundException extends RuntimeException {
    public TripNotFoundException(Long id) {
        super("Trip with given id: " + id + " not found.");
    }
}
