package bada_project.SpringApplication.dao;

import bada_project.SpringApplication.auth.RegisterForm;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class KlienciDAO {

    private final JdbcTemplate jdbc;

    public KlienciDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /*
     * =========================
     * SPRAWDZENIE EMAILA
     * =========================
     */
    public boolean existsByEmail(String email) {
        Integer cnt = jdbc.queryForObject(
                "SELECT COUNT(*) FROM KLIENCI WHERE LOWER(EMAIL) = LOWER(?)",
                Integer.class,
                email);
        return cnt != null && cnt > 0;
    }

    public List<Klient> findAll() {
        String sql = """
            SELECT id_klienta, imie, nazwisko, email
            FROM KLIENCI
            ORDER BY id_klienta
        """;

        return jdbc.query(sql, (rs, rowNum) ->
                new Klient(
                        rs.getLong("id_klienta"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        rs.getString("email")
                )
        );
    }


    public int countClients() {
        return jdbc.queryForObject(
                "SELECT COUNT(*) FROM KLIENCI",
                Integer.class
        );
    }

    /*
     * =========================
     * REJESTRACJA (ADRES + KLIENT)
     * =========================
     */
    @Transactional // 👈 CRITICAL: Ensures proper transaction management
    public void insertClient(RegisterForm f, String passwordHash) {

        try {
            // 1. Insert into ADRESY - let trigger generate ID
            jdbc.update("""
                        INSERT INTO ADRESY
                        (MIASTO, ULICA, NUMER_BUDYNKU, NUMER_LOKALU, KOD_POCZTOWY, KRAJ)
                        VALUES (?, ?, ?, ?, ?, ?)
                    """,
                    f.getCity(),
                    f.getStreet(),
                    f.getHouseNumber(),
                    f.getApartmentNumber() != null && !f.getApartmentNumber().isEmpty()
                            ? f.getApartmentNumber()
                            : "0",
                    f.getPostalCode(),
                    "Polska");

            System.out.println(">>> ADRESY insert completed");

            // 2. Get the generated adresId from sequence
            Long adresId = jdbc.queryForObject(
                    "SELECT Adresy_id_adresu_SEQ.CURRVAL FROM DUAL",
                    Long.class);

            System.out.println(">>> Retrieved adresId: " + adresId);

            // 3. Find first available salon
            Long salonId = jdbc.queryForObject(
                    "SELECT MIN(ID_SALONU) FROM SALONY_SAMOCHODOWE",
                    Long.class);

            if (salonId == null) {
                throw new IllegalStateException("No salon exists in database. Create at least one salon first.");
            }

            System.out.println(">>> Using salonId: " + salonId);

            // 4. Insert into KLIENCI - let trigger generate ID
            jdbc.update("""
                        INSERT INTO KLIENCI
                        (EMAIL, HASLO, IMIE, NAZWISKO, TELEFON, ID_ADRESU, ID_SALONU)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                    """,
                    f.getEmail().toLowerCase(),
                    passwordHash,
                    f.getFirstName(),
                    f.getLastName(),
                    f.getPhone(),
                    adresId,
                    salonId);

            System.out.println(">>> KLIENCI insert completed");

        } catch (Exception e) {
            System.err.println(">>> ERROR in insertClient: " + e.getMessage());
            throw new RuntimeException("Failed to insert client: " + e.getMessage(), e);
        }
    }

    /*
     * =========================
     * LOGOWANIE (SPRING SECURITY)
     * =========================
     */
    public Optional<ClientAuthRow> findAuthByEmail(String email) {
        return jdbc.query("""
                SELECT EMAIL, HASLO
                FROM KLIENCI
                WHERE LOWER(EMAIL) = LOWER(?)
                """,
                rs -> {
                    if (!rs.next()) {
                        return Optional.empty();
                    }
                    return Optional.of(
                            new ClientAuthRow(
                                    rs.getString("EMAIL"),
                                    rs.getString("HASLO")));
                },
                email);
    }



    /*
     * =========================
     * DTO DO AUTORYZACJI
     * =========================
     */
    public record ClientAuthRow(String email, String passwordHash) {
    }

    /**
     * Finds client ID by email.
     */
    public Integer findIdByEmail(String email) {
        String sql = "SELECT id_klienta FROM KLIENCI WHERE LOWER(email) = LOWER(?)";
        try {
            return jdbc.queryForObject(sql, Integer.class, email);
        } catch (Exception e) {
            return null;
        }
    }
}