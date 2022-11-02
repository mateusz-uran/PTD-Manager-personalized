package io.github.mateuszuran.ptdmanagerpersonalized.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User with given id: " + id + " not found.");
    }

    public UserNotFoundException(String username) {
        super("User with given username: " + username + " not found.");
    }
}
