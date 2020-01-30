import controllers.data.SprawaData;
import mappers.SprawaMapper;
import models.DaneOsobowe;
import models.DokumentIdentyfikacyjny;
import models.Sprawa;
import models.TypDokumentu;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import repositories.*;
import scala.Option;
import scala.concurrent.Future;
import scala.sys.process.ProcessImpl;
import services.SprawaService;

import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServiceTest {

    @Mock
    SprawaRepo sprawaRepo;

    @Mock
    DokumentIdentyfikacyjnyRepo dokumentIdentyfikacyjnyRepo;

    @Mock
    DaneOsoboweRepo daneOsoboweRepo;

    @Mock
    DecyzjaRepo decyzjaRepo;

    @Mock
    TypDokumentuRepo typDokumentuRepo;

    @Mock
    PracownikRepo pracownikRepo;

    @InjectMocks
    SprawaService sprawaService;

    @Test
    public void test() {
//        TypDokumentu typDokumentu = new TypDokumentu(1, "Dowód osobisty");
//        DokumentIdentyfikacyjny dokumentIdentyfikacyjny = new DokumentIdentyfikacyjny(1, "ddddd", typDokumentu);
//        java.util.Date currentUtilDate = new java.util.Date();
//        java.sql.Date currentDate = new java.sql.Date(currentUtilDate.getTime());
//        DaneOsobowe daneOsobowe = new DaneOsobowe(1, "Jan", Option.apply("Michał"), "Kowalski", currentDate, "Wrocław", "01234567890", dokumentIdentyfikacyjny);
//        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(new java.util.Date().getTime());
//        Sprawa sprawa = new Sprawa(1, "", "Sprawa nr 1", "", currentTimestamp, false, daneOsobowe, Option.empty(), false);
//
//        when(sprawaRepo.get(1)).thenReturn(new Future<Option<Sprawa>>(Option.apply(sprawa)));
    }

    @Test
    public void sprawaMapperTest() {
        SprawaMapper sprawaMapper = new SprawaMapper();

        TypDokumentu typDokumentu = new TypDokumentu(1, "Dowód osobisty");
        DokumentIdentyfikacyjny dokumentIdentyfikacyjny = new DokumentIdentyfikacyjny(1, "ddddd", typDokumentu);
        java.util.Date currentUtilDate = new java.util.Date();
        java.sql.Date currentDate = new java.sql.Date(currentUtilDate.getTime());
        DaneOsobowe daneOsobowe = new DaneOsobowe(1, "Jan", Option.apply("Michał"), "Kowalski", currentDate, "Wrocław", "01234567890", dokumentIdentyfikacyjny);
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(new java.util.Date().getTime());
        Sprawa sprawa = new Sprawa(1, "", "Sprawa nr 1", "", currentTimestamp, false, daneOsobowe, Option.empty(), false);

        SprawaData sprawaData = sprawaMapper.mapSprawaData(sprawa);

        Assert.assertEquals(sprawaData.getDataUrodzenia(), currentUtilDate);
        Assert.assertEquals(sprawaData.getDrugieImie(), sprawa.daneOsobowe().drugieImie().isEmpty() ? "" : sprawa.daneOsobowe().drugieImie().get());
        Assert.assertEquals(sprawaData.getTypDokumentu(), sprawa.daneOsobowe().dokumentIdentyfikacyjny().typDokumentu().nazwa());
        Assert.assertEquals(sprawaData.getNumerDokumentuIdentyfikacyjnego(), sprawa.daneOsobowe().dokumentIdentyfikacyjny().nrDokumentu());
        Assert.assertEquals(sprawaData.getImie(), sprawa.daneOsobowe().imie());
        Assert.assertEquals(sprawaData.getMiejsceUrodzenia(), sprawa.daneOsobowe().miejsceUrodzenia());
        Assert.assertEquals(sprawaData.getNazwisko(), sprawa.daneOsobowe().nazwisko());
        Assert.assertEquals(sprawaData.getPESEL(), sprawa.daneOsobowe().pesel());
        Assert.assertEquals(sprawaData.getTrescSprawy(), sprawa.trescSprawy());
    }
}
