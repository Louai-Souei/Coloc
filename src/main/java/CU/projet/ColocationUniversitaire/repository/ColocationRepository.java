package CU.projet.ColocationUniversitaire.repository;

import CU.projet.ColocationUniversitaire.entity.Colocation;
import CU.projet.ColocationUniversitaire.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ColocationRepository extends JpaRepository<Colocation, Integer> {

    List<Colocation> findByColocataire(User colocataire);

    @Query("SELECT c.colocataire FROM Colocation c WHERE c.logement.id = :logementId")
    List<User> findColocatairesByLogementId(@Param("logementId") Integer logementId);

    Optional<Colocation> findByColocataireAndLogementIdAndActiveTrue(User colocataire, Integer logementId);

    @Query("""
            SELECT COUNT(DISTINCT c.id)
            FROM Colocation c
            WHERE c.createdAt BETWEEN :startDate AND :endDate
            """)
    Long countCreatedColocations(Date startDate, Date endDate);

}


