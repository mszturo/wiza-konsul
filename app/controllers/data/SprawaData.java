package controllers.data;

import java.util.Date;

import models.TypDokumentu;
import play.api.data.format.Formats;
import play.data.validation.Constraints;

public class SprawaData {

    public SprawaData() {}

    @Constraints.Required
    private String imie;

    private String drugieImie;

    @Constraints.Required
    private String nazwisko;

    @Constraints.Required
    private Date dataUrodzenia;

    @Constraints.Required
    private String miejsceUrodzenia;

    @Constraints.Required
    @Constraints.MinLength(11)
    @Constraints.MaxLength(11)
    private String PESEL;

    @Constraints.Required
    private String numerDokumentuIdentyfikacyjnego;

    @Constraints.Required
    private String typDokumentu;

    @Constraints.Required
    private String trescSprawy;

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getDrugieImie() {
        return drugieImie;
    }

    public void setDrugieImie(String drugieImie) {
        this.drugieImie = drugieImie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public Date getDataUrodzenia() {
        return dataUrodzenia;
    }

    public void setDataUrodzenia(Date dataUrodzenia) {
        this.dataUrodzenia = dataUrodzenia;
    }

    public String getMiejsceUrodzenia() {
        return miejsceUrodzenia;
    }

    public void setMiejsceUrodzenia(String miejsceUrodzenia) {
        this.miejsceUrodzenia = miejsceUrodzenia;
    }

    public String getPESEL() {
        return PESEL;
    }

    public void setPESEL(String PESEL) {
        this.PESEL = PESEL;
    }

    public String getNumerDokumentuIdentyfikacyjnego() {
        return numerDokumentuIdentyfikacyjnego;
    }

    public void setNumerDokumentuIdentyfikacyjnego(String numerDokumentuIdentyfikacyjnego) {
        this.numerDokumentuIdentyfikacyjnego = numerDokumentuIdentyfikacyjnego;
    }

    public String getTrescSprawy() {
        return trescSprawy;
    }

    public void setTrescSprawy(String trescSprawy) {
        this.trescSprawy = trescSprawy;
    }

    public String getTypDokumentu() {
        return typDokumentu;
    }

    public void setTypDokumentu(String typDokumentu) {
        this.typDokumentu = typDokumentu;
    }
}
