package CU.projet.ColocationUniversitaire.service.serviceImpl;

import CU.projet.ColocationUniversitaire.repository.DepenseRepository;
import CU.projet.ColocationUniversitaire.repository.LogementRepository;
import CU.projet.ColocationUniversitaire.repository.PartageDepenseRepository;
import CU.projet.ColocationUniversitaire.repository.UserRepository;
import CU.projet.ColocationUniversitaire.service.serviceInterface.PartageDepenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartageDepenseServiceImpl implements PartageDepenseService {

    private final PartageDepenseRepository partageDepenseRepository;
    private final DepenseRepository depenseRepository;
    private final LogementRepository logementRepository;
    private final UserRepository userRepository;


}
