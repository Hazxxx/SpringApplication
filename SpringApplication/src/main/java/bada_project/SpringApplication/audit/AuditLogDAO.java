package bada_project.SpringApplication.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AuditLogDAO {

    private final JdbcTemplate jdbc;

    private final RowMapper<AuditLog> rowMapper = (rs, rowNum) -> {
        AuditLog log = new AuditLog();
        log.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
        log.setUsername(rs.getString("username"));
        log.setAction(rs.getString("action"));
        log.setDetails(rs.getString("details"));
        return log;
    };

    public List<AuditLog> findAll() {
        String sql = """
            SELECT timestamp, username, action, details
            FROM AUDIT_LOGS
            WHERE timestamp >= SYSTIMESTAMP - INTERVAL '48' HOUR
            ORDER BY timestamp DESC
        """;

        return jdbc.query(sql, rowMapper);
    }

    public int deleteOlderThan48Hours() {
        String sql = """
            DELETE FROM AUDIT_LOGS
            WHERE timestamp < SYSTIMESTAMP - INTERVAL '48' HOUR
        """;

        return jdbc.update(sql);
    }

    public void insert(String username, String action, String details) {
        String sql = """
            INSERT INTO AUDIT_LOGS (timestamp, username, action, details)
            VALUES (SYSTIMESTAMP, ?, ?, ?)
        """;

        jdbc.update(sql, username, action, details);
    }
}