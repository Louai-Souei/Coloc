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

import java.util.Optional;

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
}
