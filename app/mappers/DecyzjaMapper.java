package mappers;

import controllers.data.DecyzjaData;
import models.Decyzja;
import models.Kierownik;
import models.RodzajDecyzji;
import scala.Enumeration;

public class DecyzjaMapper {

    public Decyzja mapDecyzja(DecyzjaData decyzjaData, Kierownik kierownik) {

        Enumeration.Value rodzajDecyzji = decyzjaData.getRodzajDecyzji().equals("Pozytywna") ? RodzajDecyzji.Pozytywa() :
                decyzjaData.getRodzajDecyzji().equals("Negatywna") ? RodzajDecyzji.Negatywna() : RodzajDecyzji.DoUzupelnienia();

        Decyzja decyzja = new Decyzja(
                -1,
                new java.sql.Date(new java.util.Date().getTime()),
                decyzjaData.getUzasadnienieDecyzji(),
                kierownik,
                rodzajDecyzji);

        return decyzja;
    }
}
