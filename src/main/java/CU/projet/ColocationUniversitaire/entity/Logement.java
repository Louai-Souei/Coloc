package CU.projet.ColocationUniversitaire.entity;

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

    @ManyToOne
    @JoinColumn(name = "proprietaire_id", nullable = false)
    private User proprietaire;

    @OneToMany(mappedBy = "logement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Colocation> colocations = new ArrayList<>();
}