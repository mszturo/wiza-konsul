package controllers;

import controllers.data.DecyzjaData;
import controllers.data.LoginData;
import controllers.data.SprawaData;
import mappers.SprawaMapper;
import models.Pracownik;
import models.Sprawa;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Http;
import play.mvc.Result;
import repositories.PrzesylkaRepo;
import scala.Option;
import services.LoginService;
import services.SprawaService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class HomeController extends Controller {

    @Inject
    private SprawaService sprawaService;

    @Inject
    private LoginService loginService;
    
    private final Form<SprawaData> sprawaForm;
    private final Form<DecyzjaData> decyzjaForm;
    private final Form<LoginData> loginForm;

    private final MessagesApi messagesApi;

    @Inject
    public HomeController(FormFactory formFactory, MessagesApi messagesApi) {
        this.sprawaForm = formFactory.form(SprawaData.class);
        this.decyzjaForm = formFactory.form(DecyzjaData.class);
        this.loginForm = formFactory.form(LoginData.class);
        this.messagesApi = messagesApi;
    }

    public Result index(Http.Request request) {
        Messages messages = messagesApi.preferred(request);
        return ok(views.html.index.render(loginForm, request, messages));
    }

    public Result zaloguj(Http.Request request) {
        final Form<LoginData> form = loginForm.bindFromRequest(request);

        if(form.hasErrors()) {
            return badRequest(views.html.index.render(form, request, messagesApi.preferred(request)));
        }

        LoginData loginData = form.get();

        Option<Pracownik> pracownik = loginService.findPracownik(loginData.getLogin(), loginData.getHaslo());

        if(pracownik.isEmpty()) {

        }

        return ok(loginData.getLogin());

    }

    public Result dodajSprawe(Http.Request request) {
        Messages messages = messagesApi.preferred(request);
        return ok(views.html.createSprawa.render(sprawaForm, request, messages));
    }

    public Result zapiszSprawe(Http.Request request) {
        final Form<SprawaData> form = sprawaForm.bindFromRequest(request);

        if(form.hasErrors()) {
            return badRequest(views.html.createSprawa.render(form, request, messagesApi.preferred(request)));
        }

        SprawaData sprawaData = form.get();
        Sprawa sprawa = SprawaMapper.mapSprawa(sprawaData);
        sprawaService.dodajSprawe(sprawa);

        return redirect(routes.HomeController.pokazSprawy());
    }

    public Result edytujSprawe(Integer id) {
        return ok();
    }

    public Result aktualizujSprawe() {
        return ok();
    }

    public Result pokazSprawy() {
        List<Sprawa> sprawy = sprawaService.getSprawy();

        return ok(views.html.listaSpraw.render(sprawy, "Wszystkie sprawy"));
    }

    public Result pokazSprawyArchiwalne() {
        List<Sprawa> sprawy = sprawaService.getArchiwalneSprawy();

        return ok(views.html.listaSpraw.render(sprawy, "Sprawy archiwalne"));
    }

    public Result pokazSprawyDoUzupelnienia() {
        List<Sprawa> sprawy = sprawaService.getSprawyDoUzupelnienia();

        return ok(views.html.listaSpraw.render(sprawy, "Sprawy do uzupełnienia"));
    }

    public Result usunSprawe(Integer id) {
        sprawaService.usunSprawe(id);

        return redirect(routes.HomeController.pokazSprawy());
    }

    public Result wydajDecyzje(Http.Request request, Integer id) {
        Option<Sprawa> sprawa = sprawaService.getSprawa(id);

        if(sprawa.isEmpty())
            return notFound();

        return ok(views.html.wydajDecyzje.render(decyzjaForm, sprawa.get(), request, messagesApi.preferred(request)));
    }

    public Result zapiszDecyzje(Http.Request request) {
//        final Form<DecyzjaData> form = decyzjaForm.bindFromRequest(request);
//
//        if(form.hasErrors()) {
//            return badRequest(views.html.wydajDecyzje.render(form, request, messagesApi.preferred(request)));
//        }
//
//        //sprawaService.dodajDecyzje();
//
//        return redirect(routes.HomeController.pokazSprawy());
        return ok();
    }
}
