package CU.projet.ColocationUniversitaire.repository;

import CU.projet.ColocationUniversitaire.entity.Logement;
import CU.projet.ColocationUniversitaire.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByLastname(String lastname);

    Optional<User> findByFirstname(String firstname);

    List<User> findByColocations_Logement(Logement logement);

    boolean existsByEmail(String email);

}
