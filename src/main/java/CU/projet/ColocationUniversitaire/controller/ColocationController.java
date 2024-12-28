package CU.projet.ColocationUniversitaire.controller;

import CU.projet.ColocationUniversitaire.dto.ApiResponse;
import CU.projet.ColocationUniversitaire.dto.ColocationDto;
import CU.projet.ColocationUniversitaire.service.serviceInterface.ColocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colocations")
@RequiredArgsConstructor
public class ColocationController {

    private final ColocationService colocationService;

    @PostMapping("/{logementId}")
    public ResponseEntity<ApiResponse<String>> ajouterColocationPourUtilisateurActif(
            @PathVariable Integer logementId
    ) {

        ApiResponse<String> response = colocationService.ajouterColocation(logementId);

        if (response.getMessage().contains("n'est pas disponible") || response.getMessage().contains("atteint")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/historique")
    public ResponseEntity<ApiResponse<List<ColocationDto>>> getHistoriqueColocations() {
        ApiResponse<List<ColocationDto>> response = colocationService.getHistoriqueColocations();

        if (response.getData() == null || response.getData().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

