package CU.projet.ColocationUniversitaire.service.serviceInterface;

import CU.projet.ColocationUniversitaire.dto.ApiResponse;
import CU.projet.ColocationUniversitaire.dto.ColocationDto;

import java.util.List;

public interface ColocationService {

    ApiResponse<String> ajouterColocation(Integer logementId);

    ApiResponse<List<ColocationDto>> getHistoriqueColocations();
}
