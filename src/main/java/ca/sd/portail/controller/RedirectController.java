package ca.sd.portail.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Contrôleur pour les redirections vers les endpoints de l'application
 */
@RestController
@RequestMapping("/api")
public class RedirectController {

    /**
     * Redirige les appels à /api/domains vers /api/v1/applications
     * Utile pour maintenir la compatibilité avec les clients qui appellent encore l'ancienne route
     */
    @GetMapping("/domains")
    public RedirectView redirectDomainsToApplications() {
        return new RedirectView("/api/v1/applications");
    }
}