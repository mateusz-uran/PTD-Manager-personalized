package io.github.mateuszuran.ptdmanagerpersonalized.model;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "fuels")
public class Fuel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String refuelingDate;
    private String refuelingLocation;
    private Integer actualVehicleCounter;
    private Integer refilledFuelAmount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    public void updateForm(Fuel source) {
        refuelingDate = source.refuelingDate;
        refuelingLocation = source.refuelingLocation;
        actualVehicleCounter = source.actualVehicleCounter;
        refilledFuelAmount = source.refilledFuelAmount;
    }
}
