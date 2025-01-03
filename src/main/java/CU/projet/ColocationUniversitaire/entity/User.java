package CU.projet.ColocationUniversitaire.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "utilisateurs")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String lastname;
    private String firstname;

    @Lob
    @Column(name = "photo_url", length = 1000000)
    private byte[] photo;

    private String email;
    private String num_tel;
    private Double budget;
    private String typelogementprefere;
    private String localisationprefere;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String password;

    @JsonManagedReference

    @OneToMany(mappedBy = "colocataire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Colocation> colocations = new ArrayList<>();
    @JsonManagedReference
    @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Logement> logements = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}