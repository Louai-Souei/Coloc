package CU.projet.ColocationUniversitaire.repository;

import CU.projet.ColocationUniversitaire.entity.PartageDepense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartageDepenseRepository extends JpaRepository<PartageDepense, Integer> {

}
