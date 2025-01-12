package CU.projet.ColocationUniversitaire.service.serviceImpl;

import CU.projet.ColocationUniversitaire.dto.ApiResponse;
import CU.projet.ColocationUniversitaire.dto.ColocationDto;
import CU.projet.ColocationUniversitaire.dto.UserDto;
import CU.projet.ColocationUniversitaire.entity.Colocation;
import CU.projet.ColocationUniversitaire.entity.Logement;
import CU.projet.ColocationUniversitaire.entity.User;
import CU.projet.ColocationUniversitaire.repository.ColocationRepository;
import CU.projet.ColocationUniversitaire.repository.LogementRepository;
import CU.projet.ColocationUniversitaire.repository.UserRepository;
import CU.projet.ColocationUniversitaire.service.serviceInterface.ColocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColocationServiceImpl implements ColocationService {

    private final ColocationRepository colocationRepository;
    private final LogementRepository logementRepository;
    private final UserRepository userRepository;

    @Override
    public ApiResponse<String> ajouterColocation(Integer logementId) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> colocataireOpt = userRepository.findByEmail(username);
        User colocataire = colocataireOpt.orElseThrow(() -> new RuntimeException("Utilisateur actif introuvable."));

        Logement logementExistant = logementRepository.findById(logementId)
                .orElseThrow(() -> new RuntimeException("Le logement avec l'ID " + logementId + " n'existe pas."));

        if (!logementExistant.isDisponible()) {
            return new ApiResponse<>("Le logement n'est pas disponible.", null);
        }

        long nombreColocationsActives = logementExistant.getColocations().stream()
                .filter(Colocation::isActive)
                .count();

        Colocation colocation = new Colocation();
        colocation.setColocataire(colocataire);
        colocation.setLogement(logementExistant);
        colocation.setActive(true);

        colocationRepository.save(colocation);

        logementExistant.setNombrePlaceLibre(logementExistant.getNombrePlaceLibre() - 1);

        if (logementExistant.getNombrePlaceLibre() == 0) {
            logementExistant.setDisponible(false);
        }

        logementRepository.save(logementExistant);

        return new ApiResponse<>("Colocation ajoutée avec succès.", null);
    }


    @Override
    public ApiResponse<List<Map<String, Object>>> getHistoriqueColocations() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> colocataireOpt = userRepository.findByEmail(username);
        User colocataire = colocataireOpt.orElseThrow(() -> new RuntimeException("Utilisateur actif introuvable."));

        List<Colocation> colocations = colocationRepository.findByColocataire(colocataire);

        List<Map<String, Object>> result = colocations.stream().map(colocation -> {
            Map<String, Object> enrichedData = new HashMap<>();

            enrichedData.put("id", colocation.getId());
            enrichedData.put("colocataire", new UserDto(colocation.getColocataire()));
            enrichedData.put("active", colocation.isActive());
            enrichedData.put("logementId", colocation.getLogement().getId());

            Logement logement = colocation.getLogement();
            enrichedData.put("logementDetails", Map.of(
                    "adresse", logement.getAdresse(),
                    "prix", logement.getPrix(),
                    "description", logement.getDescription()
            ));

            List<User> colocataires = colocationRepository.findColocatairesByLogementId(logement.getId());

            List<UserDto> colocatairesDto = colocataires.stream()
                    .map(UserDto::new)
                    .collect(Collectors.toList());

            enrichedData.put("colocataires", colocatairesDto);  // Ajouter la liste des colocataires

            return enrichedData;
        }).collect(Collectors.toList());

        return new ApiResponse<>("Historique des colocations récupéré avec succès.", result);
    }

    @Override
    public Optional<Colocation> findByColocataireAndLogementIdAndActiveTrue(User colocataire, Integer logementId) {
        return colocationRepository.findByColocataireAndLogementIdAndActiveTrue(colocataire, logementId);
    }


    @Override
    public ApiResponse<String> annulerColocation(Integer logementId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> colocataireOpt = userRepository.findByEmail(username);
        User colocataire = colocataireOpt.orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Optional<Colocation> colocationOpt = colocationRepository.findByColocataireAndLogementIdAndActiveTrue(colocataire, logementId);

        if (colocationOpt.isPresent()) {
            Colocation colocation = colocationOpt.get();
            colocation.setActive(false);
            colocationRepository.save(colocation);
            return new ApiResponse<String>("La colocation a été annulée avec succès.", null);
        } else {
            return new ApiResponse<String>("Aucune colocation active trouvée pour ce logement.", null);
        }
    }
}
