package CU.projet.ColocationUniversitaire.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Depense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double montant;

    private boolean paye;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logement_id", nullable = false)
    private Logement logement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colocataire_id", nullable = false)
    private User colocataire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partage_depense_id", nullable = false)
    private PartageDepense partageDepense;
}
