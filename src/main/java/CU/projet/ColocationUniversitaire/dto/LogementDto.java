package CU.projet.ColocationUniversitaire.dto;

import CU.projet.ColocationUniversitaire.entity.Logement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogementDto {
    private Integer id;
    private String adresse;
    private Double prix;
    private String description;
    private String equipDispo;
    private Date dateDisponibilite;
    private int nombrePlaceLibre;
    private UserDto proprietaire;

    public LogementDto(Logement logement) {
        this.id = logement.getId();
        this.adresse = logement.getAdresse();
        this.prix = logement.getPrix();
        this.description = logement.getDescription();
        this.equipDispo = logement.getEquipDispo();
        this.dateDisponibilite = logement.getDateDisponibilite();
        this.nombrePlaceLibre = logement.getNombrePlaceLibre();
        this.proprietaire = new UserDto(logement.getProprietaire());
    }


    public Logement DtoToLogement() {
        Logement logement = new Logement();
        logement.setId(this.id);
        logement.setAdresse(this.adresse);
        logement.setPrix(this.prix);
        logement.setDescription(this.description);
        logement.setEquipDispo(this.equipDispo);
        logement.setDateDisponibilite(this.dateDisponibilite);
        logement.setNombrePlaceLibre(this.nombrePlaceLibre);
        if (this.proprietaire != null)
            logement.setProprietaire(this.proprietaire.DtoToUser());
        return logement;
    }
}
