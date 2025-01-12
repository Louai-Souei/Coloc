package CU.projet.ColocationUniversitaire.service.serviceImpl;

import CU.projet.ColocationUniversitaire.dto.ApiResponse;
import CU.projet.ColocationUniversitaire.dto.PartageDepenseDto;
import CU.projet.ColocationUniversitaire.entity.*;
import CU.projet.ColocationUniversitaire.repository.DepenseRepository;
import CU.projet.ColocationUniversitaire.repository.LogementRepository;
import CU.projet.ColocationUniversitaire.repository.PartageDepenseRepository;
import CU.projet.ColocationUniversitaire.repository.UserRepository;
import CU.projet.ColocationUniversitaire.service.serviceInterface.PartageDepenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartageDepenseServiceImpl implements PartageDepenseService {

    private final PartageDepenseRepository partageDepenseRepository;
    private final DepenseRepository depenseRepository;
    private final LogementRepository logementRepository;
    private final UserRepository userRepository;

    @Override
    public ApiResponse<PartageDepenseDto> ajouterPartageDepense(Integer logementId, Double montantPartage) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User colocataire = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("Utilisateur actif introuvable."));

            Logement logement = logementRepository.findById(logementId)
                    .orElseThrow(() -> new IllegalArgumentException("Logement introuvable avec l'ID : " + logementId));

            boolean isColocataire = logement.getColocations().stream()
                    .anyMatch(colocation -> colocation.getColocataire().getId().equals(colocataire.getId()) && colocation.isActive());

            if (!isColocataire) {
                return new ApiResponse<>("Vous n'avez pas de colocation active dans ce logement.", null);
            }

            PartageDepense partageDepense = new PartageDepense();
            partageDepense.setLogement(logement);
            partageDepense.setMontantPartage(montantPartage);

            partageDepense = partageDepenseRepository.save(partageDepense);

            List<User> colocatairesActifs = logement.getColocations().stream()
                    .filter(Colocation::isActive)
                    .map(Colocation::getColocataire)
                    .toList();

            double montantParColocataire = montantPartage / colocatairesActifs.size();

            for (User colocataireActif : colocatairesActifs) {
                Depense depense = new Depense();
                depense.setMontant(montantParColocataire);
                depense.setLogement(logement);
                depense.setColocataire(colocataireActif);
                depense.setPartageDepense(partageDepense);

                depenseRepository.save(depense);
            }

            return new ApiResponse<>("Partage de dépense ajouté avec succès.", new PartageDepenseDto(partageDepense));
        } catch (Exception e) {
            return new ApiResponse<>("Échec de l'ajout du partage de dépense : " + e.getMessage(), null);
        }
    }



}
