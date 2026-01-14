package bada_project.SpringApplication.dao;

import bada_project.SpringApplication.auth.RegisterForm;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("adresyDAO")
public class AdresyDAO {

    private final JdbcTemplate jdbc;

    // Jeśli Twoja sekwencja nazywa się inaczej – zmień tutaj:
    private static final String ADRESY_SEQ = "ADRESY_SEQ";

    public AdresyDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Long insertAdresAndReturnId(RegisterForm f) {

        Long idAdresu = jdbc.queryForObject(
                "SELECT " + ADRESY_SEQ + ".NEXTVAL FROM DUAL",
                Long.class
        );

        if (idAdresu == null) {
            throw new IllegalStateException("Nie udało się pobrać NEXTVAL z sekwencji " + ADRESY_SEQ);
        }

        // DOPASUJ NAZWY KOLUMN DO TWOJEJ TABELI ADRESY:
        jdbc.update("""
    INSERT INTO ADRESY (
        ID_ADRESU,
        ULICA,
        NUMER_BUDYNKU,
        NUMER_LOKALU,
        KOD_POCZTOWY,
        MIASTO,
        KRAJ
    )
    VALUES (?, ?, ?, ?, ?, ?, ?)
    """,
                idAdresu,
                f.getStreet(),
                f.getHouseNumber(),
                f.getApartmentNumber(),
                f.getPostalCode(),
                f.getCity(),
                "Polska"   // <- NA SZTYWNO, BO FORMULARZ NIE MA POLA
        );


        return idAdresu;
    }
}
