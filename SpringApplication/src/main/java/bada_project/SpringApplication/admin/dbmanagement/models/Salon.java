package bada_project.SpringApplication.admin.dbmanagement.models;

public class Salon {
    private Long idSalonu;
    private String nazwa;
    private String telefon;
    private Long idAdresu;
    private Long idFirmyPartnerskiej;

    // For display
    private String nazwaFirmy;
    private String miasto;
    private String ulica;
    private Integer liczbaPracownikow;

    public Salon() {}

    // Getters & Setters
    public Long getIdSalonu() { return idSalonu; }
    public void setIdSalonu(Long idSalonu) { this.idSalonu = idSalonu; }

    public String getNazwa() { return nazwa; }
    public void setNazwa(String nazwa) { this.nazwa = nazwa; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public Long getIdAdresu() { return idAdresu; }
    public void setIdAdresu(Long idAdresu) { this.idAdresu = idAdresu; }

    public Long getIdFirmyPartnerskiej() { return idFirmyPartnerskiej; }
    public void setIdFirmyPartnerskiej(Long idFirmyPartnerskiej) { this.idFirmyPartnerskiej = idFirmyPartnerskiej; }

    public String getNazwaFirmy() { return nazwaFirmy; }
    public void setNazwaFirmy(String nazwaFirmy) { this.nazwaFirmy = nazwaFirmy; }

    public String getMiasto() { return miasto; }
    public void setMiasto(String miasto) { this.miasto = miasto; }

    public String getUlica() { return ulica; }
    public void setUlica(String ulica) { this.ulica = ulica; }

    public Integer getLiczbaPracownikow() { return liczbaPracownikow; }
    public void setLiczbaPracownikow(Integer liczbaPracownikow) { this.liczbaPracownikow = liczbaPracownikow; }

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