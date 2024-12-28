package CU.projet.ColocationUniversitaire.repository;

import CU.projet.ColocationUniversitaire.entity.Colocation;
import CU.projet.ColocationUniversitaire.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColocationRepository extends JpaRepository<Colocation, Integer> {

    List<Colocation> findByColocataire(User colocataire);
}
