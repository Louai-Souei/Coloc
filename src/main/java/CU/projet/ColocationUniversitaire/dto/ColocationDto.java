package CU.projet.ColocationUniversitaire.dto;

import CU.projet.ColocationUniversitaire.entity.Colocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColocationDto {
    private Integer id;
    private UserDto colocataire;
    private boolean active;
    private Integer logementId;

    public ColocationDto(Colocation colocation) {
        this.id = colocation.getId();
        this.active = colocation.isActive();
        this.colocataire = new UserDto(colocation.getColocataire());
        this.logementId = colocation.getLogement().getId();
    }


    /*public Colocation DtoToColocation() {
        Colocation colocation = new Colocation();
        if (this.colocataire != null) {
            colocation.setColocataire(this.colocataire.DtoToUser());
        }
        if (this.logement != null) {
            colocation.setLogement(this.logement.DtoToLogement());
        }
        return colocation;
    }*/
}