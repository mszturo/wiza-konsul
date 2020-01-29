package mappers;

import controllers.data.SprawaData;
import models.DaneOsobowe;
import models.DokumentIdentyfikacyjny;
import models.Sprawa;
import models.TypDokumentu;
import repositories.TypDokumentuRepo;
import scala.Option;
import services.SprawaService;

import java.sql.Date;
import java.sql.Timestamp;

import javax.inject.Inject;

public class SprawaMapper {

    @Inject
    private SprawaService sprawaService;

    public Sprawa mapSprawa(SprawaData sprawaData) {
        Option<TypDokumentu> typDokumentu = sprawaService.getTypDokumentu(Integer.parseInt(sprawaData.getTypDokumentu()));

        if(!typDokumentu.isEmpty()) {
            DokumentIdentyfikacyjny dokumentIdentyfikacyjny = new DokumentIdentyfikacyjny(-1, sprawaData.getNumerDokumentuIdentyfikacyjnego(), typDokumentu.get());

            DaneOsobowe daneOsobowe = new DaneOsobowe(
                    -1,
                    sprawaData.getImie(),
                    sprawaData.getDrugieImie() == null ? Option.empty() : Option.apply(sprawaData.getDrugieImie()),
                    sprawaData.getNazwisko(),
                    new java.sql.Date(sprawaData.getDataUrodzenia().getTime()),
                    sprawaData.getMiejsceUrodzenia(),
                    sprawaData.getPESEL(),
                    dokumentIdentyfikacyjny);

            Sprawa sprawa = new Sprawa(
                    -1,
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

        return null;
    }
}
