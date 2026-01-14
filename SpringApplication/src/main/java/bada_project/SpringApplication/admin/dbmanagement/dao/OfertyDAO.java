package bada_project.SpringApplication.admin.dbmanagement.dao;

import bada_project.SpringApplication.admin.dbmanagement.models.Oferty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OfertyDAO {

    private final JdbcTemplate jdbc;

    public OfertyDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Oferty> findAll() {
        String sql = """
            SELECT 
                o.id_oferty,
                o.id_salonu,
                o.id_pojazdu,
                o.cena_katalogowa,
                s.nazwa as nazwa_salonu,
                p.VIN as vin_pojazdu,
                ma.nazwa as nazwa_marki,
                COUNT(sp.id_sprzedazy) as liczba_sprzedazy
            FROM OFERTY o
            LEFT JOIN SALONY_SAMOCHODOWE s ON o.id_salonu = s.id_salonu
            LEFT JOIN POJAZDY p ON o.id_pojazdu = p.id_pojazdu
            LEFT JOIN MODELE mo ON p.id_modelu = mo.id_modelu
            LEFT JOIN MARKI ma ON mo.id_marki = ma.id_marki
            LEFT JOIN SPRZEDAZE sp ON o.id_oferty = sp.id_oferty
            GROUP BY o.id_oferty, o.id_salonu, o.id_pojazdu, o.cena_katalogowa,
                     s.nazwa, p.VIN, ma.nazwa
            ORDER BY o.id_oferty DESC
            """;
        return jdbc.query(sql, ofertaRowMapper());
    }

    public Optional<Oferty> findById(Long id) {
        String sql = """
            SELECT 
                o.id_oferty,
                o.id_salonu,
                o.id_pojazdu,
                o.cena_katalogowa,
                s.nazwa as nazwa_salonu,
                p.VIN as vin_pojazdu,
                ma.nazwa as nazwa_marki,
                COUNT(sp.id_sprzedazy) as liczba_sprzedazy
            FROM OFERTY o
            LEFT JOIN SALONY_SAMOCHODOWE s ON o.id_salonu = s.id_salonu
            LEFT JOIN POJAZDY p ON o.id_pojazdu = p.id_pojazdu
            LEFT JOIN MODELE mo ON p.id_modelu = mo.id_modelu
            LEFT JOIN MARKI ma ON mo.id_marki = ma.id_marki
            LEFT JOIN SPRZEDAZE sp ON o.id_oferty = sp.id_oferty
            WHERE o.id_oferty = ?
            GROUP BY o.id_oferty, o.id_salonu, o.id_pojazdu, o.cena_katalogowa,
                     s.nazwa, p.VIN, ma.nazwa
            """;
        List<Oferty> results = jdbc.query(sql, ofertaRowMapper(), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public void insert(Oferty oferta) {
        String sql = "INSERT INTO OFERTY (id_salonu, id_pojazdu, cena_katalogowa) VALUES (?, ?, ?)";
        jdbc.update(sql, oferta.getIdSalonu(), oferta.getIdPojazdu(), oferta.getCenaKatalogowa());
    }

    public void update(Oferty oferta) {
        String sql = "UPDATE OFERTY SET id_salonu = ?, id_pojazdu = ?, cena_katalogowa = ? WHERE id_oferty = ?";
        jdbc.update(sql, oferta.getIdSalonu(), oferta.getIdPojazdu(), oferta.getCenaKatalogowa(), oferta.getIdOferty());
    }

    public void delete(Long id) {
        String sql = "DELETE FROM OFERTY WHERE id_oferty = ?";
        jdbc.update(sql, id);
    }

    public int countTotal() {
        String sql = "SELECT COUNT(*) FROM OFERTY";
        Integer count = jdbc.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    private RowMapper<Oferty> ofertaRowMapper() {
        return (rs, rowNum) -> {
            Oferty o = new Oferty();
            o.setIdOferty(rs.getLong("id_oferty"));
            o.setIdSalonu(rs.getLong("id_salonu"));
            o.setIdPojazdu(rs.getLong("id_pojazdu"));
            o.setCenaKatalogowa(rs.getBigDecimal("cena_katalogowa"));
            o.setNazwaSalonu(rs.getString("nazwa_salonu"));
            o.setVinPojazdu(rs.getString("vin_pojazdu"));
            o.setNazwaMarki(rs.getString("nazwa_marki"));
            o.setLiczbaSprzedazy(rs.getInt("liczba_sprzedazy"));
            return o;
        };
    }
}