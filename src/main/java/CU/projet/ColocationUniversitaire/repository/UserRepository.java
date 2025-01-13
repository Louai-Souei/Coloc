package CU.projet.ColocationUniversitaire.repository;

import CU.projet.ColocationUniversitaire.entity.Logement;
import CU.projet.ColocationUniversitaire.entity.Role;
import CU.projet.ColocationUniversitaire.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    List<User> findByRoleNot(Role role);

    Optional<User> findByLastname(String lastname);

    Optional<User> findByFirstname(String firstname);

    List<User> findByColocations_Logement(Logement logement);
    Optional<User> findById(Integer id);



    boolean existsByEmail(String email);
    @Query("SELECT u FROM User u WHERE " +
            "(:ageMin IS NULL OR u.age >= :ageMin) AND " +
            "(:ageMax IS NULL OR u.age <= :ageMax) AND " +
            "(:sexe IS NULL OR u.sexe = :sexe) AND " +
            "(:budgetMin IS NULL OR u.budget >= :budgetMin) AND " +
            "(:budgetMax IS NULL OR u.budget <= :budgetMax) AND " +
            "(:fumeur IS NULL OR u.fumeur = :fumeur) AND " +
            "(:animauxAcceptes IS NULL OR u.animauxAcceptes = :animauxAcceptes)")
    List<User> findUsersByCriteria(@Param("ageMin") Integer ageMin,
                                   @Param("ageMax") Integer ageMax,
                                   @Param("sexe") String sexe,
                                   @Param("budgetMin") Double budgetMin,
                                   @Param("budgetMax") Double budgetMax,
                                   @Param("fumeur") Boolean fumeur,
                                   @Param("animauxAcceptes") Boolean animauxAcceptes);
    @Query("SELECT u FROM User u WHERE " +
            "(:typelogementprefere IS NULL OR u.typelogementprefere = :typelogementprefere) AND " +
            "(:localisationprefere IS NULL OR u.localisationprefere = :localisationprefere) AND " +
            "(:budgetMin IS NULL OR u.budget >= :budgetMin) AND " +
            "(:budgetMax IS NULL OR u.budget <= :budgetMax) AND " +
            "(:fumeur IS NULL OR u.fumeur = :fumeur) AND " +
            "(:animauxAcceptes IS NULL OR u.animauxAcceptes = :animauxAcceptes)")
    List<User> findPotentialMatches(
            @Param("typelogementprefere") String typelogementprefere,
            @Param("localisationprefere") String localisationprefere,
            @Param("budgetMin") Double budgetMin,
            @Param("budgetMax") Double budgetMax,
            @Param("fumeur") Boolean fumeur,
            @Param("animauxAcceptes") Boolean animauxAcceptes
    );


    @Query("""
            SELECT COUNT(DISTINCT u.id)
            FROM User u
            WHERE u.loginDate BETWEEN :startDate AND :endDate
            """)
    Long countActiveUsers(Date startDate, Date endDate);


}
