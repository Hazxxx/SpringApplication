package bada_project.SpringApplication.user.vehicles;

import java.math.BigDecimal;

/**
 * Model pojazdu - dane z tabel POJAZDY + MODELE + MARKI + OFERTY
 */
public class Vehicle {

    // Z tabeli POJAZDY
    private Integer idPojazdu;
    private String kolor;
    private String vin;

    // Z tabeli MODELE
    private Integer idModelu;
    private Integer pojemnoscSilnika;
    private Integer mocSilnika;
    private String typPaliwa;
    private Integer rocznikModelowy;
    private String typNadwozia;
    private Integer masaWlasna;

    // Z tabeli MARKI
    private Integer idMarki;
    private String nazwaMarki;

    // Z tabeli OFERTY
    private Integer idOferty;
    private BigDecimal cenaKatalogowa;
    private Integer idSalonu;

    // Informacje o salonie
    private String nazwaSalonu;
    private String telefonSalonu;
    private String miastoSalonu;
    private String ulicaSalonu;

    // Dane Sprzedawcy (opiekuna oferty)
    private String imieSprzedawcy;
    private String nazwiskoSprzedawcy;
    private String emailSprzedawcy;
    private String telefonSprzedawcy;

    // Status (czy sprzedany)
    private boolean sprzedany;

    // Zdjęcie (można dodać do DB później)
    private String zdjecieUrl;

    // Konstruktor
    public Vehicle() {
    }

    public String getZdjecieUrl() {
        return zdjecieUrl;
    }

    // Gettery i settery
    public Integer getIdPojazdu() {
        return idPojazdu;
    }

    public void setIdPojazdu(Integer idPojazdu) {
        this.idPojazdu = idPojazdu;
    }

    public String getKolor() {
        return kolor;
    }

    public void setKolor(String kolor) {
        this.kolor = kolor;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Integer getIdModelu() {
        return idModelu;
    }

    public void setIdModelu(Integer idModelu) {
        this.idModelu = idModelu;
    }

    public Integer getPojemnoscSilnika() {
        return pojemnoscSilnika;
    }

    public void setPojemnoscSilnika(Integer pojemnoscSilnika) {
        this.pojemnoscSilnika = pojemnoscSilnika;
    }

    public Integer getMocSilnika() {
        return mocSilnika;
    }

    public void setMocSilnika(Integer mocSilnika) {
        this.mocSilnika = mocSilnika;
    }

    public String getTypPaliwa() {
        return typPaliwa;
    }

    public void setTypPaliwa(String typPaliwa) {
        this.typPaliwa = typPaliwa;
    }

    public Integer getRocznikModelowy() {
        return rocznikModelowy;
    }

    public void setRocznikModelowy(Integer rocznikModelowy) {
        this.rocznikModelowy = rocznikModelowy;
    }

    public String getTypNadwozia() {
        return typNadwozia;
    }

    public void setTypNadwozia(String typNadwozia) {
        this.typNadwozia = typNadwozia;
    }

    public Integer getMasaWlasna() {
        return masaWlasna;
    }

    public void setMasaWlasna(Integer masaWlasna) {
        this.masaWlasna = masaWlasna;
    }

    public Integer getIdMarki() {
        return idMarki;
    }

    public void setIdMarki(Integer idMarki) {
        this.idMarki = idMarki;
    }

    public String getNazwaMarki() {
        return nazwaMarki;
    }

    public void setNazwaMarki(String nazwaMarki) {
        this.nazwaMarki = nazwaMarki;
    }

    public Integer getIdOferty() {
        return idOferty;
    }

    public void setIdOferty(Integer idOferty) {
        this.idOferty = idOferty;
    }

    public BigDecimal getCenaKatalogowa() {
        return cenaKatalogowa;
    }

    public void setCenaKatalogowa(BigDecimal cenaKatalogowa) {
        this.cenaKatalogowa = cenaKatalogowa;
    }

    public Integer getIdSalonu() {
        return idSalonu;
    }

    public void setIdSalonu(Integer idSalonu) {
        this.idSalonu = idSalonu;
    }

    public String getNazwaSalonu() {
        return nazwaSalonu;
    }

    public void setNazwaSalonu(String nazwaSalonu) {
        this.nazwaSalonu = nazwaSalonu;
    }

    public String getTelefonSalonu() {
        return telefonSalonu;
    }

    public void setTelefonSalonu(String telefonSalonu) {
        this.telefonSalonu = telefonSalonu;
    }

    public String getMiastoSalonu() {
        return miastoSalonu;
    }

    public void setMiastoSalonu(String miastoSalonu) {
        this.miastoSalonu = miastoSalonu;
    }

    public String getUlicaSalonu() {
        return ulicaSalonu;
    }

    public void setUlicaSalonu(String ulicaSalonu) {
        this.ulicaSalonu = ulicaSalonu;
    }

    public boolean isSprzedany() {
        return sprzedany;
    }

    public void setSprzedany(boolean sprzedany) {
        this.sprzedany = sprzedany;
    }

    public void setZdjecieUrl(String zdjecieUrl) {
        this.zdjecieUrl = zdjecieUrl;
    }

    public String getImieSprzedawcy() {
        return imieSprzedawcy;
    }

    public void setImieSprzedawcy(String imieSprzedawcy) {
        this.imieSprzedawcy = imieSprzedawcy;
    }

    public String getNazwiskoSprzedawcy() {
        return nazwiskoSprzedawcy;
    }

    public void setNazwiskoSprzedawcy(String nazwiskoSprzedawcy) {
        this.nazwiskoSprzedawcy = nazwiskoSprzedawcy;
    }

    public String getEmailSprzedawcy() {
        return emailSprzedawcy;
    }

    public void setEmailSprzedawcy(String emailSprzedawcy) {
        this.emailSprzedawcy = emailSprzedawcy;
    }

    public String getTelefonSprzedawcy() {
        return telefonSprzedawcy;
    }

    public void setTelefonSprzedawcy(String telefonSprzedawcy) {
        this.telefonSprzedawcy = telefonSprzedawcy;
    }

    // Helper methods
    public String getFullName() {
        return nazwaMarki;
    }

    public String getPriceFormatted() {
        if (cenaKatalogowa == null)
            return "Brak ceny";
        return String.format("%,.2f PLN", cenaKatalogowa);
    }

    public String getPojemnoscFormatted() {
        if (pojemnoscSilnika == null)
            return "-";
        return String.format("%.1f L", pojemnoscSilnika / 1000.0);
    }
}
