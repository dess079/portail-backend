package ca.sd.portail.documentation;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DocumentationRepository extends JpaRepository<Documentation, Long> {
    Optional<Documentation> findByApplicationCodeAndScreenKeyAndActiveTrue(String applicationCode, String screenKey);
}
