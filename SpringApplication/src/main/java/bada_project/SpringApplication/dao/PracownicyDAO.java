package bada_project.SpringApplication.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
                                    rs.getString("STANOWISKO")
                            )
                    );
                },
                email
        );
    }

    /**
     * Check if employee email exists
     */
    public boolean existsByEmail(String email) {
        Integer cnt = jdbc.queryForObject(
                "SELECT COUNT(*) FROM PRACOWNICY WHERE LOWER(EMAIL) = LOWER(?)",
                Integer.class,
                email
        );
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
            String position
    ) {}
}