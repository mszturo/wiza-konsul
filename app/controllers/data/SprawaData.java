package controllers.data;

import java.util.Date;
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
    private String PESEL;

    @Constraints.Required
    private String numerDokumentuIdentyfikacyjnego;

    @Constraints.Required
    private String trescSprawy;
}
