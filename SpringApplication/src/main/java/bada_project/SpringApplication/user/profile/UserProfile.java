package bada_project.SpringApplication.user.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserProfile {

    // z bazy
    private Integer idKlienta;
    private Integer idAdresu;

    // login z Spring Security
    private String username;

    // KLIENCI
    @NotBlank(message = "Imię jest wymagane")
    @Size(max = 30)
    private String imie;

    @NotBlank(message = "Nazwisko jest wymagane")
    @Size(max = 30)
    private String nazwisko;

    @NotBlank(message = "Telefon jest wymagany")
    @Size(max = 12)
    private String telefon;

    @NotBlank(message = "Email jest wymagany")
    @Email(message = "Niepoprawny email")
    @Size(max = 50)
    private String email;

    private Integer idSalonu;

    // ADRESY
    @NotBlank(message = "Miasto jest wymagane")
    @Size(max = 30)
    private String miasto;

    @NotBlank(message = "Ulica jest wymagana")
    @Size(max = 50)
    private String ulica;

    private Integer numerBudynku; // w DB INTEGER NOT NULL
    private Integer numerLokalu;  // w DB w waszym skrypcie wygląda na NOT NULL -> podaj 0 jeśli brak

    @NotBlank(message = "Kod pocztowy jest wymagany")
    @Size(max = 6)
    private String kodPocztowy;

    @NotBlank(message = "Kraj jest wymagany")
    @Size(max = 30)
    private String kraj;

    // getters/setters

    public Integer getIdKlienta() { return idKlienta; }
    public void setIdKlienta(Integer idKlienta) { this.idKlienta = idKlienta; }

    public Integer getIdAdresu() { return idAdresu; }
    public void setIdAdresu(Integer idAdresu) { this.idAdresu = idAdresu; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getImie() { return imie; }
    public void setImie(String imie) { this.imie = imie; }

    public String getNazwisko() { return nazwisko; }
    public void setNazwisko(String nazwisko) { this.nazwisko = nazwisko; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getIdSalonu() { return idSalonu; }
    public void setIdSalonu(Integer idSalonu) { this.idSalonu = idSalonu; }

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
