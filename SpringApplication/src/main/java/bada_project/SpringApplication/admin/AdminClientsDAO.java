package bada_project.SpringApplication.admin;

import bada_project.SpringApplication.admin.dto.ClientDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AdminClientsDAO {

    private final JdbcTemplate jdbc;

    public AdminClientsDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /* =========================
       FETCH ALL CLIENTS
       ========================= */
    public List<ClientDTO> findAll() {
        String sql = """
            SELECT 
                K.ID_KLIENTA,
                K.EMAIL,
                K.IMIE,
                K.NAZWISKO,
                K.TELEFON,
                K.ID_ADRESU,
                A.MIASTO,
                A.ULICA,
                A.NUMER_BUDYNKU,
                A.NUMER_LOKALU,
                A.KOD_POCZTOWY,
                A.KRAJ,
                K.ID_SALONU,
                S.NAZWA AS NAZWA_SALONU
            FROM KLIENCI K
            LEFT JOIN ADRESY A ON K.ID_ADRESU = A.ID_ADRESU
            LEFT JOIN SALONY_SAMOCHODOWE S ON K.ID_SALONU = S.ID_SALONU
            ORDER BY K.ID_KLIENTA DESC
            """;

        return jdbc.query(sql, new ClientRowMapper());
    }

    /* =========================
       FETCH CLIENT BY ID
       ========================= */
    public ClientDTO findById(Long id) {
        String sql = """
            SELECT 
                K.ID_KLIENTA,
                K.EMAIL,
                K.IMIE,
                K.NAZWISKO,
                K.TELEFON,
                K.ID_ADRESU,
                A.MIASTO,
                A.ULICA,
                A.NUMER_BUDYNKU,
                A.NUMER_LOKALU,
                A.KOD_POCZTOWY,
                A.KRAJ,
                K.ID_SALONU,
                S.NAZWA AS NAZWA_SALONU
            FROM KLIENCI K
            LEFT JOIN ADRESY A ON K.ID_ADRESU = A.ID_ADRESU
            LEFT JOIN SALONY_SAMOCHODOWE S ON K.ID_SALONU = S.ID_SALONU
            WHERE K.ID_KLIENTA = ?
            """;

        List<ClientDTO> results = jdbc.query(sql, new ClientRowMapper(), id);
        return results.isEmpty() ? null : results.get(0);
    }

    /* =========================
       UPDATE CLIENT
       ========================= */
    @Transactional
    public void update(ClientDTO client) {
        // Update address
        jdbc.update("""
            UPDATE ADRESY SET
                MIASTO = ?,
                ULICA = ?,
                NUMER_BUDYNKU = ?,
                NUMER_LOKALU = ?,
                KOD_POCZTOWY = ?,
                KRAJ = ?
            WHERE ID_ADRESU = ?
            """,
                client.getMiasto(),
                client.getUlica(),
                client.getNumerBudynku(),
                client.getNumerLokalu(),
                client.getKodPocztowy(),
                client.getKraj(),
                client.getIdAdresu()
        );

        // Update client
        jdbc.update("""
            UPDATE KLIENCI SET
                EMAIL = ?,
                IMIE = ?,
                NAZWISKO = ?,
                TELEFON = ?,
                ID_SALONU = ?
            WHERE ID_KLIENTA = ?
            """,
                client.getEmail(),
                client.getImie(),
                client.getNazwisko(),
                client.getTelefon(),
                client.getIdSalonu(),
                client.getIdKlienta()
        );
    }

    /* =========================
       DELETE CLIENT
       ========================= */
    @Transactional
    public void delete(Long id) {
        ClientDTO client = findById(id);
        if (client == null) {
            throw new IllegalArgumentException("Client not found: " + id);
        }

        // Delete client (cascade should handle related records)
        jdbc.update("DELETE FROM KLIENCI WHERE ID_KLIENTA = ?", id);

        // Optionally delete address if not used by others
        jdbc.update("DELETE FROM ADRESY WHERE ID_ADRESU = ?", client.getIdAdresu());
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
    private static class ClientRowMapper implements RowMapper<ClientDTO> {
        @Override
        public ClientDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            ClientDTO dto = new ClientDTO();
            dto.setIdKlienta(rs.getLong("ID_KLIENTA"));
            dto.setEmail(rs.getString("EMAIL"));
            dto.setImie(rs.getString("IMIE"));
            dto.setNazwisko(rs.getString("NAZWISKO"));
            dto.setTelefon(rs.getString("TELEFON"));
            dto.setIdAdresu(rs.getLong("ID_ADRESU"));
            dto.setMiasto(rs.getString("MIASTO"));
            dto.setUlica(rs.getString("ULICA"));
            dto.setNumerBudynku(rs.getString("NUMER_BUDYNKU"));
            dto.setNumerLokalu(rs.getString("NUMER_LOKALU"));
            dto.setKodPocztowy(rs.getString("KOD_POCZTOWY"));
            dto.setKraj(rs.getString("KRAJ"));
            dto.setIdSalonu(rs.getLong("ID_SALONU"));
            dto.setNazwaSalonu(rs.getString("NAZWA_SALONU"));
            return dto;
        }
    }

    /* =========================
       HELPER CLASSES
       ========================= */
    public record SalonOption(Long id, String name) {}
}