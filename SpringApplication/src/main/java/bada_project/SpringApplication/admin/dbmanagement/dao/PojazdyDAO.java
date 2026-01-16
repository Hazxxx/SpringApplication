package bada_project.SpringApplication.admin.dbmanagement.dao;

import bada_project.SpringApplication.admin.dbmanagement.models.Pojazdy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PojazdyDAO {

    private final JdbcTemplate jdbc;

    public PojazdyDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Pojazdy> findAll() {
        String sql = """
            SELECT 
                p.id_pojazdu,
                p.kolor,
                p.VIN,
                p.id_modelu,
                p.zdjecie_url,
                mo.rocznik_modelowy,
                mo.typ_nadwozia,
                ma.nazwa as nazwa_marki,
                COUNT(o.id_oferty) as liczba_ofert
            FROM POJAZDY p
            LEFT JOIN MODELE mo ON p.id_modelu = mo.id_modelu
            LEFT JOIN MARKI ma ON mo.id_marki = ma.id_marki
            LEFT JOIN OFERTY o ON p.id_pojazdu = o.id_pojazdu
            GROUP BY p.id_pojazdu, p.kolor, p.VIN, p.id_modelu, p.zdjecie_url,
                     mo.rocznik_modelowy, mo.typ_nadwozia, ma.nazwa
            ORDER BY p.id_pojazdu DESC
            """;
        return jdbc.query(sql, pojazdRowMapper());
    }

    public Optional<Pojazdy> findById(Long id) {
        String sql = """
            SELECT 
                p.id_pojazdu,
                p.kolor,
                p.VIN,
                p.id_modelu,
                p.zdjecie_url,
                mo.rocznik_modelowy,
                mo.typ_nadwozia,
                ma.nazwa as nazwa_marki,
                COUNT(o.id_oferty) as liczba_ofert
            FROM POJAZDY p
            LEFT JOIN MODELE mo ON p.id_modelu = mo.id_modelu
            LEFT JOIN MARKI ma ON mo.id_marki = ma.id_marki
            LEFT JOIN OFERTY o ON p.id_pojazdu = o.id_pojazdu
            WHERE p.id_pojazdu = ?
            GROUP BY p.id_pojazdu, p.kolor, p.VIN, p.id_modelu, p.zdjecie_url,
                     mo.rocznik_modelowy, mo.typ_nadwozia, ma.nazwa
            """;
        List<Pojazdy> results = jdbc.query(sql, pojazdRowMapper(), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public void insert(Pojazdy pojazd) {
        String sql = "INSERT INTO POJAZDY (kolor, VIN, id_modelu, zdjecie_url) VALUES (?, ?, ?, ?)";
        jdbc.update(sql, pojazd.getKolor(), pojazd.getVin(), pojazd.getIdModelu(), pojazd.getZdjecieUrl());
    }

    public void update(Pojazdy pojazd) {
        String sql = "UPDATE POJAZDY SET kolor = ?, VIN = ?, id_modelu = ?, zdjecie_url = ? WHERE id_pojazdu = ?";
        jdbc.update(sql, pojazd.getKolor(), pojazd.getVin(), pojazd.getIdModelu(),
                pojazd.getZdjecieUrl(), pojazd.getIdPojazdu());
    }

    public void delete(Long id) {
        String sql = "DELETE FROM POJAZDY WHERE id_pojazdu = ?";
        jdbc.update(sql, id);
    }

    public int countTotal() {
        String sql = "SELECT COUNT(*) FROM POJAZDY";
        Integer count = jdbc.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    private RowMapper<Pojazdy> pojazdRowMapper() {
        return (rs, rowNum) -> {
            Pojazdy p = new Pojazdy();
            p.setIdPojazdu(rs.getLong("id_pojazdu"));
            p.setKolor(rs.getString("kolor"));
            p.setVin(rs.getString("VIN"));
            p.setIdModelu(rs.getLong("id_modelu"));
            p.setZdjecieUrl(rs.getString("zdjecie_url"));
            p.setRocznikModelowy(rs.getInt("rocznik_modelowy"));
            p.setTypNadwozia(rs.getString("typ_nadwozia"));
            p.setNazwaMarki(rs.getString("nazwa_marki"));
            p.setLiczbaOfert(rs.getInt("liczba_ofert"));
            return p;
        };
    }
}