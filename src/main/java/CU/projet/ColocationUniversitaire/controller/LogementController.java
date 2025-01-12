package CU.projet.ColocationUniversitaire.controller;

import CU.projet.ColocationUniversitaire.dto.LogementDto;
import CU.projet.ColocationUniversitaire.service.serviceInterface.LogementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/logement")
@RequiredArgsConstructor
@Slf4j
public class LogementController {

    private final LogementService logementService;

    @PostMapping("/new-logement")
    public LogementDto createLogement(
            @RequestParam("adresse") String adresse,
            @RequestParam("prix") Double prix,
            @RequestParam("description") String description,
            @RequestParam("equipDispo") String equipDispo,
            @RequestParam("dateDisponibilite") String dateDisponibilite,
            @RequestParam("nombrePlaceLibre") int nombrePlaceLibre,
            @RequestParam("disponible") boolean disponible,
            @RequestParam("photos") List<MultipartFile> photos) throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateDisponibilite);
        } catch (ParseException e) {
            throw new RuntimeException("Format de date invalide", e);
        }

        LogementDto logementDto = new LogementDto();
        logementDto.setAdresse(adresse);
        logementDto.setPrix(prix);
        logementDto.setDescription(description);
        logementDto.setEquipDispo(equipDispo);
        logementDto.setDateDisponibilite(date);
        logementDto.setNombrePlaceLibre(nombrePlaceLibre);
        logementDto.setDisponible(disponible);

        return logementService.createNewLogement(logementDto, photos);
    }

    @GetMapping("/available")
    public List<LogementDto> getAllAvailableLogements() {
        log.info("Fetching all available logements");
        return logementService.getAllAvailableLogements();
    }

    @GetMapping("/{id}")
    public LogementDto getLogementById(@PathVariable Integer id) {
        log.info("Fetching logement with ID: {}", id);
        return logementService.getLogementById(id);
    }

    @PutMapping("/{id}")
    public LogementDto updateLogement(@PathVariable Integer id, @RequestBody LogementDto logementDto) {
        log.info("Updating logement with ID: {}", id);
        return logementService.updateLogement(id, logementDto);
    }

    @DeleteMapping("/{id}")
    public void deleteLogement(@PathVariable Integer id) {
        log.info("Deleting logement with ID: {}", id);
        logementService.deleteLogement(id);
    }

    @GetMapping("/filter")
    public List<LogementDto> filterLogements(
            @RequestParam(value = "prix", required = false) Double prix,
            @RequestParam(value = "adresse", required = false) String adresse,
            @RequestParam(value = "equipDispo", required = false) String equipDispo) {
        log.info("Filtering logements with criteria - prix: {}, adresse: {}, equipDispo: {}", prix, adresse, equipDispo);
        return logementService.filterLogements(prix, adresse, equipDispo);
    }

    @GetMapping("/all")
    public List<LogementDto> getAllLogements() {
        log.info("Fetching all logements for admin");
        return logementService.getAllLogements();
    }


    // Get logements by localisation
    @GetMapping("/localisation")
    public List<LogementDto> getLogementsByLocalisation(@RequestParam("localisation") String localisation) {
        log.info("Fetching logements by localisation: {}", localisation);
        return logementService.getLogementsByLocalisation(localisation);
    }

    @GetMapping("/by-owner")
    public List<LogementDto> getLogementsByOwner(Authentication authentication) {
        String ownerEmail = authentication.getName();
        return logementService.findByOwnerEmail(ownerEmail);
    }



}
