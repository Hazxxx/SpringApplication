package bada_project.SpringApplication.admin.dbmanagement.models;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Sprzedawcy {
    private Long idPracownika;
    private String imie;
    private String nazwisko;
    private String telefon;
    private String email;
    private Character plec;
    private String pesel;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date dataUrodzenia;

    private Long idAdresu;
    private Long idSalonu;
    private Long idStanowiska;
    private Long idWynagrodzenia;

    // For display
    private String miasto;
    private String ulica;
    private String nazwaSalonu;
    private String nazwaStanowiska;
    private Boolean czyKierownicze;
    private Integer liczbaSprzedazy; // statistics

    // Address fields for form
    private String nowyMiasto;
    private String nowaUlica;
    private Integer nowyNumerBudynku;
    private Integer nowyNumerLokalu;
    private String nowyKodPocztowy;
    private String nowyKraj;

    public Sprzedawcy() {}

    // Getters & Setters
    public Long getIdPracownika() { return idPracownika; }
    public void setIdPracownika(Long idPracownika) { this.idPracownika = idPracownika; }

    public String getImie() { return imie; }
    public void setImie(String imie) { this.imie = imie; }

    public String getNazwisko() { return nazwisko; }
    public void setNazwisko(String nazwisko) { this.nazwisko = nazwisko; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Character getPlec() { return plec; }
    public void setPlec(Character plec) { this.plec = plec; }

    public String getPesel() { return pesel; }
    public void setPesel(String pesel) { this.pesel = pesel; }

    public Date getDataUrodzenia() { return dataUrodzenia; }
    public void setDataUrodzenia(Date dataUrodzenia) { this.dataUrodzenia = dataUrodzenia; }

    public Long getIdAdresu() { return idAdresu; }
    public void setIdAdresu(Long idAdresu) { this.idAdresu = idAdresu; }

    public Long getIdSalonu() { return idSalonu; }
    public void setIdSalonu(Long idSalonu) { this.idSalonu = idSalonu; }

    public Long getIdStanowiska() { return idStanowiska; }
    public void setIdStanowiska(Long idStanowiska) { this.idStanowiska = idStanowiska; }

    public Long getIdWynagrodzenia() { return idWynagrodzenia; }
    public void setIdWynagrodzenia(Long idWynagrodzenia) { this.idWynagrodzenia = idWynagrodzenia; }

    public String getMiasto() { return miasto; }
    public void setMiasto(String miasto) { this.miasto = miasto; }

    public String getUlica() { return ulica; }
    public void setUlica(String ulica) { this.ulica = ulica; }

    public String getNazwaSalonu() { return nazwaSalonu; }
    public void setNazwaSalonu(String nazwaSalonu) { this.nazwaSalonu = nazwaSalonu; }

    public String getNazwaStanowiska() { return nazwaStanowiska; }
    public void setNazwaStanowiska(String nazwaStanowiska) { this.nazwaStanowiska = nazwaStanowiska; }

    public Boolean getCzyKierownicze() { return czyKierownicze; }
    public void setCzyKierownicze(Boolean czyKierownicze) { this.czyKierownicze = czyKierownicze; }

    public Integer getLiczbaSprzedazy() { return liczbaSprzedazy; }
    public void setLiczbaSprzedazy(Integer liczbaSprzedazy) { this.liczbaSprzedazy = liczbaSprzedazy; }

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