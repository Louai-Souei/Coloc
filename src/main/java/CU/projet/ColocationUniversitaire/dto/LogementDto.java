package CU.projet.ColocationUniversitaire.dto;

import CU.projet.ColocationUniversitaire.entity.Logement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private boolean disponible;
    private UserDto proprietaire;
    private List<ColocationDto> colocations;
    private List<String> photoUrls;

    public LogementDto(Logement logement) {
        this.id = logement.getId();
        this.adresse = logement.getAdresse();
        this.prix = logement.getPrix();
        this.description = logement.getDescription();
        this.equipDispo = logement.getEquipDispo();
        this.dateDisponibilite = logement.getDateDisponibilite();
        this.nombrePlaceLibre = logement.getNombrePlaceLibre();
        this.disponible = logement.isDisponible();
        this.proprietaire = new UserDto(logement.getProprietaire());

        this.colocations = (logement.getColocations() != null)
                ? logement.getColocations().stream()
                .map(ColocationDto::new)
                .collect(Collectors.toList())
                : Collections.emptyList();
        this.photoUrls = logement.getPhotoUrls();
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
        logement.setDisponible(this.disponible);
        if (this.proprietaire != null) {
            logement.setProprietaire(this.proprietaire.DtoToUser());
        }
        logement.setPhotoUrls(this.photoUrls);
        return logement;
    }
}
