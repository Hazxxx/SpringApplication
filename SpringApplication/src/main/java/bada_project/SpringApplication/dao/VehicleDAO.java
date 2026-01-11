package bada_project.SpringApplication.dao;

import bada_project.SpringApplication.user.vehicles.Vehicle;
import bada_project.SpringApplication.user.vehicles.VehicleSearchFilter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VehicleDAO {

    private final JdbcTemplate jdbc;

    public VehicleDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Pobierz wszystkie dostępne pojazdy (nie sprzedane)
     */
    public List<Vehicle> findAllAvailable() {
        String sql = """
            SELECT 
                p.id_pojazdu,
                p.kolor,
                p.vin,
                p.id_modelu,
                m.pojemnosc_silnika,
                m.moc_silnika,
                m.typ_paliwa,
                m.rocznik_modelowy,
                m.typ_nadwozia,
                m.masa_wlasna,
                m.id_marki,
                mar.nazwa as nazwa_marki,
                o.id_oferty,
                o.cena_katalogowa,
                o.id_salonu,
                s.nazwa as nazwa_salonu,
                s.telefon as telefon_salonu,
                a.miasto as miasto_salonu,
                a.ulica as ulica_salonu,
                CASE WHEN sp.id_sprzedazy IS NULL THEN 0 ELSE 1 END as sprzedany
            FROM POJAZDY p
            INNER JOIN MODELE m ON p.id_modelu = m.id_modelu
            INNER JOIN MARKI mar ON m.id_marki = mar.id_marki
            LEFT JOIN OFERTY o ON p.id_pojazdu = o.id_pojazdu
            LEFT JOIN SALONY_SAMOCHODOWE s ON o.id_salonu = s.id_salonu
            LEFT JOIN ADRESY a ON s.id_adresu = a.id_adresu
            LEFT JOIN SPRZEDAZE sp ON o.id_oferty = sp.id_oferty
            WHERE sp.id_sprzedazy IS NULL
            ORDER BY o.id_oferty DESC
            """;

        return jdbc.query(sql, new VehicleRowMapper());
    }

    /**
     * Pobierz pojazd po ID
     */
    public Vehicle findById(Integer idPojazdu) {
        String sql = """
            SELECT 
                p.id_pojazdu,
                p.kolor,
                p.vin,
                p.id_modelu,
                m.pojemnosc_silnika,
                m.moc_silnika,
                m.typ_paliwa,
                m.rocznik_modelowy,
                m.typ_nadwozia,
                m.masa_wlasna,
                m.id_marki,
                mar.nazwa as nazwa_marki,
                o.id_oferty,
                o.cena_katalogowa,
                o.id_salonu,
                s.nazwa as nazwa_salonu,
                s.telefon as telefon_salonu,
                a.miasto as miasto_salonu,
                a.ulica as ulica_salonu,
                CASE WHEN sp.id_sprzedazy IS NULL THEN 0 ELSE 1 END as sprzedany
            FROM POJAZDY p
            INNER JOIN MODELE m ON p.id_modelu = m.id_modelu
            INNER JOIN MARKI mar ON m.id_marki = mar.id_marki
            LEFT JOIN OFERTY o ON p.id_pojazdu = o.id_pojazdu
            LEFT JOIN SALONY_SAMOCHODOWE s ON o.id_salonu = s.id_salonu
            LEFT JOIN ADRESY a ON s.id_adresu = a.id_adresu
            LEFT JOIN SPRZEDAZE sp ON o.id_oferty = sp.id_oferty
            WHERE p.id_pojazdu = ?
            """;

        List<Vehicle> results = jdbc.query(sql, new VehicleRowMapper(), idPojazdu);
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * Wyszukaj pojazdy z filtrami
     */
    public List<Vehicle> search(VehicleSearchFilter filter) {
        StringBuilder sql = new StringBuilder("""
            SELECT 
                p.id_pojazdu,
                p.kolor,
                p.vin,
                p.id_modelu,
                m.pojemnosc_silnika,
                m.moc_silnika,
                m.typ_paliwa,
                m.rocznik_modelowy,
                m.typ_nadwozia,
                m.masa_wlasna,
                m.id_marki,
                mar.nazwa as nazwa_marki,
                o.id_oferty,
                o.cena_katalogowa,
                o.id_salonu,
                s.nazwa as nazwa_salonu,
                s.telefon as telefon_salonu,
                a.miasto as miasto_salonu,
                a.ulica as ulica_salonu,
                CASE WHEN sp.id_sprzedazy IS NULL THEN 0 ELSE 1 END as sprzedany
            FROM POJAZDY p
            INNER JOIN MODELE m ON p.id_modelu = m.id_modelu
            INNER JOIN MARKI mar ON m.id_marki = mar.id_marki
            LEFT JOIN OFERTY o ON p.id_pojazdu = o.id_pojazdu
            LEFT JOIN SALONY_SAMOCHODOWE s ON o.id_salonu = s.id_salonu
            LEFT JOIN ADRESY a ON s.id_adresu = a.id_adresu
            LEFT JOIN SPRZEDAZE sp ON o.id_oferty = sp.id_oferty
            WHERE sp.id_sprzedazy IS NULL
            """);

        List<Object> params = new ArrayList<>();

        // Filtr marki
        if (filter.getNazwaMarki() != null && !filter.getNazwaMarki().isEmpty()) {
            sql.append(" AND LOWER(mar.nazwa) LIKE LOWER(?)");
            params.add("%" + filter.getNazwaMarki() + "%");
        }

        // Filtr typu nadwozia
        if (filter.getTypNadwozia() != null && !filter.getTypNadwozia().isEmpty()) {
            sql.append(" AND m.typ_nadwozia = ?");
            params.add(filter.getTypNadwozia());
        }

        // Filtr typu paliwa
        if (filter.getTypPaliwa() != null && !filter.getTypPaliwa().isEmpty()) {
            sql.append(" AND m.typ_paliwa = ?");
            params.add(filter.getTypPaliwa());
        }

        // Filtr ceny od
        if (filter.getCenaOd() != null) {
            sql.append(" AND o.cena_katalogowa >= ?");
            params.add(filter.getCenaOd());
        }

        // Filtr ceny do
        if (filter.getCenaDo() != null) {
            sql.append(" AND o.cena_katalogowa <= ?");
            params.add(filter.getCenaDo());
        }

        // Filtr roku od
        if (filter.getRocznikOd() != null) {
            sql.append(" AND m.rocznik_modelowy >= ?");
            params.add(filter.getRocznikOd());
        }

        // Filtr roku do
        if (filter.getRocznikDo() != null) {
            sql.append(" AND m.rocznik_modelowy <= ?");
            params.add(filter.getRocznikDo());
        }

        // Filtr koloru
        if (filter.getKolor() != null && !filter.getKolor().isEmpty()) {
            sql.append(" AND LOWER(p.kolor) LIKE LOWER(?)");
            params.add("%" + filter.getKolor() + "%");
        }

        // Filtr salonu
        if (filter.getIdSalonu() != null) {
            sql.append(" AND o.id_salonu = ?");
            params.add(filter.getIdSalonu());
        }

        sql.append(" ORDER BY o.id_oferty DESC");

        return jdbc.query(sql.toString(), new VehicleRowMapper(), params.toArray());
    }

    /**
     * Pobierz unikalne marki - do filtrów
     */
    public List<String> findAllMarki() {
        String sql = "SELECT DISTINCT nazwa FROM MARKI ORDER BY nazwa";
        return jdbc.queryForList(sql, String.class);
    }

    /**
     * Pobierz unikalne typy nadwozia - do filtrów
     */
    public List<String> findAllTypyNadwozia() {
        String sql = "SELECT DISTINCT typ_nadwozia FROM MODELE WHERE typ_nadwozia IS NOT NULL ORDER BY typ_nadwozia";
        return jdbc.queryForList(sql, String.class);
    }

    /**
     * Pobierz unikalne typy paliwa - do filtrów
     */
    public List<String> findAllTypyPaliwa() {
        String sql = "SELECT DISTINCT typ_paliwa FROM MODELE WHERE typ_paliwa IS NOT NULL ORDER BY typ_paliwa";
        return jdbc.queryForList(sql, String.class);
    }

    /**
     * RowMapper dla Vehicle - mapuje ResultSet na obiekt Vehicle
     */
    private static class VehicleRowMapper implements RowMapper<Vehicle> {
        @Override
        public Vehicle mapRow(ResultSet rs, int rowNum) throws SQLException {
            Vehicle v = new Vehicle();

            // Z POJAZDY
            v.setIdPojazdu(rs.getInt("id_pojazdu"));
            v.setKolor(rs.getString("kolor"));
            v.setVin(rs.getString("vin"));

            // Z MODELE
            v.setIdModelu(rs.getInt("id_modelu"));
            v.setPojemnoscSilnika(rs.getInt("pojemnosc_silnika"));
            v.setMocSilnika(rs.getInt("moc_silnika"));
            v.setTypPaliwa(rs.getString("typ_paliwa"));
            v.setRocznikModelowy(rs.getInt("rocznik_modelowy"));
            v.setTypNadwozia(rs.getString("typ_nadwozia"));
            v.setMasaWlasna(rs.getInt("masa_wlasna"));

            // Z MARKI
            v.setIdMarki(rs.getInt("id_marki"));
            v.setNazwaMarki(rs.getString("nazwa_marki"));

            // Z OFERTY
            v.setIdOferty(rs.getInt("id_oferty"));
            v.setCenaKatalogowa(rs.getBigDecimal("cena_katalogowa"));
            v.setIdSalonu(rs.getInt("id_salonu"));

            // Z SALONY i ADRESY
            v.setNazwaSalonu(rs.getString("nazwa_salonu"));
            v.setTelefonSalonu(rs.getString("telefon_salonu"));
            v.setMiastoSalonu(rs.getString("miasto_salonu"));
            v.setUlicaSalonu(rs.getString("ulica_salonu"));

            // Status
            v.setSprzedany(rs.getInt("sprzedany") == 1);

            return v;
        }
    }
}