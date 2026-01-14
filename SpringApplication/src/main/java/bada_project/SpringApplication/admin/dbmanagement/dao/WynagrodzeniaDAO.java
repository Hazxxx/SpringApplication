package bada_project.SpringApplication.admin.dbmanagement.dao;

import bada_project.SpringApplication.admin.dbmanagement.models.Wynagrodzenia;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class WynagrodzeniaDAO {

    private final JdbcTemplate jdbc;

    public WynagrodzeniaDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Wynagrodzenia> findAll() {
        String sql = """
            SELECT 
                id_wynagrodzenia,
                kwota,
                waluta,
                data_kontraktu
            FROM WYNAGRODZENIA
            ORDER BY data_kontraktu DESC
            """;
        return jdbc.query(sql, wynagrodzenieRowMapper());
    }

    public Optional<Wynagrodzenia> findById(Long id) {
        String sql = """
            SELECT 
                id_wynagrodzenia,
                kwota,
                waluta,
                data_kontraktu
            FROM WYNAGRODZENIA
            WHERE id_wynagrodzenia = ?
            """;
        List<Wynagrodzenia> results = jdbc.query(sql, wynagrodzenieRowMapper(), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public void insert(Wynagrodzenia wynagrodzenie) {
        String sql = "INSERT INTO WYNAGRODZENIA (kwota, waluta, data_kontraktu) VALUES (?, ?, ?)";
        jdbc.update(sql,
                wynagrodzenie.getKwota(),
                wynagrodzenie.getWaluta(),
                wynagrodzenie.getDataKontraktu());
    }

    public void update(Wynagrodzenia wynagrodzenie) {
        String sql = "UPDATE WYNAGRODZENIA SET kwota = ?, waluta = ?, data_kontraktu = ? WHERE id_wynagrodzenia = ?";
        jdbc.update(sql,
                wynagrodzenie.getKwota(),
                wynagrodzenie.getWaluta(),
                wynagrodzenie.getDataKontraktu(),
                wynagrodzenie.getIdWynagrodzenia());
    }

    public void delete(Long id) {
        String sql = "DELETE FROM WYNAGRODZENIA WHERE id_wynagrodzenia = ?";
        jdbc.update(sql, id);
    }

    public int countTotal() {
        String sql = "SELECT COUNT(*) FROM WYNAGRODZENIA";
        Integer count = jdbc.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    private RowMapper<Wynagrodzenia> wynagrodzenieRowMapper() {
        return (rs, rowNum) -> {
            Wynagrodzenia w = new Wynagrodzenia();
            w.setIdWynagrodzenia(rs.getLong("id_wynagrodzenia"));
            w.setKwota(rs.getBigDecimal("kwota"));
            w.setWaluta(rs.getString("waluta"));
            w.setDataKontraktu(rs.getDate("data_kontraktu"));
            return w;
        };
    }
}