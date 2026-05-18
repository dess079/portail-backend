package ca.sd.portail.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tb_env_environnement")
public class Environnement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "env_id")
    private Long id;

    @Column(name = "env_code", unique = true, nullable = false)
    private String code;

    @Column(name = "env_nom", nullable = false)
    private String nom;

    @Column(name = "env_description")
    private String description;

    @ManyToMany(mappedBy = "environnements")
    private Set<Application> applications;

    // Getters & setters
    // ...
}
