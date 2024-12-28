package CU.projet.ColocationUniversitaire.repository;

import CU.projet.ColocationUniversitaire.entity.Depense;
import CU.projet.ColocationUniversitaire.entity.PartageDepense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepenseRepository extends JpaRepository<Depense, Integer> {

    void deleteByPartageDepense(PartageDepense partageDepense);

    List<Depense> findByPartageDepense(PartageDepense partageDepense);
}