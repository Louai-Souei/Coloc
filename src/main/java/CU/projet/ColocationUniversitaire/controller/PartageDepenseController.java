package CU.projet.ColocationUniversitaire.controller;

import CU.projet.ColocationUniversitaire.dto.ApiResponse;
import CU.projet.ColocationUniversitaire.dto.PartageDepenseDto;
import CU.projet.ColocationUniversitaire.service.serviceInterface.PartageDepenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/partage-depense")
@RequiredArgsConstructor
public class PartageDepenseController {

    private final PartageDepenseService partageDepenseService;

    @PostMapping("/ajouter/{logementId}")
    public ResponseEntity<ApiResponse<PartageDepenseDto>> ajouterPartageDepense(
            @PathVariable Integer logementId,
            @RequestBody Double montantPartage) {
        System.out.println(logementId);
        ApiResponse<PartageDepenseDto> response = partageDepenseService.ajouterPartageDepense(logementId, montantPartage);
        return ResponseEntity.ok(response);
    }

}
