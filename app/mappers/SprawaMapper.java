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

    public Sprawa mapSprawa(SprawaData sprawaData, Sprawa persistentSprawa) {
        Option<TypDokumentu> typDokumentu = sprawaService.getTypDokumentu(Integer.parseInt(sprawaData.getTypDokumentu()));

        if(!typDokumentu.isEmpty()) {
            DokumentIdentyfikacyjny dokumentIdentyfikacyjny = new DokumentIdentyfikacyjny(
                    persistentSprawa.daneOsobowe().dokumentIdentyfikacyjny().id(),
                    sprawaData.getNumerDokumentuIdentyfikacyjnego(),
                    typDokumentu.get());

            DaneOsobowe daneOsobowe = new DaneOsobowe(
                    persistentSprawa.daneOsobowe().id(),
                    sprawaData.getImie(),
                    sprawaData.getDrugieImie() == null ? Option.empty() : Option.apply(sprawaData.getDrugieImie()),
                    sprawaData.getNazwisko(),
                    new java.sql.Date(sprawaData.getDataUrodzenia().getTime()),
                    sprawaData.getMiejsceUrodzenia(),
                    sprawaData.getPESEL(),
                    dokumentIdentyfikacyjny);

            Sprawa sprawa = new Sprawa(
                    persistentSprawa.id(),
                    "",
                    sprawaData.getTrescSprawy(),
                    "",
                    new Timestamp(new java.util.Date().getTime()),
                    false,
                    daneOsobowe,
                    persistentSprawa.aktualnaDecyzja(),
                    persistentSprawa.czyZakonczona());

            return sprawa;
        }

        return null;
    }

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
                    Option.empty(),
                    false);

            return sprawa;
        }

        return null;
    }

    public SprawaData mapSprawaData(Sprawa sprawa) {
        SprawaData sprawaData = new SprawaData();
        sprawaData.setDataUrodzenia(sprawa.daneOsobowe().dataUrodzenia());
        sprawaData.setDrugieImie(sprawa.daneOsobowe().drugieImie().isEmpty() ? "" : sprawa.daneOsobowe().drugieImie().get());
        sprawaData.setImie(sprawa.daneOsobowe().imie());
        sprawaData.setMiejsceUrodzenia(sprawa.daneOsobowe().miejsceUrodzenia());
        sprawaData.setNazwisko(sprawa.daneOsobowe().nazwisko());
        sprawaData.setNumerDokumentuIdentyfikacyjnego(sprawa.daneOsobowe().dokumentIdentyfikacyjny().nrDokumentu());
        sprawaData.setPESEL(sprawa.daneOsobowe().pesel());
        sprawaData.setTrescSprawy(sprawa.trescSprawy());
        sprawaData.setTypDokumentu(sprawa.daneOsobowe().dokumentIdentyfikacyjny().typDokumentu().nazwa());
        return sprawaData;
    }
}
