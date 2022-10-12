package io.github.mateuszuran.ptdmanagerpersonalized.service;

import io.github.mateuszuran.ptdmanagerpersonalized.model.User;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(final UserRepository repository) {
        this.repository = repository;
    }

    public User saveUser(User user) {
        user.setPassword(generateRegistrationCode());
        user.setPasswordChanged(false);
        return repository.save(user);
    }

    public User getUser(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User updatePassword(Long id, String newPassword) {
        if(checkIfUserChangedPassword(id)) {
            throw new IllegalArgumentException("Password was already changed");
        }
        return repository.findById(id)
                .map(user -> {
                    user.setPassword(newPassword);
                    user.setPasswordChanged(true);
                    return repository.save(user);
                })
                .orElseThrow(() -> new IllegalArgumentException("Password is invalid"));
    }

    public void deleteUser(Long id) {
        if(repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Password is invalid")) != null) {
            repository.deleteById(id);
        }
    }

    private boolean checkIfUserChangedPassword(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.isPasswordChanged();
    }

    private String generateRegistrationCode() {
        Random random = new Random();
        return random.ints(48, 122)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
