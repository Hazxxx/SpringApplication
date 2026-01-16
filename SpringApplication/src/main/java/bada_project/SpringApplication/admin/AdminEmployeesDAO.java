package bada_project.SpringApplication.admin;

import bada_project.SpringApplication.admin.dto.EmployeeDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AdminEmployeesDAO {

    private final JdbcTemplate jdbc;

    public AdminEmployeesDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /* =========================
       FETCH ALL EMPLOYEES
       ========================= */
    public List<EmployeeDTO> findAll() {
        String sql = """
            SELECT 
                P.ID_PRACOWNIKA,
                P.EMAIL,
                P.IMIE,
                P.NAZWISKO,
                P.TELEFON,
                P.ID_WYNAGRODZENIA,
                P.ID_STANOWISKA,
                ST.NAZWA AS NAZWA_STANOWISKA,
                ST.CZY_ADMIN,
                P.ID_SALONU,
                S.NAZWA AS NAZWA_SALONU
            FROM PRACOWNICY P
            LEFT JOIN STANOWISKA ST ON P.ID_STANOWISKA = ST.ID_STANOWISKA
            LEFT JOIN SALONY_SAMOCHODOWE S ON P.ID_SALONU = S.ID_SALONU
            ORDER BY P.ID_PRACOWNIKA DESC
            """;

        return jdbc.query(sql, new EmployeeRowMapper());
    }

    /* =========================
       FETCH EMPLOYEE BY ID
       ========================= */
    public EmployeeDTO findById(Long id) {
        String sql = """
            SELECT 
                P.ID_PRACOWNIKA,
                P.EMAIL,
                P.IMIE,
                P.NAZWISKO,
                P.TELEFON,
                P.ID_WYNAGRODZENIA,
                P.ID_STANOWISKA,
                ST.NAZWA AS NAZWA_STANOWISKA,
                ST.CZY_ADMIN,
                P.ID_SALONU,
                S.NAZWA AS NAZWA_SALONU
            FROM PRACOWNICY P
            LEFT JOIN STANOWISKA ST ON P.ID_STANOWISKA = ST.ID_STANOWISKA
            LEFT JOIN SALONY_SAMOCHODOWE S ON P.ID_SALONU = S.ID_SALONU
            WHERE P.ID_PRACOWNIKA = ?
            """;

        List<EmployeeDTO> results = jdbc.query(sql, new EmployeeRowMapper(), id);
        return results.isEmpty() ? null : results.get(0);
    }

    /* =========================
       UPDATE EMPLOYEE
       ========================= */
    @Transactional
    public void update(EmployeeDTO employee) {
        jdbc.update("""
            UPDATE PRACOWNICY SET
                EMAIL = ?,
                IMIE = ?,
                NAZWISKO = ?,
                TELEFON = ?,
                ID_WYNAGRODZENIA = ?,
                ID_STANOWISKA = ?,
                ID_SALONU = ?
            WHERE ID_PRACOWNIKA = ?
            """,
                employee.getEmail(),
                employee.getImie(),
                employee.getNazwisko(),
                employee.getTelefon(),
                employee.getIdWynagrodzenia(),
                employee.getIdStanowiska(),
                employee.getIdSalonu(),
                employee.getIdPracownika()
        );
    }

    /* =========================
       UPDATE EMPLOYEE PASSWORD
       ========================= */
    public void updatePassword(Long employeeId, String passwordHash) {
        jdbc.update(
                "UPDATE PRACOWNICY SET HASLO = ? WHERE ID_PRACOWNIKA = ?",
                passwordHash,
                employeeId
        );
    }

    /* =========================
       DELETE EMPLOYEE
       ========================= */
    @Transactional
    public void delete(Long id) {
        jdbc.update("DELETE FROM PRACOWNICY WHERE ID_PRACOWNIKA = ?", id);
    }

    /* =========================
       GET ALL POSITIONS (for dropdown)
       ========================= */
    public List<PositionOption> getAllPositions() {
        String sql = "SELECT ID_STANOWISKA, NAZWA, CZY_ADMIN FROM STANOWISKA ORDER BY NAZWA";
        return jdbc.query(sql, (rs, rowNum) ->
                new PositionOption(
                        rs.getLong("ID_STANOWISKA"),
                        rs.getString("NAZWA"),
                        rs.getInt("CZY_ADMIN") == 1
                )
        );
    }

    /* =========================
       GET ALL SALONS (for dropdown)
       ========================= */
    public List<SalonOption> getAllSalons() {
        String sql = "SELECT ID_SALONU, NAZWA FROM SALONY_SAMOCHODOWE ORDER BY NAZWA";
        return jdbc.query(sql, (rs, rowNum) ->
                new SalonOption(rs.getLong("ID_SALONU"), rs.getString("NAZWA"))
        );
    }

    /* =========================
       ROW MAPPER
       ========================= */
    private static class EmployeeRowMapper implements RowMapper<EmployeeDTO> {
        @Override
        public EmployeeDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeDTO dto = new EmployeeDTO();
            dto.setIdPracownika(rs.getLong("ID_PRACOWNIKA"));
            dto.setEmail(rs.getString("EMAIL"));
            dto.setImie(rs.getString("IMIE"));
            dto.setNazwisko(rs.getString("NAZWISKO"));
            dto.setTelefon(rs.getString("TELEFON"));
            dto.setIdWynagrodzenia(rs.getBigDecimal("ID_WYNAGRODZENIA"));
            dto.setIdStanowiska(rs.getLong("ID_STANOWISKA"));
            dto.setNazwaStanowiska(rs.getString("NAZWA_STANOWISKA"));
            dto.setCzyAdmin(rs.getInt("CZY_ADMIN") == 1);
            dto.setIdSalonu(rs.getLong("ID_SALONU"));
            dto.setNazwaSalonu(rs.getString("NAZWA_SALONU"));
            return dto;
        }
    }

    /* =========================
       HELPER CLASSES
       ========================= */
    public record SalonOption(Long id, String name) {}
    public record PositionOption(Long id, String name, boolean isAdmin) {}
}