package io.github.mateuszuran.ptdmanagerpersonalized.repository;

import io.github.mateuszuran.ptdmanagerpersonalized.model.Fuel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FuelRepository extends JpaRepository<Fuel, Long> {
    List<Fuel> findAllByCardId(Long id);

    Optional<Fuel> findById(Long id);

    List<Fuel> findAllByCardId(Long id, Sort sort);
}
