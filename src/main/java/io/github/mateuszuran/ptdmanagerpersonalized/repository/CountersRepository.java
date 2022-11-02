package io.github.mateuszuran.ptdmanagerpersonalized.repository;

import io.github.mateuszuran.ptdmanagerpersonalized.model.Counters;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountersRepository extends JpaRepository<Counters, Long> {
    Optional<Counters> findByCardId(Long id);

    Counters findAllByCardId(Long id);
}
