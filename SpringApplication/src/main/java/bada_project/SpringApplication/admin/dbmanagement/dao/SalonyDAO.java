package bada_project.SpringApplication.admin.dbmanagement.dao;

import bada_project.SpringApplication.admin.dbmanagement.models.Salon;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class SalonyDAO {

    private final JdbcTemplate jdbc;

    public SalonyDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Salon> findAll() {
        String sql = """
    SELECT 
        s.id_salonu,
        s.nazwa,
        s.telefon,
        s.id_adresu,
        s.id_firmy_partnerskiej,
        f.nazwa AS nazwa_firmy,
        a.miasto,
        a.ulica,
        COUNT(p.id_pracownika) AS liczba_pracownikow
    FROM SALONY_SAMOCHODOWE s
    LEFT JOIN FIRMY_PARTNERSKIE f ON s.id_firmy_partnerskiej = f.id_firmy_partnerskiej
    LEFT JOIN ADRESY a ON s.id_adresu = a.id_adresu
    LEFT JOIN PRACOWNICY p ON s.id_salonu = p.id_salonu
    GROUP BY 
        s.id_salonu,
        s.nazwa,
        s.telefon,
        s.id_adresu,
        s.id_firmy_partnerskiej,
        f.nazwa,
        a.miasto,
        a.ulica
    ORDER BY s.nazwa
    """;

        return jdbc.query(sql, salonRowMapper());
    }

    public Optional<Salon> findById(Long id) {
        String sql = """
    SELECT 
        s.id_salonu,
        s.nazwa,
        s.telefon,
        s.id_adresu,
        s.id_firmy_partnerskiej,
        f.nazwa AS nazwa_firmy,
        a.miasto,
        a.ulica,
        a.numer_budynku,
        a.numer_lokalu,
        a.kod_pocztowy,
        a.kraj,
        COUNT(p.id_pracownika) AS liczba_pracownikow
    FROM SALONY_SAMOCHODOWE s
    LEFT JOIN FIRMY_PARTNERSKIE f ON s.id_firmy_partnerskiej = f.id_firmy_partnerskiej
    LEFT JOIN ADRESY a ON s.id_adresu = a.id_adresu
    LEFT JOIN PRACOWNICY p ON s.id_salonu = p.id_salonu
    WHERE s.id_salonu = ?
    GROUP BY 
        s.id_salonu,
        s.nazwa,
        s.telefon,
        s.id_adresu,
        s.id_firmy_partnerskiej,
        f.nazwa,
        a.miasto,
        a.ulica,
        a.numer_budynku,
        a.numer_lokalu,
        a.kod_pocztowy,
        a.kraj
    """;

        List<Salon> results = jdbc.query(sql, salonDetailRowMapper(), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Transactional
    public void insert(Salon salon) {
        // 1. Insert Address
        String sqlAdres = """
            INSERT INTO ADRESY (miasto, ulica, numer_budynku, numer_lokalu, kod_pocztowy, kraj)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        jdbc.update(sqlAdres,
                salon.getNowyMiasto(),
                salon.getNowaUlica(),
                salon.getNowyNumerBudynku(),
                salon.getNowyNumerLokalu() != null ? salon.getNowyNumerLokalu() : 0,
                salon.getNowyKodPocztowy(),
                salon.getNowyKraj() != null ? salon.getNowyKraj() : "Polska");

        // 2. Get the generated adresId
        Long adresId = jdbc.queryForObject(
                "SELECT Adresy_id_adresu_SEQ.CURRVAL FROM DUAL",
                Long.class);

        // 3. Insert Salon
        String sqlSalon = """
    INSERT INTO SALONY_SAMOCHODOWE 
        (nazwa, telefon, id_adresu, id_firmy_partnerskiej)
    VALUES (?, ?, ?, ?)
    """;

        jdbc.update(sqlSalon,
                salon.getNazwa(),
                salon.getTelefon(),
                adresId,
                salon.getIdFirmyPartnerskiej());

    }

    @Transactional
    public void update(Salon salon) {
        // Update address first
        String sqlAdres = """
            UPDATE ADRESY SET 
                miasto = ?,
                ulica = ?,
                numer_budynku = ?,
                numer_lokalu = ?,
                kod_pocztowy = ?,
                kraj = ?
            WHERE id_adresu = ?
            """;
        jdbc.update(sqlAdres,
                salon.getNowyMiasto(),
                salon.getNowaUlica(),
                salon.getNowyNumerBudynku(),
                salon.getNowyNumerLokalu() != null ? salon.getNowyNumerLokalu() : 0,
                salon.getNowyKodPocztowy(),
                salon.getNowyKraj() != null ? salon.getNowyKraj() : "Polska",
                salon.getIdAdresu());

        // Update salon
        String sqlSalon = """
    UPDATE SALONY_SAMOCHODOWE SET 
        nazwa = ?,
        telefon = ?,
        id_firmy_partnerskiej = ?
    WHERE id_salonu = ?
    """;

        jdbc.update(sqlSalon,
                salon.getNazwa(),
                salon.getTelefon(),
                salon.getIdFirmyPartnerskiej(),
                salon.getIdSalonu());

    }

    @Transactional
    public void delete(Long id) {
        // Get adresId first
        Long adresId = jdbc.queryForObject(
                "SELECT id_adresu FROM SALONY_SAMOCHODOWE WHERE id_salonu = ?",
                Long.class, id);

        // Delete salon
        jdbc.update("DELETE FROM SALONY_SAMOCHODOWE WHERE id_salonu = ?", id);

        // Delete address
        if (adresId != null) {
            jdbc.update("DELETE FROM ADRESY WHERE id_adresu = ?", adresId);
        }
    }

    public int countTotal() {
        String sql = "SELECT COUNT(*) FROM SALONY_SAMOCHODOWE";
        Integer count = jdbc.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    private RowMapper<Salon> salonRowMapper() {
        return (rs, rowNum) -> {
            Salon s = new Salon();
            s.setIdSalonu(rs.getLong("id_salonu"));
            s.setNazwa(rs.getString("nazwa"));
            s.setTelefon(rs.getString("telefon"));
            s.setIdAdresu(rs.getLong("id_adresu"));
            s.setIdFirmyPartnerskiej(rs.getLong("id_firmy_partnerskiej"));
            s.setNazwaFirmy(rs.getString("nazwa_firmy"));
            s.setMiasto(rs.getString("miasto"));
            s.setUlica(rs.getString("ulica"));
            s.setLiczbaPracownikow(rs.getInt("liczba_pracownikow"));
            return s;
        };
    }

    private RowMapper<Salon> salonDetailRowMapper() {
        return (rs, rowNum) -> {
            Salon s = new Salon();
            s.setIdSalonu(rs.getLong("id_salonu"));
            s.setNazwa(rs.getString("nazwa"));
            s.setTelefon(rs.getString("telefon"));
            s.setIdAdresu(rs.getLong("id_adresu"));
            s.setIdFirmyPartnerskiej(rs.getLong("id_firmy_partnerskiej"));
            s.setNazwaFirmy(rs.getString("nazwa_firmy"));
            s.setMiasto(rs.getString("miasto"));
            s.setUlica(rs.getString("ulica"));
            s.setLiczbaPracownikow(rs.getInt("liczba_pracownikow"));

            // Set form fields
            s.setNowyMiasto(rs.getString("miasto"));
            s.setNowaUlica(rs.getString("ulica"));
            s.setNowyNumerBudynku(rs.getInt("numer_budynku"));
            s.setNowyNumerLokalu(rs.getInt("numer_lokalu"));
            s.setNowyKodPocztowy(rs.getString("kod_pocztowy"));
            s.setNowyKraj(rs.getString("kraj"));
            return s;
        };
    }
}