package CU.projet.ColocationUniversitaire.controller;

import CU.projet.ColocationUniversitaire.dto.ApiResponse;
import CU.projet.ColocationUniversitaire.dto.ColocationDto;
import CU.projet.ColocationUniversitaire.service.serviceInterface.ColocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/colocations")
@RequiredArgsConstructor
public class ColocationController {

    private final ColocationService colocationService;

    @PostMapping("/{logementId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COLOCATAIRE')")

    public ResponseEntity<ApiResponse<String>> ajouterColocationPourUtilisateurActif(
            @PathVariable Integer logementId
    ) {

        ApiResponse<String> response = colocationService.ajouterColocation(logementId);

        if (response.getMessage().contains("n'est pas disponible") || response.getMessage().contains("atteint")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAnyRole('COLOCATAIRE')")
    @GetMapping("/historique")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getHistoriqueColocations() {
        ApiResponse<List<Map<String, Object>>> response = colocationService.getHistoriqueColocations();

        if (response.getData() == null || response.getData().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PreAuthorize("hasAnyRole('COLOCATAIRE')")
    @PutMapping("/annuler/{logementId}")
    public ResponseEntity<String> annulerColocation(@PathVariable("logementId") Integer logementId) {
        ApiResponse<String> response = colocationService.annulerColocation(logementId);

        String responseMessage = response.getMessage();

        if (responseMessage.equals("La colocation a été annulée avec succès.")) {
            return ResponseEntity.ok(responseMessage);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMessage);
        }
    }

    @GetMapping("/created-colocations-stats")
    public ResponseEntity<Object> getCreatedColocationsStats() {
        try {
            Map<String, Long> stats = colocationService.getCreatedColocationsStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to fetch created colocations stats.");
        }
    }

}

