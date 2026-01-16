package bada_project.SpringApplication.admin.dbmanagement.models;

public class Pojazdy {

    // === POJAZDY ===
    private Long idPojazdu;
    private String kolor;
    private String vin;
    private Long idModelu;
    private String zdjecieUrl;

    // === DO WYSWIETLANIA (JOINY) ===
    private String nazwaMarki;
    private Integer rocznikModelowy;
    private String typNadwozia;
    private Integer liczbaOfert;

    public Pojazdy() {}

    // ===== GETTERY / SETTERY =====

    public Long getIdPojazdu() {
        return idPojazdu;
    }

    public void setIdPojazdu(Long idPojazdu) {
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

    public Long getIdModelu() {
        return idModelu;
    }

    public void setIdModelu(Long idModelu) {
        this.idModelu = idModelu;
    }

    public String getZdjecieUrl() {
        return zdjecieUrl;
    }

    public void setZdjecieUrl(String zdjecieUrl) {
        this.zdjecieUrl = zdjecieUrl;
    }

    public String getNazwaMarki() {
        return nazwaMarki;
    }

    public void setNazwaMarki(String nazwaMarki) {
        this.nazwaMarki = nazwaMarki;
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

    public Integer getLiczbaOfert() {
        return liczbaOfert;
    }

    public void setLiczbaOfert(Integer liczbaOfert) {
        this.liczbaOfert = liczbaOfert;
    }
}
