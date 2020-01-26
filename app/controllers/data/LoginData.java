package controllers.data;

import play.data.validation.Constraints;

public class LoginData {

    @Constraints.Required
    private String login;

    @Constraints.Required
    private String haslo;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }
}
