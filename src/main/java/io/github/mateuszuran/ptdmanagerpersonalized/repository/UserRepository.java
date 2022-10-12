package io.github.mateuszuran.ptdmanagerpersonalized.repository;

import io.github.mateuszuran.ptdmanagerpersonalized.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
