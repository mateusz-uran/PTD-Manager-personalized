package io.github.mateuszuran.ptdmanagerpersonalized.repository;

import io.github.mateuszuran.ptdmanagerpersonalized.model.Trip;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
    Optional<Trip> findByCardId(Long id);

    Optional<Trip> findById(Long id);

    List<Trip> findAllByCardId(Long id);

    List<Trip> findAllByCardId(Long id, Sort sort);
}
