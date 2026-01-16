package bada_project.SpringApplication.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PracownicyDAO {

    private final JdbcTemplate jdbc;

    public PracownicyDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Find employee authentication data by email
     * Returns employee info including whether they have admin privileges
     */
    public Optional<EmployeeAuthRow> findAuthByEmail(String email) {
        return jdbc.query("""
                SELECT
                    P.EMAIL,
                    P.HASLO,
                    P.IMIE,
                    P.NAZWISKO,
                    S.CZY_ADMIN,
                    S.NAZWA AS STANOWISKO
                FROM PRACOWNICY P
                JOIN STANOWISKA S ON P.ID_STANOWISKA = S.ID_STANOWISKA
                WHERE LOWER(P.EMAIL) = LOWER(?)
                """,
                rs -> {
                    if (!rs.next()) {
                        return Optional.empty();
                    }
                    return Optional.of(
                            new EmployeeAuthRow(
                                    rs.getString("EMAIL"),
                                    rs.getString("HASLO"),
                                    rs.getString("IMIE"),
                                    rs.getString("NAZWISKO"),
                                    rs.getInt("CZY_ADMIN") == 1,
                                    rs.getString("STANOWISKO")));
                },
                email);
    }

    public int countEmployees() {
        return jdbc.queryForObject(
                "SELECT COUNT(*) FROM PRACOWNICY",
                Integer.class
        );
    }

    public int countAdmins() {
        return jdbc.queryForObject(
                "SELECT COUNT(*) FROM PRACOWNICY WHERE is_admin = 1",
                Integer.class
        );
    }

    /**
     * Znajdź pierwszego dostępnego pracownika (np. z rolą EMPLOYEE)
     */
    public Integer findFirstAvailableEmployeeId() {
        String sql = """
        SELECT id_pracownika
        FROM (
            SELECT p.id_pracownika
            FROM PRACOWNICY p
            JOIN STANOWISKA s ON p.id_stanowiska = s.id_stanowiska
            WHERE s.nazwa = 'Sprzedawca'
            ORDER BY p.id_pracownika
        )
        WHERE ROWNUM = 1
    """;

        return jdbc.query(sql, rs -> rs.next() ? rs.getInt(1) : null);
    }


    /**
     * Check if employee email exists
     */
    public boolean existsByEmail(String email) {
        Integer cnt = jdbc.queryForObject(
                "SELECT COUNT(*) FROM PRACOWNICY WHERE LOWER(EMAIL) = LOWER(?)",
                Integer.class,
                email);
        return cnt != null && cnt > 0;
    }

    /**
     * DTO for employee authentication
     */
    public record EmployeeAuthRow(
            String email,
            String passwordHash,
            String firstName,
            String lastName,
            boolean isAdmin,
            String position) {
    }

    /**
     * Find employee ID by email
     */
    public Integer findIdByEmail(String email) {
        try {
            return jdbc.queryForObject(
                    "SELECT id_pracownika FROM PRACOWNICY WHERE LOWER(email) = LOWER(?)",
                    Integer.class,
                    email);
        } catch (Exception e) {
            return null;
        }
    }
}