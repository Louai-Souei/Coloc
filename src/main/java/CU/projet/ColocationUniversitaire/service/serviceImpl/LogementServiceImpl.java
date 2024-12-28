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

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogementServiceImpl implements LogementService {

    private final LogementRepository logementRepository;
    private final UserRepository userRepository;

    @Override
    public LogementDto createNewLogement(LogementDto logementDto) {
        Logement logement = logementDto.DtoToLogement();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> colocataireOpt = userRepository.findByEmail(username);

        if (colocataireOpt.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur non trouvé ou non authentifié");
        }

        User proprietaire = colocataireOpt.get();
        logement.setProprietaire(proprietaire);

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
    public List<LogementDto> filterLogements(Date dateDisponibilite, int nombrePlaceLibre) {
        List<Logement> logements = logementRepository.findAll();
        return logements.stream()
                .filter(logement -> logement.getDateDisponibilite().after(dateDisponibilite) &&
                        logement.getNombrePlaceLibre() >= nombrePlaceLibre)
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
