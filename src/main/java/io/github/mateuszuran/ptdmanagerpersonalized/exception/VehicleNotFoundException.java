package io.github.mateuszuran.ptdmanagerpersonalized.exception;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(Long id) {
        super("Vehicle with given id: " + id + " not found.");
    }

    public VehicleNotFoundException() {
        super("User has not signed vehicle, can't add new card.");
    }
}
