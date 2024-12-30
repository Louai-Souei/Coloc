package CU.projet.ColocationUniversitaire.service.serviceImpl;

import CU.projet.ColocationUniversitaire.dto.ApiResponse;
import CU.projet.ColocationUniversitaire.dto.ColocationDto;
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

import java.util.List;
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

        // Décrémentez le nombre de places libres
        logementExistant.setNombrePlaceLibre(logementExistant.getNombrePlaceLibre() - 1);

        // Vérifiez si le logement devient indisponible
        if (logementExistant.getNombrePlaceLibre() == 0) {
            logementExistant.setDisponible(false);
        }

        logementRepository.save(logementExistant);

        return new ApiResponse<>("Colocation ajoutée avec succès.", null);
    }


    @Override
    public ApiResponse<List<ColocationDto>> getHistoriqueColocations() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> colocataireOpt = userRepository.findByEmail(username);
        User colocataire = colocataireOpt.orElseThrow(() -> new RuntimeException("Utilisateur actif introuvable."));


        List<Colocation> colocations = colocationRepository.findByColocataire(colocataire);


        List<ColocationDto> colocationsDto = colocations.stream()
                .map(ColocationDto::new)
                .collect(Collectors.toList());

        return new ApiResponse<>("Historique des colocations récupéré avec succès.", colocationsDto);
    }
}


