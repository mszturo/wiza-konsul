package services;

import models.*;
import repositories.PracownikRepo;
import scala.Option;
import scala.concurrent.ExecutionContext;
import scala.util.Try;

import javax.inject.Inject;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Service {
//    Decyzja decyzja = new Decyzja(1, new Date((new java.util.Date()).getTime()), "Bo tak", new Kierownik(1, "aa", "dd"), RodzajDecyzji.Pozytywa());
//    TypDokumentu typDokumentu = new TypDokumentu(1, "Dowod osobisty");
//    DokumentIdentyfikacyjny dokumentIdentyfikacyjny = new DokumentIdentyfikacyjny(1, "AAA20000", typDokumentu);
//    DaneOsobowe daneOsobowe = new DaneOsobowe(1, "Adam", Option.apply("Jozef"), "Malysz", new Date(new java.util.Date().getTime()), "Wroclaw", "000000000", dokumentIdentyfikacyjny);
//    Sprawa sprawa = new Sprawa(1, "ddd", "Tresc", "ID012", new Timestamp(new java.util.Date().getTime()), false, daneOsobowe, decyzja, false);
//
    @Inject
    PracownikRepo pracownikRepo;
//
    private void testRepo() {
        List<Pracownik> pracowniks = new ArrayList<>();
        //pracownikRepo.list().map(val -> pracowniks.addAll(val), ExecutionContext.Implicits.global());

        pracowniks.stream().filter(pracownik -> pracownik.login().equals("")).collect(Collectors.toList());
    }


}
