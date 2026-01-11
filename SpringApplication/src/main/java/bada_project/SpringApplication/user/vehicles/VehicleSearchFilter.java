package bada_project.SpringApplication.user.vehicles;

import java.math.BigDecimal;

/**
 * Klasa filtra dla wyszukiwania pojazdów
 */
public class VehicleSearchFilter {

    private String nazwaMarki;
    private String typNadwozia;
    private String typPaliwa;
    private String kolor;
    private BigDecimal cenaOd;
    private BigDecimal cenaDo;
    private Integer rocznikOd;
    private Integer rocznikDo;
    private Integer idSalonu;

    public VehicleSearchFilter() {}

    // Gettery i settery
    public String getNazwaMarki() {
        return nazwaMarki;
    }

    public void setNazwaMarki(String nazwaMarki) {
        this.nazwaMarki = nazwaMarki;
    }

    public String getTypNadwozia() {
        return typNadwozia;
    }

    public void setTypNadwozia(String typNadwozia) {
        this.typNadwozia = typNadwozia;
    }

    public String getTypPaliwa() {
        return typPaliwa;
    }

    public void setTypPaliwa(String typPaliwa) {
        this.typPaliwa = typPaliwa;
    }

    public String getKolor() {
        return kolor;
    }

    public void setKolor(String kolor) {
        this.kolor = kolor;
    }

    public BigDecimal getCenaOd() {
        return cenaOd;
    }

    public void setCenaOd(BigDecimal cenaOd) {
        this.cenaOd = cenaOd;
    }

    public BigDecimal getCenaDo() {
        return cenaDo;
    }

    public void setCenaDo(BigDecimal cenaDo) {
        this.cenaDo = cenaDo;
    }

    public Integer getRocznikOd() {
        return rocznikOd;
    }

    public void setRocznikOd(Integer rocznikOd) {
        this.rocznikOd = rocznikOd;
    }

    public Integer getRocznikDo() {
        return rocznikDo;
    }

    public void setRocznikDo(Integer rocznikDo) {
        this.rocznikDo = rocznikDo;
    }

    public Integer getIdSalonu() {
        return idSalonu;
    }

    public void setIdSalonu(Integer idSalonu) {
        this.idSalonu = idSalonu;
    }

    // Helper method - sprawdza czy jakikolwiek filtr jest ustawiony
    public boolean hasAnyFilter() {
        return (nazwaMarki != null && !nazwaMarki.isEmpty()) ||
                (typNadwozia != null && !typNadwozia.isEmpty()) ||
                (typPaliwa != null && !typPaliwa.isEmpty()) ||
                (kolor != null && !kolor.isEmpty()) ||
                cenaOd != null ||
                cenaDo != null ||
                rocznikOd != null ||
                rocznikDo != null ||
                idSalonu != null;
    }
}
