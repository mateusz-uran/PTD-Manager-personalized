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

    public Counters(final Integer startCounter, final Integer endCounter, final Integer sumCarMileage, final Integer totalRefuelling, final EToggle toggle, final Card card) {
        this.startCounter = startCounter;
        this.endCounter = endCounter;
        this.sumCarMileage = sumCarMileage;
        this.totalRefuelling = totalRefuelling;
        this.toggle = toggle;
        this.card = card;
    }
}
