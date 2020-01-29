package services;

import models.Pracownik;
import repositories.PracownikRepo;
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
public class LoginService {

    PracownikRepo pracownikRepo;

    @Inject
    public LoginService(PracownikRepo pracownikRepo) {
        this.pracownikRepo = pracownikRepo;
    }

    public Option<Pracownik> findPracownik(String login, String haslo) {
        List<Pracownik> pracownicy = new ArrayList<>();
        Future<List<Pracownik>> future = pracownikRepo.list().map(val -> {pracownicy.addAll(val); return val;}, ExecutionContext.Implicits$.MODULE$.global());

        try {
            Await.result(future, Duration.Inf());
        } catch(Exception e) {
            e.printStackTrace();
        }

        List<Pracownik> filtered = pracownicy.stream().filter(pracownik -> pracownik.login().equals(login) && pracownik.haslo().equals(haslo)).collect(Collectors.toList());
        if(filtered.size() == 0)
            return Option.empty();
        return Option.apply(filtered.get(0));
    }

}
