package io.github.mateuszuran.ptdmanagerpersonalized.exception;

public class PasswordChangedException extends RuntimeException {
    public PasswordChangedException() {
        super("User already changed password.");
    }
}
