package io.github.mateuszuran.ptdmanagerpersonalized.exception;

public class CountersNotFoundException extends RuntimeException {
    public CountersNotFoundException(Long id) {
        super("Counters with given card id: " + id + " not found.");
    }
}
