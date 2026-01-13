package bada_project.SpringApplication.admin.dbmanagement.dao;

import bada_project.SpringApplication.admin.dbmanagement.models.ModelSamochodu;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ModeleDAO {

    private final JdbcTemplate jdbc;

    public ModeleDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<ModelSamochodu> findAll() {
        String sql = """
            SELECT 
                m.id_modelu,
                m.id_marki,
                mar.nazwa as nazwa_marki,
                m.pojemnosc_silnika,
                m.moc_silnika,
                m.typ_paliwa,
                m.rocznik_modelowy,
                m.typ_nadwozia,
                m.masa_wlasna,
                COUNT(p.id_pojazdu) as liczba_pojazdow
            FROM MODELE m
            INNER JOIN MARKI mar ON m.id_marki = mar.id_marki
            LEFT JOIN POJAZDY p ON m.id_modelu = p.id_modelu
            GROUP BY m.id_modelu, m.id_marki, mar.nazwa, m.pojemnosc_silnika, 
                     m.moc_silnika, m.typ_paliwa, m.rocznik_modelowy, 
                     m.typ_nadwozia, m.masa_wlasna
            ORDER BY mar.nazwa, m.rocznik_modelowy DESC
            """;
        return jdbc.query(sql, modelRowMapper());
    }

    public Optional<ModelSamochodu> findById(Long id) {
        String sql = """
            SELECT 
                m.id_modelu,
                m.id_marki,
                mar.nazwa as nazwa_marki,
                m.pojemnosc_silnika,
                m.moc_silnika,
                m.typ_paliwa,
                m.rocznik_modelowy,
                m.typ_nadwozia,
                m.masa_wlasna,
                COUNT(p.id_pojazdu) as liczba_pojazdow
            FROM MODELE m
            INNER JOIN MARKI mar ON m.id_marki = mar.id_marki
            LEFT JOIN POJAZDY p ON m.id_modelu = p.id_modelu
            WHERE m.id_modelu = ?
            GROUP BY m.id_modelu, m.id_marki, mar.nazwa, m.pojemnosc_silnika, 
                     m.moc_silnika, m.typ_paliwa, m.rocznik_modelowy, 
                     m.typ_nadwozia, m.masa_wlasna
            """;
        List<ModelSamochodu> results = jdbc.query(sql, modelRowMapper(), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public void insert(ModelSamochodu model) {
        String sql = """
            INSERT INTO MODELE 
            (id_marki, pojemnosc_silnika, moc_silnika, typ_paliwa, 
             rocznik_modelowy, typ_nadwozia, masa_wlasna)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        jdbc.update(sql,
                model.getIdMarki(),
                model.getPojemnoscSilnika(),
                model.getMocSilnika(),
                model.getTypPaliwa(),
                model.getRocznikModelowy(),
                model.getTypNadwozia(),
                model.getMasaWlasna());
    }

    public void update(ModelSamochodu model) {
        String sql = """
            UPDATE MODELE SET 
                id_marki = ?,
                pojemnosc_silnika = ?,
                moc_silnika = ?,
                typ_paliwa = ?,
                rocznik_modelowy = ?,
                typ_nadwozia = ?,
                masa_wlasna = ?
            WHERE id_modelu = ?
            """;
        jdbc.update(sql,
                model.getIdMarki(),
                model.getPojemnoscSilnika(),
                model.getMocSilnika(),
                model.getTypPaliwa(),
                model.getRocznikModelowy(),
                model.getTypNadwozia(),
                model.getMasaWlasna(),
                model.getIdModelu());
    }

    public void delete(Long id) {
        String sql = "DELETE FROM MODELE WHERE id_modelu = ?";
        jdbc.update(sql, id);
    }

    public int countTotal() {
        String sql = "SELECT COUNT(*) FROM MODELE";
        Integer count = jdbc.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    private RowMapper<ModelSamochodu> modelRowMapper() {
        return (rs, rowNum) -> {
            ModelSamochodu m = new ModelSamochodu();
            m.setIdModelu(rs.getLong("id_modelu"));
            m.setIdMarki(rs.getLong("id_marki"));
            m.setNazwaMarki(rs.getString("nazwa_marki"));
            m.setPojemnoscSilnika(rs.getInt("pojemnosc_silnika"));
            m.setMocSilnika(rs.getInt("moc_silnika"));
            m.setTypPaliwa(rs.getString("typ_paliwa"));
            m.setRocznikModelowy(rs.getInt("rocznik_modelowy"));
            m.setTypNadwozia(rs.getString("typ_nadwozia"));
            m.setMasaWlasna(rs.getInt("masa_wlasna"));
            m.setLiczbaPojazdow(rs.getInt("liczba_pojazdow"));
            return m;
        };
    }
}