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
            // Récupérer l'utilisateur authentifié
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User colocataire = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("Utilisateur actif introuvable."));

            // Vérifier si le logement existe
            Logement logement = logementRepository.findById(logementId)
                    .orElseThrow(() -> new IllegalArgumentException("Logement introuvable avec l'ID : " + logementId));

            // Vérifier si l'utilisateur a une colocation active dans ce logement
            boolean isColocataire = logement.getColocations().stream()
                    .anyMatch(colocation -> colocation.getColocataire().getId().equals(colocataire.getId()) && colocation.isActive());

            if (!isColocataire) {
                return new ApiResponse<>("Vous n'avez pas de colocation active dans ce logement.", null);
            }

            // Créer l'objet PartageDepense
            PartageDepense partageDepense = new PartageDepense();
            partageDepense.setLogement(logement);
            partageDepense.setMontantPartage(montantPartage);

            // Sauvegarder le partage de dépense
            partageDepense = partageDepenseRepository.save(partageDepense);

            // Obtenir les colocataires actifs dans ce logement
            List<User> colocatairesActifs = logement.getColocations().stream()
                    .filter(Colocation::isActive)
                    .map(Colocation::getColocataire)
                    .toList();

            // Diviser le montant entre les colocataires
            double montantParColocataire = montantPartage / colocatairesActifs.size();

            // Créer une dépense pour chaque colocataire
            for (User colocataireActif : colocatairesActifs) {
                Depense depense = new Depense();
                depense.setMontant(montantParColocataire);
                depense.setLogement(logement);
                depense.setColocataire(colocataireActif);
                depense.setPartageDepense(partageDepense);

                depenseRepository.save(depense);
            }

            // Retourner une réponse de succès
            return new ApiResponse<>("Partage de dépense ajouté avec succès.", new PartageDepenseDto(partageDepense));
        } catch (Exception e) {
            // Gérer les erreurs et retourner une réponse d'échec
            return new ApiResponse<>("Échec de l'ajout du partage de dépense : " + e.getMessage(), null);
        }
    }

}
