package CU.projet.ColocationUniversitaire.service.serviceInterface;

import CU.projet.ColocationUniversitaire.dto.ApiResponse;
import CU.projet.ColocationUniversitaire.dto.PartageDepenseDto;

public interface PartageDepenseService {

    ApiResponse<PartageDepenseDto> ajouterPartageDepense(Integer logementId, Double montantPartage);

}
