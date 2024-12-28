package CU.projet.ColocationUniversitaire.service.serviceInterface;

import CU.projet.ColocationUniversitaire.dto.LogementDto;

import java.util.Date;
import java.util.List;

public interface LogementService {

    LogementDto createNewLogement(LogementDto logementDto);

    List<LogementDto> getAllAvailableLogements();

    LogementDto getLogementById(Integer id);

    LogementDto updateLogement(Integer id, LogementDto logementDto);

    void deleteLogement(Integer id);

    List<LogementDto> filterLogements(Date dateDisponibilite, int nombrePlaceLibre);

    List<LogementDto> getLogementsByLocalisation(String localisation);
}
