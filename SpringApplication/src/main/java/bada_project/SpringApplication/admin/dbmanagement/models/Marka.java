package bada_project.SpringApplication.admin.dbmanagement.models;

public class Marka {
    private Long idMarki;
    private String nazwa;
    private Integer liczbaModeli; // dla statystyk

    public Marka() {}

    public Marka(Long idMarki, String nazwa) {
        this.idMarki = idMarki;
        this.nazwa = nazwa;
    }

    // Getters & Setters
    public Long getIdMarki() { return idMarki; }
    public void setIdMarki(Long idMarki) { this.idMarki = idMarki; }

    public String getNazwa() { return nazwa; }
    public void setNazwa(String nazwa) { this.nazwa = nazwa; }

    public Integer getLiczbaModeli() { return liczbaModeli; }
    public void setLiczbaModeli(Integer liczbaModeli) { this.liczbaModeli = liczbaModeli; }
}