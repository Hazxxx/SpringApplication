package bada_project.SpringApplication.admin.dbmanagement.models;

public class Stanowisko {

    private Long idStanowiska;
    private String nazwa;
    private Integer minimalneDoswiadczenieLata;
    private Boolean czyKierownicze;
    private Boolean czyAdmin;
    private Integer liczbaPracownikow;

    public Long getIdStanowiska() {
        return idStanowiska;
    }

    public void setIdStanowiska(Long idStanowiska) {
        this.idStanowiska = idStanowiska;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public Integer getMinimalneDoswiadczenieLata() {
        return minimalneDoswiadczenieLata;
    }

    public void setMinimalneDoswiadczenieLata(Integer minimalneDoswiadczenieLata) {
        this.minimalneDoswiadczenieLata = minimalneDoswiadczenieLata;
    }

    public Boolean getCzyKierownicze() {
        return czyKierownicze;
    }

    public void setCzyKierownicze(Boolean czyKierownicze) {
        this.czyKierownicze = czyKierownicze;
    }

    public Boolean getCzyAdmin() {
        return czyAdmin;
    }

    public void setCzyAdmin(Boolean czyAdmin) {
        this.czyAdmin = czyAdmin;
    }

    public Integer getLiczbaPracownikow() {
        return liczbaPracownikow;
    }

    public void setLiczbaPracownikow(Integer liczbaPracownikow) {
        this.liczbaPracownikow = liczbaPracownikow;
    }
}
