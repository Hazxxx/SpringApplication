package bada_project.SpringApplication.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogDAO auditLogDAO;

    public List<AuditLog> findAll() {
        return auditLogDAO.findAll();
    }

    public void log(String username, String action, String details) {
        auditLogDAO.insert(username, action, details);
    }
}
