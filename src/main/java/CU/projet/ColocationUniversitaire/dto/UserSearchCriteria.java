package CU.projet.ColocationUniversitaire.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchCriteria {
    private Integer ageMin;
    private Integer ageMax;
    private String sexe;
    private Boolean fumeur;
    private Boolean animauxAcceptes;
    private Double budgetMin;
    private Double budgetMax;
    private String typelogementprefere;
    private String localisationprefere;
}
