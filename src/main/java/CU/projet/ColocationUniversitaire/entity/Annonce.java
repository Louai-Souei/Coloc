package CU.projet.ColocationUniversitaire.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Annonce {

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
    private User colocataire;

}

