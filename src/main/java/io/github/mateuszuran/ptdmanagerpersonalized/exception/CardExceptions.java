package io.github.mateuszuran.ptdmanagerpersonalized.exception;

public class CardExceptions extends RuntimeException{
    public CardExceptions(String name) {
        super("Card with given name: " + name + " already exists.");
    }

    public CardExceptions(Long id) {
        super("Card with given id: " + id + " not found.");
    }
}
