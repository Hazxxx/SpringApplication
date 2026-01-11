package bada_project.SpringApplication.user.profile;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserProfileDAO {

    private final JdbcTemplate jdbc;

    public UserProfileDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public UserProfile findByEmail(String email) {
        // Łączymy KLIENCI + ADRESY
        String sql = """
            SELECT
                k.id_klienta,
                k.imie,
                k.nazwisko,
                k.email,
                k.telefon,
                k.id_adresu,
                k.id_salonu,
                a.miasto,
                a.ulica,
                a.numer_budynku,
                a.numer_lokalu,
                a.kod_pocztowy,
                a.kraj
            FROM KLIENCI k
            JOIN ADRESY a ON a.id_adresu = k.id_adresu
            WHERE k.email = ?
            """;

        try {
            return jdbc.queryForObject(sql, (rs, rowNum) -> {
                UserProfile p = new UserProfile();
                p.setIdKlienta(rs.getInt("id_klienta"));
                p.setImie(rs.getString("imie"));
                p.setNazwisko(rs.getString("nazwisko"));
                p.setEmail(rs.getString("email"));
                p.setTelefon(rs.getString("telefon"));
                p.setIdAdresu(rs.getInt("id_adresu"));
                p.setIdSalonu(rs.getInt("id_salonu"));

                p.setMiasto(rs.getString("miasto"));
                p.setUlica(rs.getString("ulica"));
                p.setNumerBudynku(rs.getInt("numer_budynku"));
                p.setNumerLokalu(rs.getInt("numer_lokalu"));
                p.setKodPocztowy(rs.getString("kod_pocztowy"));
                p.setKraj(rs.getString("kraj"));
                return p;
            }, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void updateClient(UserProfile p) {
        String sql = """
            UPDATE KLIENCI
            SET imie = ?, nazwisko = ?, telefon = ?, email = ?
            WHERE id_klienta = ?
            """;
        jdbc.update(sql,
                p.getImie(),
                p.getNazwisko(),
                p.getTelefon(),
                p.getEmail(),
                p.getIdKlienta()
        );
    }

    public void updateAddress(UserProfile p) {
        String sql = """
            UPDATE ADRESY
            SET miasto = ?, ulica = ?, numer_budynku = ?, numer_lokalu = ?, kod_pocztowy = ?, kraj = ?
            WHERE id_adresu = ?
            """;
        jdbc.update(sql,
                p.getMiasto(),
                p.getUlica(),
                p.getNumerBudynku(),
                p.getNumerLokalu(),
                p.getKodPocztowy(),
                p.getKraj(),
                p.getIdAdresu()
        );
    }
}
