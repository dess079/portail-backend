package ca.sd.portail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application principale pour le backend Portail
 * Point d'entrée de l'application Spring Boot
 */
@SpringBootApplication
public class PortailApplication {

    /**
     * Méthode principale pour démarrer l'application
     * 
     * @param args Arguments de ligne de commande
     */
    public static void main(String[] args) {
        SpringApplication.run(PortailApplication.class, args);
    }
}