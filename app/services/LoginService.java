package services;

import models.Pracownik;
import repositories.PracownikRepo;
import scala.Option;
import scala.concurrent.ExecutionContext;

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
        pracownikRepo.list().map(val -> pracownicy.addAll(val), ExecutionContext.Implicits$.MODULE$.global());
        List<Pracownik> filtered = pracownicy.stream().filter(pracownik -> pracownik.login().equals(login) && pracownik.haslo().equals(haslo)).collect(Collectors.toList());
        if(filtered.size() == 0)
            return Option.empty();
        return Option.apply(filtered.get(0));
    }

}
