package bada_project.SpringApplication.admin.dbmanagement.dao;

import bada_project.SpringApplication.admin.dbmanagement.models.FirmaPartnerska;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class FirmyPartnerskieDAO {

    private final JdbcTemplate jdbc;

    public FirmyPartnerskieDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<FirmaPartnerska> findAll() {
        String sql = """
            SELECT 
                f.id_firmy_partnerskiej,
                f.nazwa,
                f.email,
                f.telefon,
                f.id_adresu,
                a.miasto,
                a.ulica,
                COUNT(s.id_salonu) as liczba_salonow
            FROM FIRMY_PARTNERSKIE f
            LEFT JOIN ADRESY a ON f.id_adresu = a.id_adresu
            LEFT JOIN SALONY_SAMOCHODOWE s ON f.id_firmy_partnerskiej = s.id_firmy_partnerskiej
            GROUP BY f.id_firmy_partnerskiej, f.nazwa, f.email, f.telefon, f.id_adresu, a.miasto, a.ulica
            ORDER BY f.nazwa
            """;
        return jdbc.query(sql, firmaRowMapper());
    }

    public Optional<FirmaPartnerska> findById(Long id) {
        String sql = """
            SELECT 
                f.id_firmy_partnerskiej,
                f.nazwa,
                f.email,
                f.telefon,
                f.id_adresu,
                a.miasto,
                a.ulica,
                a.numer_budynku,
                a.numer_lokalu,
                a.kod_pocztowy,
                a.kraj,
                COUNT(s.id_salonu) as liczba_salonow
            FROM FIRMY_PARTNERSKIE f
            LEFT JOIN ADRESY a ON f.id_adresu = a.id_adresu
            LEFT JOIN SALONY_SAMOCHODOWE s ON f.id_firmy_partnerskiej = s.id_firmy_partnerskiej
            WHERE f.id_firmy_partnerskiej = ?
            GROUP BY f.id_firmy_partnerskiej, f.nazwa, f.email, f.telefon, f.id_adresu, 
                     a.miasto, a.ulica, a.numer_budynku, a.numer_lokalu, a.kod_pocztowy, a.kraj
            """;
        List<FirmaPartnerska> results = jdbc.query(sql, firmaDetailRowMapper(), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Transactional
    public void insert(FirmaPartnerska firma) {
        // 1. Insert Address
        String sqlAdres = """
            INSERT INTO ADRESY (miasto, ulica, numer_budynku, numer_lokalu, kod_pocztowy, kraj)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        jdbc.update(sqlAdres,
                firma.getNowyMiasto(),
                firma.getNowaUlica(),
                firma.getNowyNumerBudynku(),
                firma.getNowyNumerLokalu() != null ? firma.getNowyNumerLokalu() : 0,
                firma.getNowyKodPocztowy(),
                firma.getNowyKraj() != null ? firma.getNowyKraj() : "Polska");

        // 2. Get the generated adresId
        Long adresId = jdbc.queryForObject(
                "SELECT Adresy_id_adresu_SEQ.CURRVAL FROM DUAL",
                Long.class);

        // 3. Insert Firma
        String sqlFirma = """
            INSERT INTO FIRMY_PARTNERSKIE (nazwa, email, telefon, id_adresu)
            VALUES (?, ?, ?, ?)
            """;
        jdbc.update(sqlFirma,
                firma.getNazwa(),
                firma.getEmail(),
                firma.getTelefon(),
                adresId);
    }

    @Transactional
    public void update(FirmaPartnerska firma) {
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
                firma.getNowyMiasto(),
                firma.getNowaUlica(),
                firma.getNowyNumerBudynku(),
                firma.getNowyNumerLokalu() != null ? firma.getNowyNumerLokalu() : 0,
                firma.getNowyKodPocztowy(),
                firma.getNowyKraj() != null ? firma.getNowyKraj() : "Polska",
                firma.getIdAdresu());

        // Update firma
        String sqlFirma = """
            UPDATE FIRMY_PARTNERSKIE SET 
                nazwa = ?,
                email = ?,
                telefon = ?
            WHERE id_firmy_partnerskiej = ?
            """;
        jdbc.update(sqlFirma,
                firma.getNazwa(),
                firma.getEmail(),
                firma.getTelefon(),
                firma.getIdFirmyPartnerskiej());
    }

    @Transactional
    public void delete(Long id) {
        // Get adresId first
        Long adresId = jdbc.queryForObject(
                "SELECT id_adresu FROM FIRMY_PARTNERSKIE WHERE id_firmy_partnerskiej = ?",
                Long.class, id);

        // Delete firma
        jdbc.update("DELETE FROM FIRMY_PARTNERSKIE WHERE id_firmy_partnerskiej = ?", id);

        // Delete address
        if (adresId != null) {
            jdbc.update("DELETE FROM ADRESY WHERE id_adresu = ?", adresId);
        }
    }

    public int countTotal() {
        String sql = "SELECT COUNT(*) FROM FIRMY_PARTNERSKIE";
        Integer count = jdbc.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    private RowMapper<FirmaPartnerska> firmaRowMapper() {
        return (rs, rowNum) -> {
            FirmaPartnerska f = new FirmaPartnerska();
            f.setIdFirmyPartnerskiej(rs.getLong("id_firmy_partnerskiej"));
            f.setNazwa(rs.getString("nazwa"));
            f.setEmail(rs.getString("email"));
            f.setTelefon(rs.getString("telefon"));
            f.setIdAdresu(rs.getLong("id_adresu"));
            f.setMiasto(rs.getString("miasto"));
            f.setUlica(rs.getString("ulica"));
            f.setLiczbaSalonow(rs.getInt("liczba_salonow"));
            return f;
        };
    }

    private RowMapper<FirmaPartnerska> firmaDetailRowMapper() {
        return (rs, rowNum) -> {
            FirmaPartnerska f = new FirmaPartnerska();
            f.setIdFirmyPartnerskiej(rs.getLong("id_firmy_partnerskiej"));
            f.setNazwa(rs.getString("nazwa"));
            f.setEmail(rs.getString("email"));
            f.setTelefon(rs.getString("telefon"));
            f.setIdAdresu(rs.getLong("id_adresu"));
            f.setMiasto(rs.getString("miasto"));
            f.setUlica(rs.getString("ulica"));
            f.setLiczbaSalonow(rs.getInt("liczba_salonow"));

            // Set form fields
            f.setNowyMiasto(rs.getString("miasto"));
            f.setNowaUlica(rs.getString("ulica"));
            f.setNowyNumerBudynku(rs.getInt("numer_budynku"));
            f.setNowyNumerLokalu(rs.getInt("numer_lokalu"));
            f.setNowyKodPocztowy(rs.getString("kod_pocztowy"));
            f.setNowyKraj(rs.getString("kraj"));
            return f;
        };
    }
}