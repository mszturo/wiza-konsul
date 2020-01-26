package mappers;

import controllers.data.SprawaData;
import models.DaneOsobowe;
import models.DokumentIdentyfikacyjny;
import models.Sprawa;
import models.TypDokumentu;
import repositories.TypDokumentuRepo;
import scala.Option;
import java.sql.Date;
import java.sql.Timestamp;

import javax.inject.Inject;

public class SprawaMapper {

    public static Sprawa mapSprawa(SprawaData sprawaData) {
        DokumentIdentyfikacyjny dokumentIdentyfikacyjny = new DokumentIdentyfikacyjny(1, sprawaData.getNumerDokumentuIdentyfikacyjnego(), null);


        DaneOsobowe daneOsobowe = new DaneOsobowe(
                1,
                sprawaData.getImie(),
                sprawaData.getDrugieImie() == null ? Option.empty() : Option.apply(sprawaData.getDrugieImie()),
                sprawaData.getNazwisko(),
                sprawaData.getDataUrodzenia(),
                sprawaData.getMiejsceUrodzenia(),
                sprawaData.getPESEL(),
                dokumentIdentyfikacyjny);

        Sprawa sprawa = new Sprawa(
                1,
                "",
                sprawaData.getTrescSprawy(),
                "",
                new Timestamp(new java.util.Date().getTime()),
                false,
                daneOsobowe,
                null,
                false);

        return sprawa;
    }
}
