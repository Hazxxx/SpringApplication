package bada_project.SpringApplication.admin.dto;

public class ClientDTO {
    private Long idKlienta;
    private String email;
    private String imie;
    private String nazwisko;
    private String telefon;

    // Adres
    private Long idAdresu;
    private String miasto;
    private String ulica;
    private String numerBudynku;
    private String numerLokalu;
    private String kodPocztowy;
    private String kraj;

    // Salon
    private Long idSalonu;
    private String nazwaSalonu;

    // Constructors
    public ClientDTO() {}

    // Getters and Setters
    public Long getIdKlienta() { return idKlienta; }
    public void setIdKlienta(Long idKlienta) { this.idKlienta = idKlienta; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getImie() { return imie; }
    public void setImie(String imie) { this.imie = imie; }

    public String getNazwisko() { return nazwisko; }
    public void setNazwisko(String nazwisko) { this.nazwisko = nazwisko; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public Long getIdAdresu() { return idAdresu; }
    public void setIdAdresu(Long idAdresu) { this.idAdresu = idAdresu; }

    public String getMiasto() { return miasto; }
    public void setMiasto(String miasto) { this.miasto = miasto; }

    public String getUlica() { return ulica; }
    public void setUlica(String ulica) { this.ulica = ulica; }

    public String getNumerBudynku() { return numerBudynku; }
    public void setNumerBudynku(String numerBudynku) { this.numerBudynku = numerBudynku; }

    public String getNumerLokalu() { return numerLokalu; }
    public void setNumerLokalu(String numerLokalu) { this.numerLokalu = numerLokalu; }

    public String getKodPocztowy() { return kodPocztowy; }
    public void setKodPocztowy(String kodPocztowy) { this.kodPocztowy = kodPocztowy; }

    public String getKraj() { return kraj; }
    public void setKraj(String kraj) { this.kraj = kraj; }

    public Long getIdSalonu() { return idSalonu; }
    public void setIdSalonu(Long idSalonu) { this.idSalonu = idSalonu; }

    public String getNazwaSalonu() { return nazwaSalonu; }
    public void setNazwaSalonu(String nazwaSalonu) { this.nazwaSalonu = nazwaSalonu; }

    public String getFullName() {
        return imie + " " + nazwisko;
    }

    public String getFullAddress() {
        String addr = ulica + " " + numerBudynku;
        if (numerLokalu != null && !numerLokalu.isEmpty() && !numerLokalu.equals("0")) {
            addr += "/" + numerLokalu;
        }
        addr += ", " + kodPocztowy + " " + miasto;
        return addr;
    }
}