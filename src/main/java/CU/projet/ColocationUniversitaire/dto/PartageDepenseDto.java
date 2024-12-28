package CU.projet.ColocationUniversitaire.dto;

import CU.projet.ColocationUniversitaire.entity.PartageDepense;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartageDepenseDto {

    private Integer id;
    private Double montantPartage;
    private LogementDto logement;
    private List<DepenseDto> depenses;

    public PartageDepenseDto(PartageDepense partageDepense) {
        this.id = partageDepense.getId();
        this.montantPartage = partageDepense.getMontantPartage();
        this.logement = new LogementDto(partageDepense.getLogement());
        this.depenses = partageDepense.getDepenses().stream()
                .map(DepenseDto::new)
                .collect(Collectors.toList());
    }

    public PartageDepense DtoToPartageDepense() {
        PartageDepense partageDepense = new PartageDepense();
        partageDepense.setId(this.id);
        partageDepense.setMontantPartage(this.montantPartage);
        if (this.logement != null) {
            partageDepense.setLogement(this.logement.DtoToLogement());
        }
        return partageDepense;
    }
}
