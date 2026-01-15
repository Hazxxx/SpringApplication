package bada_project.SpringApplication.admin.dbmanagement.models;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

public class Sprzedaze {
    private Long idSprzedazy;

    @NotNull(message = "Employee is required")
    private Long idPracownika;

    @NotNull(message = "Customer is required")
    private Long idKlienta;

    @NotNull(message = "Offer is required")
    private Long idOferty;

    @NotNull(message = "Sale amount is required")
    @DecimalMin(value = "0.01", message = "Sale amount must be greater than 0")
    private BigDecimal kwotaSprzedazy;

    @NotNull(message = "Sale date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataSprzedazy;

    // For display only
    private String imiePracownika;
    private String nazwiskoPracownika;
    private String imieKlienta;
    private String nazwiskoKlienta;
    private String vinPojazdu;
    private String nazwaMarki;
    private BigDecimal cenaKatalogowa;

    public Sprzedaze() {}

    // Getters & Setters
    public Long getIdSprzedazy() { return idSprzedazy; }
    public void setIdSprzedazy(Long idSprzedazy) { this.idSprzedazy = idSprzedazy; }

    public Long getIdPracownika() { return idPracownika; }
    public void setIdPracownika(Long idPracownika) { this.idPracownika = idPracownika; }

    public Long getIdKlienta() { return idKlienta; }
    public void setIdKlienta(Long idKlienta) { this.idKlienta = idKlienta; }

    public Long getIdOferty() { return idOferty; }
    public void setIdOferty(Long idOferty) { this.idOferty = idOferty; }

    public BigDecimal getKwotaSprzedazy() { return kwotaSprzedazy; }
    public void setKwotaSprzedazy(BigDecimal kwotaSprzedazy) { this.kwotaSprzedazy = kwotaSprzedazy; }

    public Date getDataSprzedazy() { return dataSprzedazy; }
    public void setDataSprzedazy(Date dataSprzedazy) { this.dataSprzedazy = dataSprzedazy; }

    public String getImiePracownika() { return imiePracownika; }
    public void setImiePracownika(String imiePracownika) { this.imiePracownika = imiePracownika; }

    public String getNazwiskoPracownika() { return nazwiskoPracownika; }
    public void setNazwiskoPracownika(String nazwiskoPracownika) { this.nazwiskoPracownika = nazwiskoPracownika; }

    public String getImieKlienta() { return imieKlienta; }
    public void setImieKlienta(String imieKlienta) { this.imieKlienta = imieKlienta; }

    public String getNazwiskoKlienta() { return nazwiskoKlienta; }
    public void setNazwiskoKlienta(String nazwiskoKlienta) { this.nazwiskoKlienta = nazwiskoKlienta; }

    public String getVinPojazdu() { return vinPojazdu; }
    public void setVinPojazdu(String vinPojazdu) { this.vinPojazdu = vinPojazdu; }

    public String getNazwaMarki() { return nazwaMarki; }
    public void setNazwaMarki(String nazwaMarki) { this.nazwaMarki = nazwaMarki; }

    public BigDecimal getCenaKatalogowa() { return cenaKatalogowa; }
    public void setCenaKatalogowa(BigDecimal cenaKatalogowa) { this.cenaKatalogowa = cenaKatalogowa; }
}