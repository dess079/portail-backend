package ca.sd.portail.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TB_APP_ENV_LIAISON")
public class ApplicationEnvironnement implements Serializable {
    @EmbeddedId
    private ApplicationEnvironnementId id;

    @ManyToOne
    @MapsId("appCode")
    @JoinColumn(name = "app_code")
    private Application application;

    @ManyToOne
    @MapsId("envId")
    @JoinColumn(name = "env_id")
    private Environnement environnement;

    @Column(name = "url")
    private String url;

    // Getters & setters

    public ApplicationEnvironnementId getId() {
        return id;
    }

    public void setId(ApplicationEnvironnementId id) {
        this.id = id;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Environnement getEnvironnement() {
        return environnement;
    }

    public void setEnvironnement(Environnement environnement) {
        this.environnement = environnement;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
