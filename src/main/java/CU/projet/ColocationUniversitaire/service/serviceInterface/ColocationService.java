package CU.projet.ColocationUniversitaire.service.serviceInterface;

import CU.projet.ColocationUniversitaire.dto.ApiResponse;
import CU.projet.ColocationUniversitaire.dto.ColocationDto;
import CU.projet.ColocationUniversitaire.entity.Colocation;
import CU.projet.ColocationUniversitaire.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ColocationService {

    ApiResponse<String> ajouterColocation(Integer logementId);

    ApiResponse<List<Map<String, Object>>> getHistoriqueColocations();
    Optional<Colocation> findByColocataireAndLogementIdAndActiveTrue(User colocataire, Integer logementId);

    ApiResponse<String> annulerColocation(Integer logementId);

    Map<String, Long> getCreatedColocationsStats();
}

