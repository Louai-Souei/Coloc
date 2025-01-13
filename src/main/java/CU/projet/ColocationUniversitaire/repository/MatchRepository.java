package CU.projet.ColocationUniversitaire.repository;

import CU.projet.ColocationUniversitaire.entity.Match;
import CU.projet.ColocationUniversitaire.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Integer> {
    List<Match> findByUser(User user);
}

