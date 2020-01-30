package services;

import models.*;
import repositories.*;
import scala.None;
import scala.Option;
import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class SprawaService {

    @Inject
    DokumentIdentyfikacyjnyRepo dokumentIdentyfikacyjnyRepo;

    @Inject
    DaneOsoboweRepo daneOsoboweRepo;

    @Inject
    SprawaRepo sprawaRepo;

    @Inject
    DecyzjaRepo decyzjaRepo;

    @Inject
    TypDokumentuRepo typDokumentuRepo;

    @Inject
    PracownikRepo pracownikRepo;

//    private static int DaneOsoboweMaxInt = 1;
//    private static int DokumentIdentyifkacyjnyMaxInt = 1;
//    private static int DecyzjaMaxInt = 1;

    public void dodajSprawe(Sprawa sprawa) {

        DaneOsobowe daneOsobowe = sprawa.daneOsobowe();
        DokumentIdentyfikacyjny dokumentIdentyfikacyjny = daneOsobowe.dokumentIdentyfikacyjny();

        List<Option<Object>> id = new ArrayList<>();

        //dokumentIdentyfikacyjny.id_$eq(DokumentIdentyifkacyjnyMaxInt++);
        //daneOsobowe.id_$eq(DaneOsoboweMaxInt++);
        try {
            Await.result(dokumentIdentyfikacyjnyRepo.upsert(dokumentIdentyfikacyjny)
                    .map(val -> {id.add(val); return val;}, ExecutionContext.Implicits$.MODULE$.global()), Duration.Inf());
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(!id.get(0).isEmpty())
            dokumentIdentyfikacyjny.id_$eq((Long)id.get(0).get());

        id.remove(0);
        try {
            Await.result(daneOsoboweRepo.upsert(daneOsobowe)
                    .map(val -> {id.add(val); return val;}, ExecutionContext.Implicits$.MODULE$.global()), Duration.Inf());
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(!id.get(0).isEmpty())
            daneOsobowe.id_$eq((Long)id.get(0).get());

        sprawaRepo.upsert(sprawa);
    }

    public Option<Sprawa> getSprawa(int id) {
        List<Option<Sprawa>> sprawa = new ArrayList<>();
        Future<Option<Sprawa>> future = sprawaRepo.get(id).map(val -> {sprawa.add(val); return val;}, ExecutionContext.Implicits$.MODULE$.global());
        try {
            Await.result(future, Duration.Inf());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return sprawa.get(0);
    }

    public Option<Pracownik> getPracownik(int id) {
        List<Option<Pracownik>> pracownik = new ArrayList<>();
        Future<Option<Pracownik>> future = pracownikRepo.get(id).map(val -> {pracownik.add(val); return val;}, ExecutionContext.Implicits$.MODULE$.global());
        try {
            Await.result(future, Duration.Inf());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return pracownik.get(0);
    }

    public void usunSprawe(int id) {
        Option<Sprawa> sprawa = getSprawa(id);
        if(!sprawa.isEmpty())
            sprawaRepo.delete(sprawa.get());
    }

    public List<Sprawa> getSprawy() {
        List<Sprawa> sprawy = new ArrayList<>();
        Future<List<Sprawa>> future = sprawaRepo.list().map(val -> {sprawy.addAll(val); return val; }, ExecutionContext.Implicits$.MODULE$.global());
        try {
            Await.result(future, Duration.Inf());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return sprawy.stream().filter(sprawa -> sprawa.czyZakonczona() == false).collect(Collectors.toList());
    }

    public List<Sprawa> getArchiwalneSprawy() {
        List<Sprawa> sprawy = new ArrayList<>();
        Future<List<Sprawa>> future = sprawaRepo.list().map(val -> {sprawy.addAll(val); return val; }, ExecutionContext.Implicits$.MODULE$.global());
        try {
            Await.result(future, Duration.Inf());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return sprawy.stream().filter(sprawa -> sprawa.czyZakonczona() == true).collect(Collectors.toList());
    }

    public List<Sprawa> getSprawyDoUzupelnienia() {
        List<Sprawa> sprawy = new ArrayList<>();
        Future<List<Sprawa>> future = sprawaRepo.list().map(val -> {sprawy.addAll(val); return val; }, ExecutionContext.Implicits$.MODULE$.global());
        try {
            Await.result(future, Duration.Inf());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return sprawy.stream().filter(sprawa -> !sprawa.aktualnaDecyzja().isEmpty() && sprawa.aktualnaDecyzja().get().rodzajDecyzji() == RodzajDecyzji.DoUzupelnienia()).collect(Collectors.toList());
    }

    public void dodajDecyzje(Decyzja decyzja, int sprawaId) {
        boolean czyZakonczona = decyzja.rodzajDecyzji() == RodzajDecyzji.DoUzupelnienia() ? false : true;

        Option<Sprawa> sprawaOption = getSprawa(sprawaId);

        if(!sprawaOption.isEmpty()) {
//            decyzja.id_$eq(DecyzjaMaxInt++);
            Option<Decyzja> decyzjaOption = Option.apply(decyzja);
            Sprawa sprawa = sprawaOption.get();
            sprawa.aktualnaDecyzja_$eq(decyzjaOption);
            sprawa.czyZakonczona_$eq(czyZakonczona);

            List<Option<Object>> id = new ArrayList<>();

            try {
                Await.result(decyzjaRepo.upsert(decyzja)
                        .map(val -> {id.add(val); return val;}, ExecutionContext.Implicits$.MODULE$.global()), Duration.Inf());
            } catch(Exception e) {
                e.printStackTrace();
            }

            if(!id.get(0).isEmpty())
                decyzja.id_$eq((Long)id.get(0).get());

            sprawaRepo.upsert(sprawa);
        }
    }

    public List<TypDokumentu> getTypyDokumentow() {
        List<TypDokumentu> typyDokumentow = new ArrayList<>();
        Future<List<TypDokumentu>> future = typDokumentuRepo.list().map(val -> {typyDokumentow.addAll(val); return val;}, ExecutionContext.Implicits$.MODULE$.global());
        try {
            Await.result(future, Duration.Inf());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return typyDokumentow;
    }

    public Option<TypDokumentu> getTypDokumentu(int id) {
        List<Option<TypDokumentu>> typDokumentu = new ArrayList<>();
        Future<Option<TypDokumentu>> future = typDokumentuRepo.get(id).map(val -> {typDokumentu.add(val); return val;}, ExecutionContext.Implicits$.MODULE$.global());
        try {
            Await.result(future, Duration.Inf());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return typDokumentu.get(0);
    }
}
