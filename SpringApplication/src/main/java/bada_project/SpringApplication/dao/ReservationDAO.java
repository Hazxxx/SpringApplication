package bada_project.SpringApplication.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

@Repository
public class ReservationDAO {

    private final JdbcTemplate jdbc;

    public ReservationDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Approves reservation and creates a Sale record.
     */
    @Transactional
    public void approve(int idRezerwacji) {
        // 1. Pobierz dane potrzebne do sprzedaży (Klient z rezerwacji, Pracownik i Cena
        // z Oferty)
        String sqlSelect = """
                    SELECT r.id_klienta, o.id_pracownika, o.id_oferty, o.cena_katalogowa
                    FROM REZERWACJE r
                    JOIN OFERTY o ON r.id_oferty = o.id_oferty
                    WHERE r.id_rezerwacji = ?
                """;

        jdbc.query(sqlSelect, rs -> {
            int idKlienta = rs.getInt("id_klienta");
            int idPracownika = rs.getInt("id_pracownika");
            int idOferty = rs.getInt("id_oferty");
            java.math.BigDecimal cena = rs.getBigDecimal("cena_katalogowa");

            // 2. Wstaw rekord sprzedaży (używając nowej sekwencji SPRZEDAZE_SEQ)
            String sqlInsert = """
                        INSERT INTO SPRZEDAZE
                        (ID_SPRZEDAZY, ID_PRACOWNIKA, ID_KLIENTA, ID_OFERTY, KWOTA_SPRZEDAZY, DATA_SPRZEDAZY)
                        VALUES (SPRZEDAZE_SEQ.NEXTVAL, ?, ?, ?, ?, SYSDATE)
                    """;

            jdbc.update(sqlInsert, idPracownika, idKlienta, idOferty, cena);
        }, idRezerwacji);

        // 3. Zaktualizuj status rezerwacji
        updateStatus(idRezerwacji, "ZATWIERDZONA");
    }

    /**
     * Creates a new reservation for an offer and client.
     */
    public void save(int idOferty, int idKlienta) {
        // Status 'OCZEKUJACA' is default in DB, date is SYSDATE default
        String sql = "INSERT INTO REZERWACJE (id_oferty, id_klienta) VALUES (?, ?)";
        jdbc.update(sql, idOferty, idKlienta);
    }

    /**
     * Checks if a reservation already exists for this offer.
     */
    public boolean existsForOffer(int idOferty) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM REZERWACJE WHERE id_oferty = ? AND status != 'ODRZUCONA'",
                Integer.class,
                idOferty);
        return count != null && count > 0;
    }

    /*
     * =========================
     * EMPLOYEE METHODS
     * =========================
     */

    public void updateStatus(int reservationId, String status) {
        jdbc.update("UPDATE REZERWACJE SET status = ? WHERE id_rezerwacji = ?", status, reservationId);

        // If approved, verify if we should mark vehicle as sold?
        // Logic: Approving a reservation doesn't automatically sell it in this simple
        // model,
        // but typically a SALE is a separate step.
        // However, if we want to "sell" it, we would add to SPRZEDAZE.
        // For now, just update status.
    }

    public java.util.List<ReservationDTO> findAllByEmployeeId(int employeeId) {
        String sql = """
                    SELECT
                        r.id_rezerwacji,
                        r.data_rezerwacji,
                        r.status,
                        r.id_oferty,
                        mar.nazwa || ' (Rocznik: ' || m.rocznik_modelowy || ')' as nazwa_pojazdu,
                        k.imie || ' ' || k.nazwisko as dane_klienta,
                        k.email as email_klienta,
                        k.telefon as telefon_klienta
                    FROM REZERWACJE r
                    JOIN OFERTY o ON r.id_oferty = o.id_oferty
                    JOIN POJAZDY p ON o.id_pojazdu = p.id_pojazdu
                    JOIN MODELE m ON p.id_modelu = m.id_modelu
                    JOIN MARKI mar ON m.id_marki = mar.id_marki
                    JOIN KLIENCI k ON r.id_klienta = k.id_klienta
                    WHERE o.id_pracownika = ?
                    ORDER BY r.data_rezerwacji DESC
                """;

        return jdbc.query(sql, (rs, rowNum) -> new ReservationDTO(
                rs.getInt("id_rezerwacji"),
                rs.getDate("data_rezerwacji"),
                rs.getString("status"),
                rs.getInt("id_oferty"),
                rs.getString("nazwa_pojazdu"),
                rs.getString("dane_klienta"),
                rs.getString("email_klienta"),
                rs.getString("telefon_klienta")), employeeId);
    }

    public record ReservationDTO(
            int idRezerwacji,
            java.util.Date dataRezerwacji,
            String status,
            int idOferty,
            String nazwaPojazdu,
            String daneKlienta,
            String emailKlienta,
            String telefonKlienta) {
    }
}
