package io.github.mateuszuran.ptdmanagerpersonalized.exception;

public class RoleNotFoundException extends RuntimeException{
    public RoleNotFoundException(String name) {
        super("Role with given name: " + name + " not found.");
    }
}
