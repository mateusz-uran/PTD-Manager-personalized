package io.github.mateuszuran.ptdmanagerpersonalized.repository;

import io.github.mateuszuran.ptdmanagerpersonalized.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
