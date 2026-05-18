package ca.sd.portail.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ApplicationEnvironnementId implements Serializable {
    @Column(name = "app_code")
    private String appCode;

    @Column(name = "env_id")
    private Long envId;

    public ApplicationEnvironnementId() {}

    public ApplicationEnvironnementId(String appCode, Long envId) {
        this.appCode = appCode;
        this.envId = envId;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Long getEnvId() {
        return envId;
    }

    public void setEnvId(Long envId) {
        this.envId = envId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationEnvironnementId that = (ApplicationEnvironnementId) o;
        return Objects.equals(appCode, that.appCode) && Objects.equals(envId, that.envId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appCode, envId);
    }
}