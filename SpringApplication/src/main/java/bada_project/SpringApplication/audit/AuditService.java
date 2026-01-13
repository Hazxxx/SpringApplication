package bada_project.SpringApplication.audit;

import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private final AuditLogDAO dao;

    public AuditService(AuditLogDAO dao) {
        this.dao = dao;
    }

    public void log(String email, String action) {
        dao.insert(email, action);
    }
}
