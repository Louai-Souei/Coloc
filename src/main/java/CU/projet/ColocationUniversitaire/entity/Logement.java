package CU.projet.ColocationUniversitaire.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Logement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String adresse;
    private Double prix;
    private String description;
    private String equipDispo;
    private Date dateDisponibilite;
    private int nombrePlaceLibre;
    private boolean disponible;

    @ManyToOne
    @JoinColumn(name = "proprietaire_id", nullable = false)
    private User proprietaire;

    @OneToMany(mappedBy = "logement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Colocation> colocations = new ArrayList<>();

    @OneToMany(mappedBy = "logement", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Depense> depenses = new ArrayList<>();
}