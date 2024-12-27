package CU.projet.ColocationUniversitaire.controller;

import CU.projet.ColocationUniversitaire.dto.LogementDto;
import CU.projet.ColocationUniversitaire.service.serviceInterface.LogementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/logement")
@RequiredArgsConstructor
@Slf4j
public class LogementController {

    private final LogementService logementService;

    @PostMapping("/new-logement")
    public LogementDto createLogement(@RequestBody LogementDto logementDto) {
        return logementService.createNewLogement(logementDto);
    }

}
