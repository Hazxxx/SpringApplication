package bada_project.SpringApplication.audit;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AuditLogDAO {

    private final JdbcTemplate jdbc;

    public List<AuditLog> findAll() {
        String sql = """
            SELECT
                timestamp,
                username,
                action,
                details
            FROM AUDIT_LOGS
            ORDER BY timestamp DESC
        """;

        return jdbc.query(sql, (rs, rowNum) -> {
            AuditLog log = new AuditLog();
            log.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
            log.setUsername(rs.getString("username"));
            log.setAction(rs.getString("action"));
            log.setDetails(rs.getString("details"));
            return log;
        });
    }

    public void insert(String username, String action, String details) {
        jdbc.update("""
            INSERT INTO AUDIT_LOGS (timestamp, username, action, details)
            VALUES (SYSTIMESTAMP, ?, ?, ?)
        """, username, action, details);
    }
}
