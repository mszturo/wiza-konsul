package controllers;

import controllers.data.DecyzjaData;
import controllers.data.SprawaData;
import play.mvc.Controller;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;
import services.LoginService;
import services.SprawaService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HomeController extends Controller {

    @Inject
    private SprawaService service;

    @Inject
    private LoginService loginService;
    
    private final Form<SprawaData> sprawaForm;
    private final Form<DecyzjaData> decyzjaForm;

    @Inject
    public HomeController(FormFactory formFactory) {
        this.sprawaForm = formFactory.form(SprawaData.class);
        this.decyzjaForm = formFactory.form(DecyzjaData.class);
    }

    public Result index() {
        return ok();
    }

    public Result dodajSprawe() {
        return ok();
    }

    public Result zapiszSprawe() {
        return ok();
    }

    public Result edytujSprawe(Integer id) {
        return ok();
    }

    public Result aktualizujSprawe() {
        return ok();
    }

    public Result pokazSprawy() {
        return ok();
    }

    public Result pokazSprawyArchiwalne() {
        return ok();
    }

    public Result pokazSprawyDoUzupelnienia() {
        return ok();
    }

    public Result usunSprawe(Integer id) {
        return ok();
    }

    public Result wydajDecyzje(Integer id) {
        return ok();
    }

    public Result zapiszDecyzje() {
        return ok();
    }
}
