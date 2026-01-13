package bada_project.SpringApplication.admin;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SystemFlagsDAO {

    private final JdbcTemplate jdbc;

    public SystemFlagsDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /* =========================
       MAINTENANCE MODE
       ========================= */

    public boolean isMaintenanceEnabled() {
        Integer value = jdbc.queryForObject(
                "SELECT MAINTENANCE_ENABLED FROM SYSTEM_FLAGS WHERE ID = 1",
                Integer.class
        );
        return value != null && value == 1;
    }

    public void setMaintenance(boolean enabled) {
        jdbc.update(
                "UPDATE SYSTEM_FLAGS SET MAINTENANCE_ENABLED = ? WHERE ID = 1",
                enabled ? 1 : 0
        );
    }
}
