package CU.projet.ColocationUniversitaire.controller;

import CU.projet.ColocationUniversitaire.dto.AnnonceDto;
import CU.projet.ColocationUniversitaire.service.serviceInterface.AnnonceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/annonce")
@RequiredArgsConstructor
@Slf4j
public class AnnonceController {

    private final AnnonceService annonceService;

    @PostMapping("/new-annonce")
    public AnnonceDto createAnnonce(@RequestBody AnnonceDto annonceDto) {
        log.info("Received annonceDto: {}", annonceDto);
        return annonceService.createNewAnnonce(annonceDto);
    }

}
