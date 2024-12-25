package CU.projet.ColocationUniversitaire;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class test implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            String sql = "SELECT 1";
            jdbcTemplate.execute(sql);
            System.out.println("Connexion réussie à la base de données !");
        } catch (Exception e) {
            System.err.println("Échec de la connexion à la base de données : " + e.getMessage());
        }
    }
}
