package io.github.mateuszuran.ptdmanagerpersonalized.service;

import io.github.mateuszuran.ptdmanagerpersonalized.exception.PasswordChangedException;
import io.github.mateuszuran.ptdmanagerpersonalized.exception.RoleNotFoundException;
import io.github.mateuszuran.ptdmanagerpersonalized.exception.UserNotFoundException;
import io.github.mateuszuran.ptdmanagerpersonalized.model.ERole;
import io.github.mateuszuran.ptdmanagerpersonalized.model.Role;
import io.github.mateuszuran.ptdmanagerpersonalized.model.User;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.RoleRepository;
import io.github.mateuszuran.ptdmanagerpersonalized.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public UserService(final UserRepository repository, final RoleRepository roleRepository, final PasswordEncoder encoder) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    public User saveUser(User user) {
        var tempPassword = generateRegistrationCode();
        user.setTemporaryPassword(tempPassword);
        user.setPassword(encoder.encode(tempPassword));
        user.setPasswordChanged(false);
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_USER.toString()));
        roles.add(userRole);
        user.setRoles(roles);
        return repository.save(user);
    }

    public boolean checkIfUserExists(String username) {
        return repository.existsByUsername(username);
    }

    public User getUser(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User updatePassword(Long id, String newPassword) {
        if(checkIfUserChangedPassword(id)) {
            throw new PasswordChangedException();
        }
        return repository.findById(id)
                .map(user -> {
                    user.setPassword(encoder.encode(newPassword));
                    user.setPasswordChanged(true);
                    return repository.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public void deleteUser(Long id) {
        if(repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id)) != null) {
            repository.deleteById(id);
        }
    }

    private boolean checkIfUserChangedPassword(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
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
