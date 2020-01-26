package services;

import models.*;
import repositories.*;
import scala.Option;
import scala.concurrent.ExecutionContext;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SprawaService {

    @Inject
    DokumentIdentyfikacyjnyRepo dokumentIdentyfikacyjnyRepo;

    @Inject
    DaneOsoboweRepo daneOsoboweRepo;

    @Inject
    SprawaRepo sprawaRepo;

    @Inject
    DecyzjaRepo decyzjaRepo;

    public void dodajSprawe(Sprawa sprawa) {
        DaneOsobowe daneOsobowe = sprawa.daneOsobowe();
        DokumentIdentyfikacyjny dokumentIdentyfikacyjny = daneOsobowe.dokumentIdentyfikacyjny();

        dokumentIdentyfikacyjnyRepo.upsert(dokumentIdentyfikacyjny);
        daneOsoboweRepo.upsert(daneOsobowe);
        sprawaRepo.upsert(sprawa);
    }

    public Option<Sprawa> getSprawa(int id) {
        List<Sprawa> sprawy = new ArrayList<>();
        sprawaRepo.get(id).map(val -> sprawy.add(val.get()), ExecutionContext.Implicits$.MODULE$.global());
        return sprawy.size() == 0 ? Option.empty() : Option.apply(sprawy.get(0));
    }

    public void usunSprawe(int id) {
        Option<Sprawa> sprawa = getSprawa(id);
        if(!sprawa.isEmpty())
            sprawaRepo.delete(sprawa.get());
    }

    public List<Sprawa> getSprawy() {
        List<Sprawa> sprawy = new ArrayList<>();
        sprawaRepo.list().map(val -> sprawy.addAll(val), ExecutionContext.Implicits$.MODULE$.global());
        return sprawy;
    }

    public List<Sprawa> getArchiwalneSprawy() {
        List<Sprawa> sprawy = new ArrayList<>();
        sprawaRepo.list().map(val -> sprawy.addAll(val), ExecutionContext.Implicits$.MODULE$.global());
        return sprawy.stream().filter(sprawa -> sprawa.czyZakonczona() == true).collect(Collectors.toList());
    }

    public List<Sprawa> getSprawyDoUzupelnienia() {
        List<Sprawa> sprawy = new ArrayList<>();
        sprawaRepo.list().map(val -> sprawy.addAll(val), ExecutionContext.Implicits$.MODULE$.global());
        return sprawy.stream().filter(sprawa -> sprawa.aktualnaDecyzja() != null && sprawa.aktualnaDecyzja().rodzajDecyzji() == RodzajDecyzji.DoUzupelnienia()).collect(Collectors.toList());
    }

    public void dodajDecyzje(Sprawa sprawa, Decyzja decyzja) {
        boolean czyZakonczona = decyzja.rodzajDecyzji() == RodzajDecyzji.DoUzupelnienia() ? false : true;
        sprawa.aktualnaDecyzja_$eq(decyzja);
        sprawa.czyZakonczona_$eq(czyZakonczona);

        decyzjaRepo.upsert(decyzja);
        sprawaRepo.upsert(sprawa);
    }
}
