package io.github.mateuszuran.ptdmanagerpersonalized.exception;

public class UserExistsException  extends RuntimeException {
    public UserExistsException(String username) {
        super("User with given username: " + username + " already exists.");
    }
}
