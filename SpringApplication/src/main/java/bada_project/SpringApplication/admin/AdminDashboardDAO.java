package bada_project.SpringApplication.admin;

import bada_project.SpringApplication.admin.dto.AdminDashboardStats;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class AdminDashboardDAO {

    private final JdbcTemplate jdbc;

    public AdminDashboardDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public AdminDashboardStats fetchStats() {

        int clients = jdbc.queryForObject(
                "SELECT COUNT(*) FROM KLIENCI", Integer.class);

        int employees = jdbc.queryForObject(
                "SELECT COUNT(*) FROM PRACOWNICY", Integer.class);

        int admins = jdbc.queryForObject("""
                SELECT COUNT(*) 
                FROM PRACOWNICY P
                JOIN STANOWISKA S ON P.ID_STANOWISKA = S.ID_STANOWISKA
                WHERE S.CZY_ADMIN = 1
        """, Integer.class);

        int salons = jdbc.queryForObject(
                "SELECT COUNT(*) FROM SALONY_SAMOCHODOWE", Integer.class);

        int companies = jdbc.queryForObject(
                "SELECT COUNT(*) FROM FIRMY_PARTNERSKIE", Integer.class);

        return new AdminDashboardStats(
                clients,
                employees,
                admins,
                salons,
                companies
        );
    }
    @Transactional
    public void clearDatabase() {

        // 1. Najbardziej zależne
        jdbc.update("DELETE FROM SPRZEDAZE");
        jdbc.update("DELETE FROM OFERTY");

        // 2. Dane użytkowe
        jdbc.update("DELETE FROM POJAZDY");
        jdbc.update("DELETE FROM MODELE");
        jdbc.update("DELETE FROM MARKI");

        // 3. Pracownicy / klienci
        jdbc.update("DELETE FROM PRACOWNICY");
        jdbc.update("DELETE FROM KLIENCI");

        // 4. Struktura salonów
        jdbc.update("DELETE FROM SALONY_SAMOCHODOWE");
        jdbc.update("DELETE FROM ADRESY");

        // 5. Słowniki / pomocnicze
        jdbc.update("DELETE FROM STANOWISKA");

        // Jeśli masz inne tabele z DDL – dokładamy tu
    }
}
