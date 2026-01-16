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

    public List<Vehicle> findAllAvailable() {
        String sql = """
                SELECT
                    p.id_pojazdu,
                    p.kolor,
                    p.vin,
                    p.zdjecie_url,
                    p.id_modelu,
                    m.pojemnosc_silnika,
                    m.moc_silnika,
                    m.typ_paliwa,
                    m.rocznik_modelowy,
                    m.typ_nadwozia,
                    m.masa_wlasna,
                    m.id_marki,
                    mar.nazwa AS nazwa_marki,
                    o.id_oferty,
                    o.cena_katalogowa,
                    o.id_salonu,
                    s.nazwa AS nazwa_salonu,
                    s.telefon AS telefon_salonu,
                    a.miasto AS miasto_salonu,
                    a.ulica AS ulica_salonu,
                    CASE WHEN sp.id_sprzedazy IS NULL THEN 0 ELSE 1 END AS sprzedany
                FROM POJAZDY p
                INNER JOIN MODELE m ON p.id_modelu = m.id_modelu
                INNER JOIN MARKI mar ON m.id_marki = mar.id_marki
                INNER JOIN OFERTY o ON p.id_pojazdu = o.id_pojazdu
                INNER JOIN SALONY_SAMOCHODOWE s ON o.id_salonu = s.id_salonu
                INNER JOIN ADRESY a ON s.id_adresu = a.id_adresu
                LEFT JOIN SPRZEDAZE sp ON o.id_oferty = sp.id_oferty
                WHERE sp.id_sprzedazy IS NULL
                ORDER BY o.id_oferty DESC
                """;

        return jdbc.query(sql, new VehicleRowMapper());
    }

    /**
     * Pobierz wszystkie unikalne marki (do filtrów)
     */
    public List<String> findAllMarki() {
        String sql = """
        SELECT DISTINCT mar.nazwa
        FROM MARKI mar
        INNER JOIN MODELE m ON mar.id_marki = m.id_marki
        INNER JOIN POJAZDY p ON p.id_modelu = m.id_modelu
        ORDER BY mar.nazwa
        """;

        return jdbc.queryForList(sql, String.class);
    }

    /**
     * Pobierz wszystkie unikalne typy nadwozia (do filtrów)
     */
    public List<String> findAllTypyNadwozia() {
        String sql = """
        SELECT DISTINCT m.typ_nadwozia
        FROM MODELE m
        INNER JOIN POJAZDY p ON p.id_modelu = m.id_modelu
        WHERE m.typ_nadwozia IS NOT NULL
        ORDER BY m.typ_nadwozia
        """;

        return jdbc.queryForList(sql, String.class);
    }

    /**
     * Pobierz ID pracownika przypisanego do oferty
     */
    public Integer getEmployeeIdForOffer(Integer idOferty) {
        String sql = """
        SELECT id_pracownika
        FROM OFERTY
        WHERE id_oferty = ?
        """;

        return jdbc.query(sql, rs -> {
            if (rs.next()) {
                int id = rs.getInt("id_pracownika");
                return rs.wasNull() ? null : id;
            }
            return null;
        }, idOferty);
    }

    /**
     * Przypisz pracownika do oferty
     */
    public void assignEmployeeToOffer(Integer idOferty, Integer idPracownika) {
        String sql = """
        UPDATE OFERTY
        SET id_pracownika = ?
        WHERE id_oferty = ?
        """;

        jdbc.update(sql, idPracownika, idOferty);
    }


    /**
     * Pobierz wszystkie unikalne typy paliwa (do filtrów)
     */
    public List<String> findAllTypyPaliwa() {
        String sql = """
        SELECT DISTINCT m.typ_paliwa
        FROM MODELE m
        INNER JOIN POJAZDY p ON p.id_modelu = m.id_modelu
        WHERE m.typ_paliwa IS NOT NULL
        ORDER BY m.typ_paliwa
        """;

        return jdbc.queryForList(sql, String.class);
    }


    public Vehicle findById(Integer idPojazdu) {
        String sql = """
                SELECT
                    p.id_pojazdu,
                    p.kolor,
                    p.vin,
                    p.zdjecie_url,
                    p.id_modelu,
                    m.pojemnosc_silnika,
                    m.moc_silnika,
                    m.typ_paliwa,
                    m.rocznik_modelowy,
                    m.typ_nadwozia,
                    m.masa_wlasna,
                    m.id_marki,
                    mar.nazwa AS nazwa_marki,
                    o.id_oferty,
                    o.cena_katalogowa,
                    o.id_salonu,
                    s.nazwa AS nazwa_salonu,
                    s.telefon AS telefon_salonu,
                    a.miasto AS miasto_salonu,
                    a.ulica AS ulica_salonu,
                    CASE WHEN sp.id_sprzedazy IS NULL THEN 0 ELSE 1 END AS sprzedany,
                    prac.imie AS imie_sprzedawcy,
                    prac.nazwisko AS nazwisko_sprzedawcy,
                    prac.email AS email_sprzedawcy,
                    prac.telefon AS telefon_sprzedawcy
                FROM POJAZDY p
                INNER JOIN MODELE m ON p.id_modelu = m.id_modelu
                INNER JOIN MARKI mar ON m.id_marki = mar.id_marki
                LEFT JOIN OFERTY o ON p.id_pojazdu = o.id_pojazdu
                LEFT JOIN SALONY_SAMOCHODOWE s ON o.id_salonu = s.id_salonu
                LEFT JOIN ADRESY a ON s.id_adresu = a.id_adresu
                LEFT JOIN SPRZEDAZE sp ON o.id_oferty = sp.id_oferty
                LEFT JOIN PRACOWNICY prac ON o.id_pracownika = prac.id_pracownika
                WHERE p.id_pojazdu = ?
                """;

        List<Vehicle> results = jdbc.query(sql, new VehicleRowMapper(), idPojazdu);
        return results.isEmpty() ? null : results.get(0);
    }

    public List<Vehicle> findAssignedToEmployee(Integer employeeId) {
        String sql = """
                SELECT
                    p.id_pojazdu,
                    p.kolor,
                    p.vin,
                    p.zdjecie_url,
                    p.id_modelu,
                    m.pojemnosc_silnika,
                    m.moc_silnika,
                    m.typ_paliwa,
                    m.rocznik_modelowy,
                    m.typ_nadwozia,
                    m.masa_wlasna,
                    m.id_marki,
                    mar.nazwa AS nazwa_marki,
                    o.id_oferty,
                    o.cena_katalogowa,
                    o.id_salonu,
                    s.nazwa AS nazwa_salonu,
                    s.telefon AS telefon_salonu,
                    a.miasto AS miasto_salonu,
                    a.ulica AS ulica_salonu,
                    CASE WHEN sp.id_sprzedazy IS NULL THEN 0 ELSE 1 END AS sprzedany,
                    prac.imie AS imie_sprzedawcy,
                    prac.nazwisko AS nazwisko_sprzedawcy,
                    prac.email AS email_sprzedawcy,
                    prac.telefon AS telefon_sprzedawcy
                FROM POJAZDY p
                INNER JOIN MODELE m ON p.id_modelu = m.id_modelu
                INNER JOIN MARKI mar ON m.id_marki = mar.id_marki
                INNER JOIN OFERTY o ON p.id_pojazdu = o.id_pojazdu
                INNER JOIN SALONY_SAMOCHODOWE s ON o.id_salonu = s.id_salonu
                INNER JOIN ADRESY a ON s.id_adresu = a.id_adresu
                LEFT JOIN SPRZEDAZE sp ON o.id_oferty = sp.id_oferty
                LEFT JOIN PRACOWNICY prac ON o.id_pracownika = prac.id_pracownika
                WHERE o.id_pracownika = ?
                ORDER BY o.id_oferty DESC
                """;

        return jdbc.query(sql, new VehicleRowMapper(), employeeId);
    }

    public List<Vehicle> search(VehicleSearchFilter filter) {
        StringBuilder sql = new StringBuilder("""
                SELECT
                    p.id_pojazdu,
                    p.kolor,
                    p.vin,
                    p.zdjecie_url,
                    p.id_modelu,
                    m.pojemnosc_silnika,
                    m.moc_silnika,
                    m.typ_paliwa,
                    m.rocznik_modelowy,
                    m.typ_nadwozia,
                    m.masa_wlasna,
                    m.id_marki,
                    mar.nazwa AS nazwa_marki,
                    o.id_oferty,
                    o.cena_katalogowa,
                    o.id_salonu,
                    s.nazwa AS nazwa_salonu,
                    s.telefon AS telefon_salonu,
                    a.miasto AS miasto_salonu,
                    a.ulica AS ulica_salonu,
                    CASE WHEN sp.id_sprzedazy IS NULL THEN 0 ELSE 1 END AS sprzedany
                FROM POJAZDY p
                INNER JOIN MODELE m ON p.id_modelu = m.id_modelu
                INNER JOIN MARKI mar ON m.id_marki = mar.id_marki
                INNER JOIN OFERTY o ON p.id_pojazdu = o.id_pojazdu
                INNER JOIN SALONY_SAMOCHODOWE s ON o.id_salonu = s.id_salonu
                INNER JOIN ADRESY a ON s.id_adresu = a.id_adresu
                LEFT JOIN SPRZEDAZE sp ON o.id_oferty = sp.id_oferty
                WHERE sp.id_sprzedazy IS NULL
                """);

        List<Object> params = new ArrayList<>();

        if (filter.getNazwaMarki() != null && !filter.getNazwaMarki().isEmpty()) {
            sql.append(" AND LOWER(mar.nazwa) LIKE LOWER(?)");
            params.add("%" + filter.getNazwaMarki() + "%");
        }

        if (filter.getTypNadwozia() != null && !filter.getTypNadwozia().isEmpty()) {
            sql.append(" AND m.typ_nadwozia = ?");
            params.add(filter.getTypNadwozia());
        }

        if (filter.getTypPaliwa() != null && !filter.getTypPaliwa().isEmpty()) {
            sql.append(" AND m.typ_paliwa = ?");
            params.add(filter.getTypPaliwa());
        }

        if (filter.getCenaOd() != null) {
            sql.append(" AND o.cena_katalogowa >= ?");
            params.add(filter.getCenaOd());
        }

        if (filter.getCenaDo() != null) {
            sql.append(" AND o.cena_katalogowa <= ?");
            params.add(filter.getCenaDo());
        }

        if (filter.getRocznikOd() != null) {
            sql.append(" AND m.rocznik_modelowy >= ?");
            params.add(filter.getRocznikOd());
        }

        if (filter.getRocznikDo() != null) {
            sql.append(" AND m.rocznik_modelowy <= ?");
            params.add(filter.getRocznikDo());
        }

        if (filter.getKolor() != null && !filter.getKolor().isEmpty()) {
            sql.append(" AND LOWER(p.kolor) LIKE LOWER(?)");
            params.add("%" + filter.getKolor() + "%");
        }

        if (filter.getIdSalonu() != null) {
            sql.append(" AND o.id_salonu = ?");
            params.add(filter.getIdSalonu());
        }

        sql.append(" ORDER BY o.id_oferty DESC");

        return jdbc.query(sql.toString(), new VehicleRowMapper(), params.toArray());
    }

    private static class VehicleRowMapper implements RowMapper<Vehicle> {
        @Override
        public Vehicle mapRow(ResultSet rs, int rowNum) throws SQLException {
            Vehicle v = new Vehicle();

            v.setIdPojazdu(rs.getInt("id_pojazdu"));
            v.setKolor(rs.getString("kolor"));
            v.setVin(rs.getString("vin"));
            v.setZdjecieUrl(rs.getString("zdjecie_url"));

            v.setIdModelu(rs.getInt("id_modelu"));
            v.setPojemnoscSilnika(rs.getInt("pojemnosc_silnika"));
            v.setMocSilnika(rs.getInt("moc_silnika"));
            v.setTypPaliwa(rs.getString("typ_paliwa"));
            v.setRocznikModelowy(rs.getInt("rocznik_modelowy"));
            v.setTypNadwozia(rs.getString("typ_nadwozia"));
            v.setMasaWlasna(rs.getInt("masa_wlasna"));

            v.setIdMarki(rs.getInt("id_marki"));
            v.setNazwaMarki(rs.getString("nazwa_marki"));

            v.setIdOferty(rs.getInt("id_oferty"));
            v.setCenaKatalogowa(rs.getBigDecimal("cena_katalogowa"));
            v.setIdSalonu(rs.getInt("id_salonu"));

            v.setNazwaSalonu(rs.getString("nazwa_salonu"));
            v.setTelefonSalonu(rs.getString("telefon_salonu"));
            v.setMiastoSalonu(rs.getString("miasto_salonu"));
            v.setUlicaSalonu(rs.getString("ulica_salonu"));

            v.setSprzedany(rs.getInt("sprzedany") == 1);

            try {
                v.setImieSprzedawcy(rs.getString("imie_sprzedawcy"));
                v.setNazwiskoSprzedawcy(rs.getString("nazwisko_sprzedawcy"));
                v.setEmailSprzedawcy(rs.getString("email_sprzedawcy"));
                v.setTelefonSprzedawcy(rs.getString("telefon_sprzedawcy"));
            } catch (SQLException ignored) {}

            return v;
        }
    }
}
