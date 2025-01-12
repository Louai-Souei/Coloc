package CU.projet.ColocationUniversitaire.service.serviceInterface;

import CU.projet.ColocationUniversitaire.dto.LogementDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface LogementService {

    LogementDto createNewLogement(LogementDto logementDto, List<MultipartFile> photos) throws IOException;

    List<LogementDto> getAllAvailableLogements();

    LogementDto getLogementById(Integer id);

    LogementDto updateLogement(Integer id, LogementDto logementDto);

    void deleteLogement(Integer id);

    List<LogementDto> filterLogements(Double prix, String adresse, String equipDispo);

    public List<LogementDto> getAllLogements();


    List<LogementDto> getLogementsByLocalisation(String localisation);
}
