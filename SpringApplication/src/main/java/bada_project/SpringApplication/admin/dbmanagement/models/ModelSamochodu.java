package bada_project.SpringApplication.admin.dbmanagement.models;

public class ModelSamochodu {
    private Long idModelu;
    private Long idMarki;
    private String nazwaMarki; // for display
    private Integer pojemnoscSilnika;
    private Integer mocSilnika;
    private String typPaliwa;
    private Integer rocznikModelowy;
    private String typNadwozia;
    private Integer masaWlasna;
    private Integer liczbaPojazdow; // statistics

    public ModelSamochodu() {}

    // Getters & Setters
    public Long getIdModelu() { return idModelu; }
    public void setIdModelu(Long idModelu) { this.idModelu = idModelu; }

    public Long getIdMarki() { return idMarki; }
    public void setIdMarki(Long idMarki) { this.idMarki = idMarki; }

    public String getNazwaMarki() { return nazwaMarki; }
    public void setNazwaMarki(String nazwaMarki) { this.nazwaMarki = nazwaMarki; }

    public Integer getPojemnoscSilnika() { return pojemnoscSilnika; }
    public void setPojemnoscSilnika(Integer pojemnoscSilnika) { this.pojemnoscSilnika = pojemnoscSilnika; }

    public Integer getMocSilnika() { return mocSilnika; }
    public void setMocSilnika(Integer mocSilnika) { this.mocSilnika = mocSilnika; }

    public String getTypPaliwa() { return typPaliwa; }
    public void setTypPaliwa(String typPaliwa) { this.typPaliwa = typPaliwa; }

    public Integer getRocznikModelowy() { return rocznikModelowy; }
    public void setRocznikModelowy(Integer rocznikModelowy) { this.rocznikModelowy = rocznikModelowy; }

    public String getTypNadwozia() { return typNadwozia; }
    public void setTypNadwozia(String typNadwozia) { this.typNadwozia = typNadwozia; }

    public Integer getMasaWlasna() { return masaWlasna; }
    public void setMasaWlasna(Integer masaWlasna) { this.masaWlasna = masaWlasna; }

    public Integer getLiczbaPojazdow() { return liczbaPojazdow; }
    public void setLiczbaPojazdow(Integer liczbaPojazdow) { this.liczbaPojazdow = liczbaPojazdow; }

    public String getDisplayName() {
        return nazwaMarki + " " + rocznikModelowy + " (" + typNadwozia + ")";
    }
}