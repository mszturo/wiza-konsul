package controllers;

import controllers.data.DecyzjaData;
import controllers.data.LoginData;
import controllers.data.SprawaData;
import mappers.DecyzjaMapper;
import mappers.SprawaMapper;
import models.*;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Http;
import play.mvc.Result;
import repositories.PrzesylkaRepo;
import scala.Option;
import scala.collection.JavaConverters;
import scala.collection.immutable.Seq;
import scala.jdk.CollectionConverters;
import services.LoginService;
import services.SprawaService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class HomeController extends Controller {

    @Inject
    private SprawaService sprawaService;

    @Inject
    private LoginService loginService;

    @Inject
    private SprawaMapper sprawaMapper;

    @Inject
    private DecyzjaMapper decyzjaMapper;

    private FormFactory formFactory;

    private final Form<SprawaData> sprawaForm;
    private final Form<DecyzjaData> decyzjaForm;
    private final Form<LoginData> loginForm;

    private final MessagesApi messagesApi;

    @Inject
    public HomeController(FormFactory formFactory, MessagesApi messagesApi) {
        this.formFactory = formFactory;
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
            return badRequest(views.html.index.render(form, request, messagesApi.preferred(request)));
        }

        return redirect(routes.HomeController.pokazSprawy())
                .addingToSession(request, "id", "" + pracownik.get().id())
                .addingToSession(request, "czyKierownik", pracownik.get() instanceof Kierownik ? "True" : "False");

    }

    public Result wyloguj(Http.Request request) {
        return redirect(routes.HomeController.index())
                .removingFromSession(request, "id")
                .removingFromSession(request, "czyKierownik");
    }

    public Result dodajSprawe(Http.Request request) {
        if(!request.session().getOptional("id").isPresent()) {
            return redirect(routes.HomeController.index());
        }

        Messages messages = messagesApi.preferred(request);
        List<TypDokumentu> typyDokumentow = sprawaService.getTypyDokumentow();
        Seq<TypDokumentu> typy = CollectionConverters.IterableHasAsScala(typyDokumentow).asScala().toList();

        return ok(views.html.createSprawa.render(sprawaForm, typy, request, messages));
    }

    public Result zapiszSprawe(Http.Request request) {
        final Form<SprawaData> form = sprawaForm.bindFromRequest(request);

        if(form.hasErrors()) {
            List<TypDokumentu> typyDokumentow = sprawaService.getTypyDokumentow();
            Seq<TypDokumentu> typy = CollectionConverters.IterableHasAsScala(typyDokumentow).asScala().toList();

            return badRequest(views.html.createSprawa.render(form, typy, request, messagesApi.preferred(request)));
        }

        SprawaData sprawaData = form.get();
        Sprawa sprawa = sprawaMapper.mapSprawa(sprawaData);

        if(request.session().getOptional("sprawaId").isPresent()) {
            Long sprawaId = Long.parseLong(request.session().getOptional("sprawaId").get());
            Option<Sprawa> persistentSprawa = sprawaService.getSprawa(sprawaId.intValue());
            if(!persistentSprawa.isEmpty())
                sprawa = sprawaMapper.mapSprawa(sprawaData, persistentSprawa.get());
        }

        sprawaService.dodajSprawe(sprawa);

        return redirect(routes.HomeController.pokazSprawy()).removingFromSession(request, "sprawaId");
    }

    public Result edytujSprawe(Http.Request request, Long id) {
        if(!request.session().getOptional("id").isPresent()) {
            return redirect(routes.HomeController.index());
        }

        Option<Sprawa> sprawa = sprawaService.getSprawa(id.intValue());

        if(sprawa.isEmpty())
            return notFound();

        Form<SprawaData> sprawaForm = formFactory.form(SprawaData.class).fill(sprawaMapper.mapSprawaData(sprawa.get()));

        Messages messages = messagesApi.preferred(request);
        List<TypDokumentu> typyDokumentow = sprawaService.getTypyDokumentow();
        Seq<TypDokumentu> typy = CollectionConverters.IterableHasAsScala(typyDokumentow).asScala().toList();

        return ok(views.html.createSprawa.render(sprawaForm, typy, request, messages)).addingToSession(request, "sprawaId", id.toString());
    }

    public Result pokazSprawy(Http.Request request) {
        if(!request.session().getOptional("id").isPresent()) {
            return redirect(routes.HomeController.index());
        }

        List<Sprawa> sprawy = sprawaService.getSprawy();

        return ok(views.html.listaSpraw.render(sprawy, "Wszystkie sprawy"));
    }

    public Result pokazSprawyArchiwalne(Http.Request request) {
        if(!request.session().getOptional("id").isPresent()) {
            return redirect(routes.HomeController.index());
        }

        List<Sprawa> sprawy = sprawaService.getArchiwalneSprawy();

        return ok(views.html.listaSpraw.render(sprawy, "Sprawy archiwalne"));
    }

    public Result pokazSprawyDoUzupelnienia(Http.Request request) {
        if(!request.session().getOptional("id").isPresent()) {
            return redirect(routes.HomeController.index());
        }

        List<Sprawa> sprawy = sprawaService.getSprawyDoUzupelnienia();

        return ok(views.html.listaSpraw.render(sprawy, "Sprawy do uzupełnienia"));
    }

    public Result usunSprawe(Long id) {
        sprawaService.usunSprawe(id.intValue());

        return redirect(routes.HomeController.pokazSprawy());
    }

    public Result wydajDecyzje(Http.Request request, Long id) {
        if(!request.session().getOptional("id").isPresent() && request.session().getOptional("czyKierownik").get().equals("False")) {
            return redirect(routes.HomeController.pokazSprawy());
        }

        Option<Sprawa> sprawa = sprawaService.getSprawa(id.intValue());

        if(sprawa.isEmpty())
            return notFound();

        return ok(views.html.wydajDecyzje.render(decyzjaForm, sprawa.get(), request, messagesApi.preferred(request)));
    }

    public Result zapiszDecyzje(Http.Request request, Long sprawaId) {
        final Form<DecyzjaData> form = decyzjaForm.bindFromRequest(request);

        if(form.hasErrors()) {
            Option<Sprawa> sprawa = sprawaService.getSprawa(sprawaId.intValue());

            if(sprawa.isEmpty())
                return notFound();

            return badRequest(views.html.wydajDecyzje.render(form, sprawa.get(), request, messagesApi.preferred(request)));
        }

        Pracownik pracownik = sprawaService.getPracownik(Integer.parseInt(request.session().getOptional("id").get())).get();

        DecyzjaData decyzjaData = form.get();
        Decyzja decyzja = decyzjaMapper.mapDecyzja(decyzjaData, (Kierownik) pracownik);
        sprawaService.dodajDecyzje(decyzja, sprawaId.intValue());

        return redirect(routes.HomeController.pokazSprawy());
    }
}
