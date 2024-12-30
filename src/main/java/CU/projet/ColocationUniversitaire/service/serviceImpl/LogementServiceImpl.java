package CU.projet.ColocationUniversitaire.service.serviceImpl;

import CU.projet.ColocationUniversitaire.dto.LogementDto;
import CU.projet.ColocationUniversitaire.entity.Logement;
import CU.projet.ColocationUniversitaire.entity.User;
import CU.projet.ColocationUniversitaire.repository.LogementRepository;
import CU.projet.ColocationUniversitaire.repository.UserRepository;
import CU.projet.ColocationUniversitaire.service.serviceInterface.LogementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.io.File;

@Service
@RequiredArgsConstructor
public class LogementServiceImpl implements LogementService {

    private final LogementRepository logementRepository;
    private final UserRepository userRepository;

    private String savePhoto(MultipartFile photo, String uploadDir) throws IOException {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        Files.copy(photo.getInputStream(), filePath);

        return "/images/" + fileName;
    }
    @Override
    public LogementDto createNewLogement(LogementDto logementDto, List<MultipartFile> photos) throws IOException {
        // Convertir le DTO en entité Logement
        Logement logement = logementDto.DtoToLogement();

        // Récupérer l'utilisateur authentifié
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> colocataireOpt = userRepository.findByEmail(username);

        if (colocataireOpt.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur non trouvé ou non authentifié");
        }

        User proprietaire = colocataireOpt.get();
        logement.setProprietaire(proprietaire);

        // Traiter et stocker les fichiers photo
        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/images";
        List<String> photoUrls = photos.stream()
                .map(photo -> {
                    try {
                        return savePhoto(photo, uploadDir);
                    } catch (IOException e) {
                        throw new RuntimeException("Erreur lors du stockage de la photo", e);
                    }
                })
                .collect(Collectors.toList());
        logement.setPhotoUrls(photoUrls);

        // Sauvegarder le logement
        Logement savedLogement = logementRepository.save(logement);

        return new LogementDto(savedLogement);
    }

    @Override
    public List<LogementDto> getAllAvailableLogements() {
        List<Logement> logements = logementRepository.findAll();
        return logements.stream()
                .filter(logement -> logement.getNombrePlaceLibre() > 0 && logement.getDateDisponibilite().after(new Date()))
                .map(LogementDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public LogementDto getLogementById(Integer id) {
        Optional<Logement> logementOpt = logementRepository.findById(id);
        if (logementOpt.isEmpty()) {
            throw new IllegalArgumentException("Logement non trouvé");
        }
        return new LogementDto(logementOpt.get());
    }


    @Override
    public LogementDto updateLogement(Integer id, LogementDto logementDto) {
        Optional<Logement> logementOpt = logementRepository.findById(id);
        if (logementOpt.isEmpty()) {
            throw new IllegalArgumentException("Logement non trouvé");
        }

        Logement logement = logementOpt.get();
        logement.setAdresse(logementDto.getAdresse());
        logement.setPrix(logementDto.getPrix());
        logement.setDescription(logementDto.getDescription());
        logement.setEquipDispo(logementDto.getEquipDispo());
        logement.setDateDisponibilite(logementDto.getDateDisponibilite());
        logement.setNombrePlaceLibre(logementDto.getNombrePlaceLibre());


        if (logementDto.getProprietaire() != null) {
            logement.setProprietaire(logementDto.getProprietaire().DtoToUser());
        }

        Logement updatedLogement = logementRepository.save(logement);
        return new LogementDto(updatedLogement);
    }

    @Override
    public void deleteLogement(Integer id) {
        Optional<Logement> logementOpt = logementRepository.findById(id);
        if (logementOpt.isEmpty()) {
            throw new IllegalArgumentException("Logement non trouvé");
        }
        logementRepository.delete(logementOpt.get());
    }

    @Override
    public List<LogementDto> filterLogements(Double prix, String adresse, String equipDispo) {
        List<Logement> logements = logementRepository.findAll();
        return logements.stream()
                .filter(logement -> logement.isDisponible() && // Logement disponible
                        (prix == null || logement.getPrix().equals(prix)) && // Filtrer par prix si fourni
                        (adresse == null || logement.getAdresse().contains(adresse)) && // Filtrer par adresse si fourni
                        (equipDispo == null || logement.getEquipDispo().contains(equipDispo))) // Filtrer par equipDispo si fourni
                .map(LogementDto::new)
                .collect(Collectors.toList());
    }


    @Override
    public List<LogementDto> getLogementsByLocalisation(String localisation) {
        List<Logement> logements = logementRepository.findAll();
        return logements.stream()
                .filter(logement -> logement.getAdresse().contains(localisation))
                .map(LogementDto::new)
                .collect(Collectors.toList());
    }
}
