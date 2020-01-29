package services;

import models.*;
import repositories.*;
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

    public void dodajSprawe(Sprawa sprawa) {
        DaneOsobowe daneOsobowe = sprawa.daneOsobowe();
        DokumentIdentyfikacyjny dokumentIdentyfikacyjny = daneOsobowe.dokumentIdentyfikacyjny();

        dokumentIdentyfikacyjnyRepo.upsert(dokumentIdentyfikacyjny);
        daneOsoboweRepo.upsert(daneOsobowe);
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
        return sprawy;
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
        return sprawy.stream().filter(sprawa -> sprawa.aktualnaDecyzja() != null && sprawa.aktualnaDecyzja().rodzajDecyzji() == RodzajDecyzji.DoUzupelnienia()).collect(Collectors.toList());
    }

    public void dodajDecyzje(Decyzja decyzja, int sprawaId) {
        boolean czyZakonczona = decyzja.rodzajDecyzji() == RodzajDecyzji.DoUzupelnienia() ? false : true;

        Option<Sprawa> sprawaOption = getSprawa(sprawaId);

        if(!sprawaOption.isEmpty()) {
            Sprawa sprawa = sprawaOption.get();
            sprawa.aktualnaDecyzja_$eq(decyzja);
            sprawa.czyZakonczona_$eq(czyZakonczona);

            decyzjaRepo.upsert(decyzja);
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
