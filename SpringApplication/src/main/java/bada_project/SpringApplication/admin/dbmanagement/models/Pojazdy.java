package bada_project.SpringApplication.admin.dbmanagement.models;

public class Pojazdy {
    private Long idPojazdu;
    private String kolor;
    private String vin;
    private Long idModelu;

    // For display
    private String nazwaMarki;
    private String nazwaModelu;
    private Integer rocznikModelowy;
    private String typNadwozia;
    private Integer liczbaOfert; // statistics

    public Pojazdy() {}

    // Getters & Setters
    public Long getIdPojazdu() { return idPojazdu; }
    public void setIdPojazdu(Long idPojazdu) { this.idPojazdu = idPojazdu; }

    public String getKolor() { return kolor; }
    public void setKolor(String kolor) { this.kolor = kolor; }

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public Long getIdModelu() { return idModelu; }
    public void setIdModelu(Long idModelu) { this.idModelu = idModelu; }

    public String getNazwaMarki() { return nazwaMarki; }
    public void setNazwaMarki(String nazwaMarki) { this.nazwaMarki = nazwaMarki; }

    public String getNazwaModelu() { return nazwaModelu; }
    public void setNazwaModelu(String nazwaModelu) { this.nazwaModelu = nazwaModelu; }

    public Integer getRocznikModelowy() { return rocznikModelowy; }
    public void setRocznikModelowy(Integer rocznikModelowy) { this.rocznikModelowy = rocznikModelowy; }

    public String getTypNadwozia() { return typNadwozia; }
    public void setTypNadwozia(String typNadwozia) { this.typNadwozia = typNadwozia; }

    public Integer getLiczbaOfert() { return liczbaOfert; }
    public void setLiczbaOfert(Integer liczbaOfert) { this.liczbaOfert = liczbaOfert; }
}