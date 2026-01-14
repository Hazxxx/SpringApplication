package bada_project.SpringApplication.admin.dbmanagement.models;

import java.math.BigDecimal;
import java.util.Date;

public class Wynagrodzenia {
    private Long idWynagrodzenia;
    private BigDecimal kwota;
    private String waluta;
    private Date dataKontraktu;

    public Wynagrodzenia() {}

    // Getters & Setters
    public Long getIdWynagrodzenia() { return idWynagrodzenia; }
    public void setIdWynagrodzenia(Long idWynagrodzenia) { this.idWynagrodzenia = idWynagrodzenia; }

    public BigDecimal getKwota() { return kwota; }
    public void setKwota(BigDecimal kwota) { this.kwota = kwota; }

    public String getWaluta() { return waluta; }
    public void setWaluta(String waluta) { this.waluta = waluta; }

    public Date getDataKontraktu() { return dataKontraktu; }
    public void setDataKontraktu(Date dataKontraktu) { this.dataKontraktu = dataKontraktu; }
}