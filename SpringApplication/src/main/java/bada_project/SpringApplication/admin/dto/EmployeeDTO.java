package bada_project.SpringApplication.admin.dto;

import java.math.BigDecimal;

public class EmployeeDTO {
    private Long idPracownika;
    private String email;
    private String imie;
    private String nazwisko;
    private String telefon;
    private BigDecimal idWynagrodzenia;
    private Long idStanowiska;
    private String nazwaStanowiska;
    private boolean czyAdmin;
    private Long idSalonu;
    private String nazwaSalonu;

    // New field for password change
    private String newPassword;

    // Getters and Setters
    public Long getIdPracownika() { return idPracownika; }
    public void setIdPracownika(Long idPracownika) { this.idPracownika = idPracownika; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getImie() { return imie; }
    public void setImie(String imie) { this.imie = imie; }

    public String getNazwisko() { return nazwisko; }
    public void setNazwisko(String nazwisko) { this.nazwisko = nazwisko; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public BigDecimal getIdWynagrodzenia() { return idWynagrodzenia; }
    public void setIdWynagrodzenia(BigDecimal idWynagrodzenia) { this.idWynagrodzenia = idWynagrodzenia; }

    public Long getIdStanowiska() { return idStanowiska; }
    public void setIdStanowiska(Long idStanowiska) { this.idStanowiska = idStanowiska; }

    public String getNazwaStanowiska() { return nazwaStanowiska; }
    public void setNazwaStanowiska(String nazwaStanowiska) { this.nazwaStanowiska = nazwaStanowiska; }

    public boolean isCzyAdmin() { return czyAdmin; }
    public void setCzyAdmin(boolean czyAdmin) { this.czyAdmin = czyAdmin; }

    public Long getIdSalonu() { return idSalonu; }
    public void setIdSalonu(Long idSalonu) { this.idSalonu = idSalonu; }

    public String getNazwaSalonu() { return nazwaSalonu; }
    public void setNazwaSalonu(String nazwaSalonu) { this.nazwaSalonu = nazwaSalonu; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    // Computed property for full name
    public String getFullName() {
        if (imie == null && nazwisko == null) {
            return "";
        }
        if (imie == null) {
            return nazwisko;
        }
        if (nazwisko == null) {
            return imie;
        }
        return imie + " " + nazwisko;
    }
}