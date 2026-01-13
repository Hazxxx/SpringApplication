package bada_project.SpringApplication.admin.dbmanagement.dao;

import bada_project.SpringApplication.admin.dbmanagement.models.Marka;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MarkiDAO {

    private final JdbcTemplate jdbc;

    public MarkiDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // ===== CRUD OPERATIONS =====

    public List<Marka> findAll() {
        String sql = """
            SELECT 
                m.id_marki,
                m.nazwa,
                COUNT(mo.id_modelu) as liczba_modeli
            FROM MARKI m
            LEFT JOIN MODELE mo ON m.id_marki = mo.id_marki
            GROUP BY m.id_marki, m.nazwa
            ORDER BY m.nazwa
            """;

        return jdbc.query(sql, markaRowMapper());
    }

    public Optional<Marka> findById(Long id) {
        String sql = """
            SELECT 
                m.id_marki,
                m.nazwa,
                COUNT(mo.id_modelu) as liczba_modeli
            FROM MARKI m
            LEFT JOIN MODELE mo ON m.id_marki = mo.id_marki
            WHERE m.id_marki = ?
            GROUP BY m.id_marki, m.nazwa
            """;

        List<Marka> results = jdbc.query(sql, markaRowMapper(), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public void insert(Marka marka) {
        String sql = "INSERT INTO MARKI (nazwa) VALUES (?)";
        jdbc.update(sql, marka.getNazwa());
    }

    public void update(Marka marka) {
        String sql = "UPDATE MARKI SET nazwa = ? WHERE id_marki = ?";
        jdbc.update(sql, marka.getNazwa(), marka.getIdMarki());
    }

    public void delete(Long id) {
        String sql = "DELETE FROM MARKI WHERE id_marki = ?";
        jdbc.update(sql, id);
    }

    // ===== BUSINESS LOGIC =====

    public boolean existsByNazwa(String nazwa) {
        String sql = "SELECT COUNT(*) FROM MARKI WHERE LOWER(nazwa) = LOWER(?)";
        Integer count = jdbc.queryForObject(sql, Integer.class, nazwa);
        return count != null && count > 0;
    }

    public boolean existsByNazwaExcludingId(String nazwa, Long excludeId) {
        String sql = "SELECT COUNT(*) FROM MARKI WHERE LOWER(nazwa) = LOWER(?) AND id_marki != ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, nazwa, excludeId);
        return count != null && count > 0;
    }

    public int countTotal() {
        String sql = "SELECT COUNT(*) FROM MARKI";
        Integer count = jdbc.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    // ===== ROW MAPPER =====

    private RowMapper<Marka> markaRowMapper() {
        return (rs, rowNum) -> {
            Marka m = new Marka();
            m.setIdMarki(rs.getLong("id_marki"));
            m.setNazwa(rs.getString("nazwa"));
            m.setLiczbaModeli(rs.getInt("liczba_modeli"));
            return m;
        };
    }
}
