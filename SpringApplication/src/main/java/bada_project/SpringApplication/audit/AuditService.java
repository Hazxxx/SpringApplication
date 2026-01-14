package bada_project.SpringApplication.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogDAO auditLogDAO;

    @Transactional(readOnly = true)
    public List<AuditLog> findAll() {
        try {
            return auditLogDAO.findAll();
        } catch (Exception e) {
            log.error("Error fetching audit logs", e);
            throw new RuntimeException("Failed to fetch audit logs", e);
        }
    }

    @Transactional
    public void deleteOlderThan48Hours() {
        try {
            int deleted = auditLogDAO.deleteOlderThan48Hours();
            if (deleted > 0) {
                log.info("Deleted {} audit logs older than 48 hours", deleted);
            }
        } catch (Exception e) {
            log.error("Error deleting old audit logs", e);
            // Don't throw - continue showing logs even if cleanup fails
        }
    }

    @Transactional
    public void log(String username, String action, String details) {
        try {
            auditLogDAO.insert(username, action, details);
            log.debug("Audit log created - User: {}, Action: {}", username, action);
        } catch (Exception e) {
            log.error("Failed to create audit log - User: {}, Action: {}", username, action, e);
            // Don't throw exception to avoid breaking the main operation
        }
    }
}