package bada_project.SpringApplication.admin.dbmanagement.dao;

import bada_project.SpringApplication.admin.dbmanagement.models.Sprzedaze;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SprzedazeDAO {

    private final JdbcTemplate jdbc;

    public SprzedazeDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Sprzedaze> findAll() {
        String sql = """
            SELECT 
                s.id_sprzedazy,
                s.id_pracownika,
                s.id_klienta,
                s.id_oferty,
                s.kwota_sprzedazy,
                s.data_sprzedazy,
                p.imie as imie_pracownika,
                p.nazwisko as nazwisko_pracownika,
                k.imie as imie_klienta,
                k.nazwisko as nazwisko_klienta,
                poj.VIN as vin_pojazdu,
                ma.nazwa as nazwa_marki,
                o.cena_katalogowa
            FROM SPRZEDAZE s
            LEFT JOIN PRACOWNICY p ON s.id_pracownika = p.id_pracownika
            LEFT JOIN KLIENCI k ON s.id_klienta = k.id_klienta
            LEFT JOIN OFERTY o ON s.id_oferty = o.id_oferty
            LEFT JOIN POJAZDY poj ON o.id_pojazdu = poj.id_pojazdu
            LEFT JOIN MODELE mo ON poj.id_modelu = mo.id_modelu
            LEFT JOIN MARKI ma ON mo.id_marki = ma.id_marki
            ORDER BY s.data_sprzedazy DESC
            """;
        return jdbc.query(sql, sprzedazRowMapper());
    }

    public Optional<Sprzedaze> findById(Long id) {
        String sql = """
            SELECT 
                s.id_sprzedazy,
                s.id_pracownika,
                s.id_klienta,
                s.id_oferty,
                s.kwota_sprzedazy,
                s.data_sprzedazy,
                p.imie as imie_pracownika,
                p.nazwisko as nazwisko_pracownika,
                k.imie as imie_klienta,
                k.nazwisko as nazwisko_klienta,
                poj.VIN as vin_pojazdu,
                ma.nazwa as nazwa_marki,
                o.cena_katalogowa
            FROM SPRZEDAZE s
            LEFT JOIN PRACOWNICY p ON s.id_pracownika = p.id_pracownika
            LEFT JOIN KLIENCI k ON s.id_klienta = k.id_klienta
            LEFT JOIN OFERTY o ON s.id_oferty = o.id_oferty
            LEFT JOIN POJAZDY poj ON o.id_pojazdu = poj.id_pojazdu
            LEFT JOIN MODELE mo ON poj.id_modelu = mo.id_modelu
            LEFT JOIN MARKI ma ON mo.id_marki = ma.id_marki
            WHERE s.id_sprzedazy = ?
            """;
        List<Sprzedaze> results = jdbc.query(sql, sprzedazRowMapper(), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public void insert(Sprzedaze sprzedaz) {
        // For Oracle: Use sequence to generate ID
        String sql = """
            INSERT INTO SPRZEDAZE (id_sprzedazy, id_pracownika, id_klienta, id_oferty, kwota_sprzedazy, data_sprzedazy)
            VALUES (sprzedaze_seq.NEXTVAL, ?, ?, ?, ?, ?)
            """;
        jdbc.update(sql,
                sprzedaz.getIdPracownika(),
                sprzedaz.getIdKlienta(),
                sprzedaz.getIdOferty(),
                sprzedaz.getKwotaSprzedazy(),
                sprzedaz.getDataSprzedazy());
    }

    public void update(Sprzedaze sprzedaz) {
        String sql = """
            UPDATE SPRZEDAZE SET 
                id_pracownika = ?,
                id_klienta = ?,
                id_oferty = ?,
                kwota_sprzedazy = ?,
                data_sprzedazy = ?
            WHERE id_sprzedazy = ?
            """;
        jdbc.update(sql,
                sprzedaz.getIdPracownika(),
                sprzedaz.getIdKlienta(),
                sprzedaz.getIdOferty(),
                sprzedaz.getKwotaSprzedazy(),
                sprzedaz.getDataSprzedazy(),
                sprzedaz.getIdSprzedazy());
    }

    public void delete(Long id) {
        String sql = "DELETE FROM SPRZEDAZE WHERE id_sprzedazy = ?";
        jdbc.update(sql, id);
    }

    public int countTotal() {
        String sql = "SELECT COUNT(*) FROM SPRZEDAZE";
        Integer count = jdbc.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    private RowMapper<Sprzedaze> sprzedazRowMapper() {
        return (rs, rowNum) -> {
            Sprzedaze s = new Sprzedaze();
            s.setIdSprzedazy(rs.getLong("id_sprzedazy"));
            s.setIdPracownika(rs.getLong("id_pracownika"));
            s.setIdKlienta(rs.getLong("id_klienta"));
            s.setIdOferty(rs.getLong("id_oferty"));
            s.setKwotaSprzedazy(rs.getBigDecimal("kwota_sprzedazy"));
            s.setDataSprzedazy(rs.getDate("data_sprzedazy"));
            s.setImiePracownika(rs.getString("imie_pracownika"));
            s.setNazwiskoPracownika(rs.getString("nazwisko_pracownika"));
            s.setImieKlienta(rs.getString("imie_klienta"));
            s.setNazwiskoKlienta(rs.getString("nazwisko_klienta"));
            s.setVinPojazdu(rs.getString("vin_pojazdu"));
            s.setNazwaMarki(rs.getString("nazwa_marki"));
            s.setCenaKatalogowa(rs.getBigDecimal("cena_katalogowa"));
            return s;
        };
    }
}