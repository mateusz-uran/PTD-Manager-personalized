package io.github.mateuszuran.ptdmanagerpersonalized.model;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "counters")
public class Counters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer startCounter;
    private Integer endCounter;
    private Integer sumCarMileage;
    private Integer totalRefuelling;
    @Enumerated(EnumType.STRING)
    private EToggle toggle;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;
}
