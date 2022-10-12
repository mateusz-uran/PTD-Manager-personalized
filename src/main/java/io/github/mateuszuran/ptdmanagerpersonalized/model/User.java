package io.github.mateuszuran.ptdmanagerpersonalized.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 40, nullable = false, unique = true)
    private String login;
    @Column(nullable = false)
    @Size(min = 5, max = 55)
    private String password;
    private boolean passwordChanged;
}
