package bada_project.SpringApplication.admin.dbmanagement.dao;

import bada_project.SpringApplication.admin.dbmanagement.models.Sprzedawcy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class SprzedawcyDAO {

    private final JdbcTemplate jdbc;

    public SprzedawcyDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Sprzedawcy> findAll() {
        String sql = """
            SELECT 
                p.id_pracownika,
                p.imie,
                p.nazwisko,
                p.telefon,
                p.email,
                p.plec,
                p.PESEL,
                p.data_urodzenia,
                p.id_adresu,
                p.id_salonu,
                p.id_stanowiska,
                p.id_wynagrodzenia,
                a.miasto,
                a.ulica,
                s.nazwa as nazwa_salonu,
                st.nazwa as nazwa_stanowiska,
                st.czy_kierownicze,
                COUNT(sp.id_sprzedazy) as liczba_sprzedazy
            FROM PRACOWNICY p
            LEFT JOIN ADRESY a ON p.id_adresu = a.id_adresu
            LEFT JOIN SALONY_SAMOCHODOWE s ON p.id_salonu = s.id_salonu
            LEFT JOIN STANOWISKA st ON p.id_stanowiska = st.id_stanowiska
            LEFT JOIN SPRZEDAZE sp ON p.id_pracownika = sp.id_pracownika
            GROUP BY p.id_pracownika, p.imie, p.nazwisko, p.telefon, p.email, p.plec, 
                     p.PESEL, p.data_urodzenia, p.id_adresu, p.id_salonu, p.id_stanowiska, 
                     p.id_wynagrodzenia, a.miasto, a.ulica, s.nazwa, st.nazwa, st.czy_kierownicze
            ORDER BY p.nazwisko, p.imie
            """;
        return jdbc.query(sql, sprzedawcaRowMapper());
    }

    public Optional<Sprzedawcy> findById(Long id) {
        String sql = """
            SELECT 
                p.id_pracownika,
                p.imie,
                p.nazwisko,
                p.telefon,
                p.email,
                p.plec,
                p.PESEL,
                p.data_urodzenia,
                p.id_adresu,
                p.id_salonu,
                p.id_stanowiska,
                p.id_wynagrodzenia,
                a.miasto,
                a.ulica,
                a.numer_budynku,
                a.numer_lokalu,
                a.kod_pocztowy,
                a.kraj,
                s.nazwa as nazwa_salonu,
                st.nazwa as nazwa_stanowiska,
                st.czy_kierownicze,
                COUNT(sp.id_sprzedazy) as liczba_sprzedazy
            FROM PRACOWNICY p
            LEFT JOIN ADRESY a ON p.id_adresu = a.id_adresu
            LEFT JOIN SALONY_SAMOCHODOWE s ON p.id_salonu = s.id_salonu
            LEFT JOIN STANOWISKA st ON p.id_stanowiska = st.id_stanowiska
            LEFT JOIN SPRZEDAZE sp ON p.id_pracownika = sp.id_pracownika
            WHERE p.id_pracownika = ?
            GROUP BY p.id_pracownika, p.imie, p.nazwisko, p.telefon, p.email, p.plec,
                     p.PESEL, p.data_urodzenia, p.id_adresu, p.id_salonu, p.id_stanowiska,
                     p.id_wynagrodzenia, a.miasto, a.ulica, a.numer_budynku, a.numer_lokalu,
                     a.kod_pocztowy, a.kraj, s.nazwa, st.nazwa, st.czy_kierownicze
            """;
        List<Sprzedawcy> results = jdbc.query(sql, sprzedawcaDetailRowMapper(), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Transactional
    public void insert(Sprzedawcy sprzedawca) {
        // 1. Insert Address
        String sqlAdres = """
            INSERT INTO ADRESY (miasto, ulica, numer_budynku, numer_lokalu, kod_pocztowy, kraj)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        jdbc.update(sqlAdres,
                sprzedawca.getNowyMiasto(),
                sprzedawca.getNowaUlica(),
                sprzedawca.getNowyNumerBudynku(),
                sprzedawca.getNowyNumerLokalu() != null ? sprzedawca.getNowyNumerLokalu() : 0,
                sprzedawca.getNowyKodPocztowy(),
                sprzedawca.getNowyKraj() != null ? sprzedawca.getNowyKraj() : "Polska");

        // 2. Get the generated adresId
        Long adresId = jdbc.queryForObject(
                "SELECT Adresy_id_adresu_SEQ.CURRVAL FROM DUAL",
                Long.class);

        // 3. Insert Pracownik
        String sqlPracownik = """
            INSERT INTO PRACOWNICY (imie, nazwisko, telefon, email, plec, PESEL, 
                                   data_urodzenia, id_adresu, id_salonu, id_stanowiska, id_wynagrodzenia)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        jdbc.update(sqlPracownik,
                sprzedawca.getImie(),
                sprzedawca.getNazwisko(),
                sprzedawca.getTelefon(),
                sprzedawca.getEmail(),
                sprzedawca.getPlec() != null ? String.valueOf(sprzedawca.getPlec()) : null,
                sprzedawca.getPesel(),
                sprzedawca.getDataUrodzenia(),
                adresId,
                sprzedawca.getIdSalonu(),
                sprzedawca.getIdStanowiska(),
                sprzedawca.getIdWynagrodzenia());
    }

    @Transactional
    public void update(Sprzedawcy sprzedawca) {
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
                sprzedawca.getNowyMiasto(),
                sprzedawca.getNowaUlica(),
                sprzedawca.getNowyNumerBudynku(),
                sprzedawca.getNowyNumerLokalu() != null ? sprzedawca.getNowyNumerLokalu() : 0,
                sprzedawca.getNowyKodPocztowy(),
                sprzedawca.getNowyKraj() != null ? sprzedawca.getNowyKraj() : "Polska",
                sprzedawca.getIdAdresu());

        // Update pracownik
        String sqlPracownik = """
            UPDATE PRACOWNICY SET 
                imie = ?,
                nazwisko = ?,
                telefon = ?,
                email = ?,
                plec = ?,
                PESEL = ?,
                data_urodzenia = ?,
                id_salonu = ?,
                id_stanowiska = ?,
                id_wynagrodzenia = ?
            WHERE id_pracownika = ?
            """;
        jdbc.update(sqlPracownik,
                sprzedawca.getImie(),
                sprzedawca.getNazwisko(),
                sprzedawca.getTelefon(),
                sprzedawca.getEmail(),
                sprzedawca.getPlec() != null ? String.valueOf(sprzedawca.getPlec()) : null,
                sprzedawca.getPesel(),
                sprzedawca.getDataUrodzenia(),
                sprzedawca.getIdSalonu(),
                sprzedawca.getIdStanowiska(),
                sprzedawca.getIdWynagrodzenia(),
                sprzedawca.getIdPracownika());
    }

    @Transactional
    public void delete(Long id) {
        // Get adresId first
        Long adresId = jdbc.queryForObject(
                "SELECT id_adresu FROM PRACOWNICY WHERE id_pracownika = ?",
                Long.class, id);

        // Delete pracownik
        jdbc.update("DELETE FROM PRACOWNICY WHERE id_pracownika = ?", id);

        // Delete address
        if (adresId != null) {
            jdbc.update("DELETE FROM ADRESY WHERE id_adresu = ?", adresId);
        }
    }

    public int countTotal() {
        String sql = "SELECT COUNT(*) FROM PRACOWNICY";
        Integer count = jdbc.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    private RowMapper<Sprzedawcy> sprzedawcaRowMapper() {
        return (rs, rowNum) -> {
            Sprzedawcy s = new Sprzedawcy();
            s.setIdPracownika(rs.getLong("id_pracownika"));
            s.setImie(rs.getString("imie"));
            s.setNazwisko(rs.getString("nazwisko"));
            s.setTelefon(rs.getString("telefon"));
            s.setEmail(rs.getString("email"));
            String plecStr = rs.getString("plec");
            s.setPlec(plecStr != null && !plecStr.isEmpty() ? plecStr.charAt(0) : null);
            s.setPesel(rs.getString("PESEL"));
            s.setDataUrodzenia(rs.getDate("data_urodzenia"));
            s.setIdAdresu(rs.getLong("id_adresu"));
            s.setIdSalonu(rs.getLong("id_salonu"));
            s.setIdStanowiska(rs.getLong("id_stanowiska"));
            s.setIdWynagrodzenia(rs.getLong("id_wynagrodzenia"));
            s.setMiasto(rs.getString("miasto"));
            s.setUlica(rs.getString("ulica"));
            s.setNazwaSalonu(rs.getString("nazwa_salonu"));
            s.setNazwaStanowiska(rs.getString("nazwa_stanowiska"));
            s.setCzyKierownicze(rs.getInt("czy_kierownicze") == 1);
            s.setLiczbaSprzedazy(rs.getInt("liczba_sprzedazy"));
            return s;
        };
    }

    private RowMapper<Sprzedawcy> sprzedawcaDetailRowMapper() {
        return (rs, rowNum) -> {
            Sprzedawcy s = new Sprzedawcy();
            s.setIdPracownika(rs.getLong("id_pracownika"));
            s.setImie(rs.getString("imie"));
            s.setNazwisko(rs.getString("nazwisko"));
            s.setTelefon(rs.getString("telefon"));
            s.setEmail(rs.getString("email"));
            String plecStr = rs.getString("plec");
            s.setPlec(plecStr != null && !plecStr.isEmpty() ? plecStr.charAt(0) : null);
            s.setPesel(rs.getString("PESEL"));
            s.setDataUrodzenia(rs.getDate("data_urodzenia"));
            s.setIdAdresu(rs.getLong("id_adresu"));
            s.setIdSalonu(rs.getLong("id_salonu"));
            s.setIdStanowiska(rs.getLong("id_stanowiska"));
            s.setIdWynagrodzenia(rs.getLong("id_wynagrodzenia"));
            s.setMiasto(rs.getString("miasto"));
            s.setUlica(rs.getString("ulica"));
            s.setNazwaSalonu(rs.getString("nazwa_salonu"));
            s.setNazwaStanowiska(rs.getString("nazwa_stanowiska"));
            s.setCzyKierownicze(rs.getInt("czy_kierownicze") == 1);
            s.setLiczbaSprzedazy(rs.getInt("liczba_sprzedazy"));

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