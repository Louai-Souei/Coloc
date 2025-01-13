package CU.projet.ColocationUniversitaire.repository;

import CU.projet.ColocationUniversitaire.entity.Depense;
import CU.projet.ColocationUniversitaire.entity.Logement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogementRepository extends JpaRepository<Logement, Integer> {
    List<Logement> findByProprietaire_Email(String email);


}
