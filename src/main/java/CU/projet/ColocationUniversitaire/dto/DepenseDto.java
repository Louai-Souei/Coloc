package CU.projet.ColocationUniversitaire.dto;

import CU.projet.ColocationUniversitaire.entity.Depense;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepenseDto {

    private Integer id;
    private Double montant;
    private LogementDto logement;
    private UserDto colocataire;
    private PartageDepenseDto partageDepense;

    public DepenseDto(Depense depense) {
        this.id = depense.getId();
        this.montant = depense.getMontant();
        this.logement = new LogementDto(depense.getLogement());
        this.colocataire = new UserDto(depense.getColocataire());
        this.partageDepense = new PartageDepenseDto(depense.getPartageDepense());
    }

    public Depense DtoToDepense() {
        Depense depense = new Depense();
        depense.setId(this.id);
        depense.setMontant(this.montant);
        if (this.logement != null) {
            depense.setLogement(this.logement.DtoToLogement());
        }
        if (this.colocataire != null) {
            depense.setColocataire(this.colocataire.DtoToUser());
        }
        if (this.partageDepense != null) {
            depense.setPartageDepense(this.partageDepense.DtoToPartageDepense());
        }
        return depense;
    }
}
