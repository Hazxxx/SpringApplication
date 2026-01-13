package bada_project.SpringApplication.admin;

import bada_project.SpringApplication.admin.dto.AdminDashboardStats;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDashboardDAO {

    private final JdbcTemplate jdbc;

    public AdminDashboardDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public AdminDashboardStats fetchStats() {
        return new AdminDashboardStats(
                jdbc.queryForObject("SELECT COUNT(*) FROM KLIENCI", Integer.class),
                jdbc.queryForObject("SELECT COUNT(*) FROM PRACOWNICY", Integer.class),
                jdbc.queryForObject("SELECT COUNT(*) FROM SALONY_SAMOCHODOWE", Integer.class),
                jdbc.queryForObject("SELECT COUNT(*) FROM FIRMY_PARTNERSKIE", Integer.class),
                jdbc.queryForObject("""
                SELECT COUNT(*) 
                FROM PRACOWNICY P
                JOIN STANOWISKA S ON P.ID_STANOWISKA = S.ID_STANOWISKA
                WHERE S.CZY_ADMIN = 1
            """, Integer.class)
        );
    }
}
