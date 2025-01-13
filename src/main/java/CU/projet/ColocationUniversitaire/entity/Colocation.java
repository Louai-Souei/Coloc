package CU.projet.ColocationUniversitaire.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Colocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User colocataire;

    @ManyToOne
    @JoinColumn(name = "logement_id", nullable = false)
    @JsonBackReference
    private Logement logement;

    private boolean active;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Date createdAt;

}
