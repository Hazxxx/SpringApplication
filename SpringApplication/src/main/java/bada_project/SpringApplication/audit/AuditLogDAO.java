package bada_project.SpringApplication.audit;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuditLogDAO {

    private final JdbcTemplate jdbc;

    public AuditLogDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void insert(String email, String action) {
        jdbc.update("""
                INSERT INTO AUDIT_LOG (USER_EMAIL, ACTION)
                VALUES (?, ?)
                """,
                email,
                action
        );
    }
}
