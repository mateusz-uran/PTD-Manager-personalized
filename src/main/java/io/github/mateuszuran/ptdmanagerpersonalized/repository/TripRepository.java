package io.github.mateuszuran.ptdmanagerpersonalized.repository;

import io.github.mateuszuran.ptdmanagerpersonalized.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    Trip findByCardId(Long id);

    List<Trip> findAllByCardId(Long id);
}
