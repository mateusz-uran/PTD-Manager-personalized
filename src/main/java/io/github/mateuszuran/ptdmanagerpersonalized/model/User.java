package io.github.mateuszuran.ptdmanagerpersonalized.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

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
    private String username;
    @Column(nullable = false)
    @Size(min = 5, max = 120)
    private String password;
    private boolean passwordChanged;
    private String temporaryPassword;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(final String username, final String password, final boolean passwordChanged, final String temporaryPassword) {
        this.username = username;
        this.password = password;
        this.passwordChanged = passwordChanged;
        this.temporaryPassword = temporaryPassword;
    }
}
