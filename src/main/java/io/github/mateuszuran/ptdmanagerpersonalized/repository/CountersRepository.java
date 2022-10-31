package io.github.mateuszuran.ptdmanagerpersonalized.repository;

import io.github.mateuszuran.ptdmanagerpersonalized.model.Counters;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountersRepository extends JpaRepository<Counters, Long> {
    Counters findByCardId(Long id);
}
