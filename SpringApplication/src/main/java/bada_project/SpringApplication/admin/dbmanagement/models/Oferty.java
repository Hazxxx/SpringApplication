package bada_project.SpringApplication.admin.dbmanagement.models;

import java.math.BigDecimal;

public class Oferty {
    private Long idOferty;
    private Long idSalonu;
    private Long idPojazdu;
    private BigDecimal cenaKatalogowa;
    private Integer idPojazdu2; // duplicate field from schema

    // For display
    private String nazwaSalonu;
    private String vinPojazdu;
    private String nazwaMarki;
    private String nazwaModelu;
    private Integer liczbaSprzedazy; // statistics

    public Oferty() {}

    // Getters & Setters
    public Long getIdOferty() { return idOferty; }
    public void setIdOferty(Long idOferty) { this.idOferty = idOferty; }

    public Long getIdSalonu() { return idSalonu; }
    public void setIdSalonu(Long idSalonu) { this.idSalonu = idSalonu; }

    public Long getIdPojazdu() { return idPojazdu; }
    public void setIdPojazdu(Long idPojazdu) { this.idPojazdu = idPojazdu; }

    public BigDecimal getCenaKatalogowa() { return cenaKatalogowa; }
    public void setCenaKatalogowa(BigDecimal cenaKatalogowa) { this.cenaKatalogowa = cenaKatalogowa; }

    public Integer getIdPojazdu2() { return idPojazdu2; }
    public void setIdPojazdu2(Integer idPojazdu2) { this.idPojazdu2 = idPojazdu2; }

    public String getNazwaSalonu() { return nazwaSalonu; }
    public void setNazwaSalonu(String nazwaSalonu) { this.nazwaSalonu = nazwaSalonu; }

    public String getVinPojazdu() { return vinPojazdu; }
    public void setVinPojazdu(String vinPojazdu) { this.vinPojazdu = vinPojazdu; }

    public String getNazwaMarki() { return nazwaMarki; }
    public void setNazwaMarki(String nazwaMarki) { this.nazwaMarki = nazwaMarki; }

    public String getNazwaModelu() { return nazwaModelu; }
    public void setNazwaModelu(String nazwaModelu) { this.nazwaModelu = nazwaModelu; }

    public Integer getLiczbaSprzedazy() { return liczbaSprzedazy; }
    public void setLiczbaSprzedazy(Integer liczbaSprzedazy) { this.liczbaSprzedazy = liczbaSprzedazy; }
}