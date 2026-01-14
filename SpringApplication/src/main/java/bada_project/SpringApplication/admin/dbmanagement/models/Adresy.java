package bada_project.SpringApplication.admin.dbmanagement.models;

public class Adresy {
    private Long idAdresu;
    private String miasto;
    private String ulica;
    private Integer numerBudynku;
    private Integer numerLokalu;
    private String kodPocztowy;
    private String kraj;

    public Adresy() {}

    // Getters & Setters
    public Long getIdAdresu() { return idAdresu; }
    public void setIdAdresu(Long idAdresu) { this.idAdresu = idAdresu; }

    public String getMiasto() { return miasto; }
    public void setMiasto(String miasto) { this.miasto = miasto; }

    public String getUlica() { return ulica; }
    public void setUlica(String ulica) { this.ulica = ulica; }

    public Integer getNumerBudynku() { return numerBudynku; }
    public void setNumerBudynku(Integer numerBudynku) { this.numerBudynku = numerBudynku; }

    public Integer getNumerLokalu() { return numerLokalu; }
    public void setNumerLokalu(Integer numerLokalu) { this.numerLokalu = numerLokalu; }

    public String getKodPocztowy() { return kodPocztowy; }
    public void setKodPocztowy(String kodPocztowy) { this.kodPocztowy = kodPocztowy; }

    public String getKraj() { return kraj; }
    public void setKraj(String kraj) { this.kraj = kraj; }
}