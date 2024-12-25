package CU.projet.ColocationUniversitaire.service.serviceImpl;

import CU.projet.ColocationUniversitaire.dto.AnnonceDto;
import CU.projet.ColocationUniversitaire.entity.Annonce;
import CU.projet.ColocationUniversitaire.entity.User;
import CU.projet.ColocationUniversitaire.repository.AnnonceRepository;
import CU.projet.ColocationUniversitaire.repository.UserRepository;
import CU.projet.ColocationUniversitaire.service.serviceInterface.AnnonceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnnonceServiceImpl implements AnnonceService {

    private final AnnonceRepository annonceRepository;
    private final UserRepository userRepository;

    @Override
    public AnnonceDto createNewAnnonce(AnnonceDto annonceDto) {
        Annonce annonce = annonceDto.DtoToAnnonce();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<User> colocataireOpt = userRepository.findByEmail(username);

        if (colocataireOpt.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur non trouvé ou non authentifié");
        }

        User colocataire = colocataireOpt.get();
        annonce.setColocataire(colocataire);

        Annonce savedAnnonce = annonceRepository.save(annonce);

        return new AnnonceDto(savedAnnonce);
    }
}
