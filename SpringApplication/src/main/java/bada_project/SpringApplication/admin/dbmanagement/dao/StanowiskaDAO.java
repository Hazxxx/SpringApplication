package bada_project.SpringApplication.admin.dbmanagement.dao;

import bada_project.SpringApplication.admin.dbmanagement.models.Stanowisko;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StanowiskaDAO {

    private final JdbcTemplate jdbc;

    public StanowiskaDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Stanowisko> findAll() {
        String sql = """
            SELECT 
                s.id_stanowiska,
                s.nazwa,
                s.czy_admin,
                COUNT(p.id_pracownika) as liczba_pracownikow
            FROM STANOWISKA s
            LEFT JOIN PRACOWNICY p ON s.id_stanowiska = p.id_stanowiska
            GROUP BY s.id_stanowiska, s.nazwa, s.czy_admin
            ORDER BY s.nazwa
            """;
        return jdbc.query(sql, stanowiskoRowMapper());
    }

    public Optional<Stanowisko> findById(Long id) {
        String sql = """
            SELECT 
                s.id_stanowiska,
                s.nazwa,
                s.czy_admin,
                COUNT(p.id_pracownika) as liczba_pracownikow
            FROM STANOWISKA s
            LEFT JOIN PRACOWNICY p ON s.id_stanowiska = p.id_stanowiska
            WHERE s.id_stanowiska = ?
            GROUP BY s.id_stanowiska, s.nazwa, s.czy_admin
            """;
        List<Stanowisko> results = jdbc.query(sql, stanowiskoRowMapper(), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public void insert(Stanowisko stanowisko) {
        String sql = "INSERT INTO STANOWISKA (nazwa, czy_admin) VALUES (?, ?)";
        jdbc.update(sql,
                stanowisko.getNazwa(),
                stanowisko.getCzyAdmin() ? 1 : 0);
    }

    public void update(Stanowisko stanowisko) {
        String sql = "UPDATE STANOWISKA SET nazwa = ?, czy_admin = ? WHERE id_stanowiska = ?";
        jdbc.update(sql,
                stanowisko.getNazwa(),
                stanowisko.getCzyAdmin() ? 1 : 0,
                stanowisko.getIdStanowiska());
    }

    public void delete(Long id) {
        String sql = "DELETE FROM STANOWISKA WHERE id_stanowiska = ?";
        jdbc.update(sql, id);
    }

    public int countTotal() {
        String sql = "SELECT COUNT(*) FROM STANOWISKA";
        Integer count = jdbc.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    private RowMapper<Stanowisko> stanowiskoRowMapper() {
        return (rs, rowNum) -> {
            Stanowisko s = new Stanowisko();
            s.setIdStanowiska(rs.getLong("id_stanowiska"));
            s.setNazwa(rs.getString("nazwa"));
            s.setCzyAdmin(rs.getInt("czy_admin") == 1);
            s.setLiczbaPracownikow(rs.getInt("liczba_pracownikow"));
            return s;
        };
    }
}