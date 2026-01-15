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
            SELECT s.id_stanowiska,
                   s.nazwa,
                   s.minimalne_doswiadczenie_lata,
                   s.czy_kierownicze,
                   s.czy_admin,
                   COUNT(p.id_pracownika) liczba_pracownikow
            FROM STANOWISKA s
            LEFT JOIN PRACOWNICY p ON p.id_stanowiska = s.id_stanowiska
            GROUP BY s.id_stanowiska, s.nazwa,
                     s.minimalne_doswiadczenie_lata,
                     s.czy_kierownicze, s.czy_admin
            ORDER BY s.nazwa
        """;
        return jdbc.query(sql, mapper());
    }

    public Optional<Stanowisko> findById(Long id) {
        String sql = """
            SELECT s.id_stanowiska,
                   s.nazwa,
                   s.minimalne_doswiadczenie_lata,
                   s.czy_kierownicze,
                   s.czy_admin,
                   0 liczba_pracownikow
            FROM STANOWISKA s
            WHERE s.id_stanowiska = ?
        """;
        return jdbc.query(sql, mapper(), id).stream().findFirst();
    }

    public void insert(Stanowisko s) {
        String sql = """
            INSERT INTO STANOWISKA
            (nazwa, minimalne_doswiadczenie_lata, czy_kierownicze, czy_admin)
            VALUES (?, ?, ?, ?)
        """;

        jdbc.update(sql,
                s.getNazwa(),
                s.getMinimalneDoswiadczenieLata(),
                s.getCzyKierownicze() ? 1 : 0,
                s.getCzyAdmin() ? 1 : 0
        );
    }

    public void update(Stanowisko s) {
        String sql = """
            UPDATE STANOWISKA
            SET nazwa = ?,
                minimalne_doswiadczenie_lata = ?,
                czy_kierownicze = ?,
                czy_admin = ?
            WHERE id_stanowiska = ?
        """;

        jdbc.update(sql,
                s.getNazwa(),
                s.getMinimalneDoswiadczenieLata(),
                s.getCzyKierownicze() ? 1 : 0,
                s.getCzyAdmin() ? 1 : 0,
                s.getIdStanowiska()
        );
    }

    public void delete(Long id) {
        jdbc.update("DELETE FROM STANOWISKA WHERE id_stanowiska = ?", id);
    }

    private RowMapper<Stanowisko> mapper() {
        return (rs, rowNum) -> {
            Stanowisko s = new Stanowisko();
            s.setIdStanowiska(rs.getLong("id_stanowiska"));
            s.setNazwa(rs.getString("nazwa"));
            s.setMinimalneDoswiadczenieLata(
                    rs.getInt("minimalne_doswiadczenie_lata")
            );
            s.setCzyKierownicze(rs.getInt("czy_kierownicze") == 1);
            s.setCzyAdmin(rs.getInt("czy_admin") == 1);
            s.setLiczbaPracownikow(rs.getInt("liczba_pracownikow"));
            return s;
        };
    }

}
