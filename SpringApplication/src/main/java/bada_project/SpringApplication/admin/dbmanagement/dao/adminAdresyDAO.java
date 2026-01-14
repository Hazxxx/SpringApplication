package bada_project.SpringApplication.admin.dbmanagement.dao;

import bada_project.SpringApplication.admin.dbmanagement.models.Adresy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("adminAdresyDAO")
public class adminAdresyDAO {

    private final JdbcTemplate jdbc;

    public adminAdresyDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Adresy> findAll() {
        String sql = """
            SELECT 
                id_adresu,
                miasto,
                ulica,
                numer_budynku,
                numer_lokalu,
                kod_pocztowy,
                kraj
            FROM ADRESY
            ORDER BY miasto, ulica
            """;
        return jdbc.query(sql, adresRowMapper());
    }

    public Optional<Adresy> findById(Long id) {
        String sql = """
            SELECT 
                id_adresu,
                miasto,
                ulica,
                numer_budynku,
                numer_lokalu,
                kod_pocztowy,
                kraj
            FROM ADRESY
            WHERE id_adresu = ?
            """;
        List<Adresy> results = jdbc.query(sql, adresRowMapper(), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public void insert(Adresy adres) {
        String sql = """
            INSERT INTO ADRESY (miasto, ulica, numer_budynku, numer_lokalu, kod_pocztowy, kraj)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        jdbc.update(sql,
                adres.getMiasto(),
                adres.getUlica(),
                adres.getNumerBudynku(),
                adres.getNumerLokalu() != null ? adres.getNumerLokalu() : 0,
                adres.getKodPocztowy(),
                adres.getKraj() != null ? adres.getKraj() : "Polska");
    }

    public void update(Adresy adres) {
        String sql = """
            UPDATE ADRESY SET 
                miasto = ?,
                ulica = ?,
                numer_budynku = ?,
                numer_lokalu = ?,
                kod_pocztowy = ?,
                kraj = ?
            WHERE id_adresu = ?
            """;
        jdbc.update(sql,
                adres.getMiasto(),
                adres.getUlica(),
                adres.getNumerBudynku(),
                adres.getNumerLokalu() != null ? adres.getNumerLokalu() : 0,
                adres.getKodPocztowy(),
                adres.getKraj() != null ? adres.getKraj() : "Polska",
                adres.getIdAdresu());
    }

    public void delete(Long id) {
        String sql = "DELETE FROM ADRESY WHERE id_adresu = ?";
        jdbc.update(sql, id);
    }

    public int countTotal() {
        String sql = "SELECT COUNT(*) FROM ADRESY";
        Integer count = jdbc.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    private RowMapper<Adresy> adresRowMapper() {
        return (rs, rowNum) -> {
            Adresy a = new Adresy();
            a.setIdAdresu(rs.getLong("id_adresu"));
            a.setMiasto(rs.getString("miasto"));
            a.setUlica(rs.getString("ulica"));
            a.setNumerBudynku(rs.getInt("numer_budynku"));
            a.setNumerLokalu(rs.getInt("numer_lokalu"));
            a.setKodPocztowy(rs.getString("kod_pocztowy"));
            a.setKraj(rs.getString("kraj"));
            return a;
        };
    }
}