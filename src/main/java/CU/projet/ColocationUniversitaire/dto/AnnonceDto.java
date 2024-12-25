package CU.projet.ColocationUniversitaire.dto;

import CU.projet.ColocationUniversitaire.entity.Annonce;
import CU.projet.ColocationUniversitaire.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnonceDto {
    private Integer id;
    private String adresse;
    private Double prix;
    private String description;
    private String equipDispo;
    private Date dateDisponibilite;
    private int nombrePlaceLibre;
    private UserDto colocataire;

    public AnnonceDto(Annonce annonce) {
        this.id = annonce.getId();
        this.adresse = annonce.getAdresse();
        this.prix = annonce.getPrix();
        this.description = annonce.getDescription();
        this.equipDispo = annonce.getEquipDispo();
        this.dateDisponibilite = annonce.getDateDisponibilite();
        this.nombrePlaceLibre = annonce.getNombrePlaceLibre();
        this.colocataire = new UserDto(annonce.getColocataire());
    }


    public Annonce DtoToAnnonce() {
        Annonce annonce = new Annonce();
        annonce.setId(this.id);
        annonce.setAdresse(this.adresse);
        annonce.setPrix(this.prix);
        annonce.setDescription(this.description);
        annonce.setEquipDispo(this.equipDispo);
        annonce.setDateDisponibilite(this.dateDisponibilite);
        annonce.setNombrePlaceLibre(this.nombrePlaceLibre);
        if (this.colocataire != null)
            annonce.setColocataire(this.colocataire.DtoToUser());
        return annonce;
    }
}
