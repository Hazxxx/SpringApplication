// ===== FirmaPartnerska.java =====
package bada_project.SpringApplication.admin.dbmanagement.models;

public class FirmaPartnerska {
    private Long idFirmyPartnerskiej;
    private String nazwa;
    private String email;
    private String telefon;
    private Long idAdresu;

    // For display
    private String miasto;
    private String ulica;
    private Integer liczbaSalonow; // statistics

    public FirmaPartnerska() {}

    // Getters & Setters
    public Long getIdFirmyPartnerskiej() { return idFirmyPartnerskiej; }
    public void setIdFirmyPartnerskiej(Long idFirmyPartnerskiej) { this.idFirmyPartnerskiej = idFirmyPartnerskiej; }

    public String getNazwa() { return nazwa; }
    public void setNazwa(String nazwa) { this.nazwa = nazwa; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public Long getIdAdresu() { return idAdresu; }
    public void setIdAdresu(Long idAdresu) { this.idAdresu = idAdresu; }

    public String getMiasto() { return miasto; }
    public void setMiasto(String miasto) { this.miasto = miasto; }

    public String getUlica() { return ulica; }
    public void setUlica(String ulica) { this.ulica = ulica; }

    public Integer getLiczbaSalonow() { return liczbaSalonow; }
    public void setLiczbaSalonow(Integer liczbaSalonow) { this.liczbaSalonow = liczbaSalonow; }

    // Address fields for form
    private String nowyMiasto;
    private String nowaUlica;
    private Integer nowyNumerBudynku;
    private Integer nowyNumerLokalu;
    private String nowyKodPocztowy;
    private String nowyKraj;

    public String getNowyMiasto() { return nowyMiasto; }
    public void setNowyMiasto(String nowyMiasto) { this.nowyMiasto = nowyMiasto; }

    public String getNowaUlica() { return nowaUlica; }
    public void setNowaUlica(String nowaUlica) { this.nowaUlica = nowaUlica; }

    public Integer getNowyNumerBudynku() { return nowyNumerBudynku; }
    public void setNowyNumerBudynku(Integer nowyNumerBudynku) { this.nowyNumerBudynku = nowyNumerBudynku; }

    public Integer getNowyNumerLokalu() { return nowyNumerLokalu; }
    public void setNowyNumerLokalu(Integer nowyNumerLokalu) { this.nowyNumerLokalu = nowyNumerLokalu; }

    public String getNowyKodPocztowy() { return nowyKodPocztowy; }
    public void setNowyKodPocztowy(String nowyKodPocztowy) { this.nowyKodPocztowy = nowyKodPocztowy; }

    public String getNowyKraj() { return nowyKraj; }
    public void setNowyKraj(String nowyKraj) { this.nowyKraj = nowyKraj; }
}