package CU.projet.ColocationUniversitaire.repository;

import CU.projet.ColocationUniversitaire.entity.Logement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogementRepository extends JpaRepository<Logement, Integer> {
}
