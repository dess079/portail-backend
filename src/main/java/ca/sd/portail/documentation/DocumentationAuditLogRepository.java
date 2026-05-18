package ca.sd.portail.documentation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentationAuditLogRepository extends JpaRepository<DocumentationAuditLog, Long> {
}
