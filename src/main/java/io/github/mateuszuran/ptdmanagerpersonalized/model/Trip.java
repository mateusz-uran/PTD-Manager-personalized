package io.github.mateuszuran.ptdmanagerpersonalized.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tripStartDay;
    private String tripEndDay;
    private String tripStartHour;
    private String tripEndHour;
    private String tripStartLocation;
    private String tripEndLocation;
    private String tripStartCountry;
    private String tripEndCountry;
    private Integer tripStartVehicleCounter;
    private Integer tripEndVehicleCounter;
    private Integer carMileage;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;
}
